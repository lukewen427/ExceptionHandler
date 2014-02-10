/*
 * ServiceProcess.java
 */

package com.connexience.server.service;
import com.connexience.server.rmi.IControllableService;
import com.connexience.server.rmi.IControllableServiceConnection;
import com.connexience.server.workflow.rpc.*;
import com.connexience.server.workflow.util.*;
import com.connexience.server.util.RegistryUtil;
import com.connexience.server.util.SerializationUtils;
import com.connexience.server.util.XmlUtils;
import com.connexience.server.workflow.xmlstorage.*;

import java.io.*;
import java.util.*;

/**
 * This class represents a container process for a service. It contains the
 * path to the directory where all of the service .jars are stored and captures the
 * input/output/error streams of the actual service.
 * @author hugo
 */
public class ServiceProcessContainer implements Runnable {
    /** Parent service host */
    private ServiceHost parent;

    /** Actual service runtime */
    private Process serviceProcess = null;

    /** Input stream dumper */
    private StreamDumper inputDumper = null;

    /** Error stream dumper */
    private StreamDumper errorDumper = null;

    /** Service container directory */
    private File serviceDirectory;

    /** Temporary directory for this service */
    private File tempDir;

    /** Data directory for this service */
    private File dataDir;

    /** Classpath for running the service */
    private String classpath;

    /** Service name */
    private String name;

    /** RMI Name of the service */
    private String rmiName;

    /** Class name of the service. This is stored in the classname.txt file
     * in the service directory */
    private String classname;

    /** Log file for this service */
    private File logFile = null;

    /** RMI Port for the service */
    private int rmiPort = -1;

    /** Is this service to be started in debug mode */
    private boolean debugEnabled = false;

    /** Debugger port */
    private int debuggerPort = 5005;

    /** Log file writer */
    private PrintStream logFileStream = null;

    /** Thread to flush the log file */
    private LogFileFlusherThread flusherThread = null;

    /** Starting RMI Port for searching for suitable registry locations */
    private int rmiSearchStartPort = 1100;
    
    /** Create a server process */
    public ServiceProcessContainer(File serviceDirectory, File tempDir, File dataDir, ServiceHost parent){
        this.parent = parent;
        this.serviceDirectory = serviceDirectory;
        this.tempDir = tempDir;
        this.dataDir = dataDir;
        this.classpath = buildClasspath();
        this.name = serviceDirectory.getName();
        this.rmiName = name + "Ctl";
        parseServiceXml();
    }

    /** Get the directory containing the service files */
    public File getServiceDirectory(){
        return serviceDirectory;
    }

    /** Get the service temp directory */
    public File getTemporaryDirectory(){
        return tempDir;
    }

    /** Get the service log file */
    public File getLogFile(){
        return logFile;
    }
        
    /** Get the properties xml file for this service */
    public String getServicePropertiesXml() throws IOException {
        File propertiesFile = new File(System.getProperty("user.home") + File.separator + ".inkspot" + File.separator + name + ".xml");
        if(propertiesFile.exists()){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ZipUtils.copyFileToOutputStream(propertiesFile, stream);
            stream.flush();
            stream.close();
            return new String(stream.toByteArray());
        } else {
            return null;
        }
    }

    /** Set the service properties XML file */
    public void setServicePropertiesXml(String xml) throws IOException {
        File propertiesFile = new File(System.getProperty("user.home") + File.separator + ".inkspot" + File.separator + name + ".xml");
        FileOutputStream stream = new FileOutputStream(propertiesFile);
        stream.write(xml.getBytes());
        stream.flush();
        stream.close();
    }

    /** Delete the service properties file */
    public void deleteServicePropertiesXml() throws IOException {
        File propertiesFile = new File(System.getProperty("user.home") + File.separator + ".inkspot" + File.separator + name + ".xml");
        if(propertiesFile.exists()){
            propertiesFile.delete();
        }
    }

    /** Find the classname of the service */
    private void parseServiceXml(){
        File serviceXml = new File(serviceDirectory, "service.xml");
        if(serviceXml.exists()){
            try {
                FileInputStream stream = new FileInputStream(serviceXml);
                Properties props = XmlUtils.extractChildNodes(XmlUtils.readXmlDocumentFromStream(stream));
                stream.close();
                name = props.getProperty("name");
                classname = props.getProperty("classname");

            } catch (Exception e){
                System.out.println("Error reading service.xml: " + e.getMessage());
                classname = null;
            }
        }
    }
    
    /** Build the classpath for the service */
    private String buildClasspath(){
        File[] libraries = serviceDirectory.listFiles();
        StringBuffer cpb = new StringBuffer();
        File entry;
        String path;
        int count = 0;
        for(int i=0;i<libraries.length;i++){
            entry = libraries[i];
            if(entry.getName().endsWith(".jar")){
                if(count>0){
                    cpb.append(File.pathSeparator);
                }
                cpb.append(entry.getPath());
                count++;
            }
        }
        return cpb.toString();
    }

    /** Stop the process */
    public void stopProcess(){
        if(logFileStream!=null){
            logFileStream.println("Destroying process...");
        }
        
        if(serviceProcess!=null){
            //serviceProcess.destroy();
            try {
                IControllableService svc = (IControllableService) RegistryUtil.lookup("localhost", rmiName);
                IControllableServiceConnection c = svc.openConnection();
                c.stopService();
            } catch (Exception e){
                if(!(e instanceof NullPointerException)){
                    System.out.println("Error shutting down via RMI. Terminating");
                    serviceProcess.destroy();
                }
            }
            
        }
        notifyShutdown();
        serviceProcess = null;

        if(inputDumper!=null){
            inputDumper.stop();
            inputDumper = null;
        }

        if(errorDumper!=null){
            errorDumper.stop();
            errorDumper = null;
        }

        if(flusherThread!=null){
            flusherThread.setStopFlag();
            flusherThread = null;
        }


        if(logFileStream!=null){
            try {
                logFileStream.flush();
                logFileStream.close();
            } catch (Exception ex){

            }
        }
        logFileStream = null;
    }

    /** Is this process running */
    public boolean running(){
        if(serviceProcess!=null){
            return true;
        } else {
            return false;
        }
    }

    /** Get the RMI server port that has been assigned to the child process */
    public int getRmiPort() {
        return rmiPort;
    }

    /** Set the starting port for RMI registry search */
    public void setRmiSearchStartPort(int rmiSearchStartPort) {
        this.rmiSearchStartPort = rmiSearchStartPort;
    }

    /** Reset the log file and open a writer */
    private void resetLogFile(){
        if(flusherThread!=null){
            flusherThread.setStopFlag();
            flusherThread = null;
        }

        logFile = new File(tempDir, "service.log");
        try {
            if(logFileStream!=null){
                try {
                    logFileStream.flush();
                    logFileStream.close();
                    logFileStream = null;
                } catch (Exception ex){}
            }

            if(logFile.exists()){
                logFile.delete();
            }

            logFileStream = new PrintStream(logFile);
            flusherThread = new LogFileFlusherThread();
            flusherThread.start();
        } catch (Exception e){

        }
    }

    /** Run the process */
    public void run(){
        if(serviceProcess==null){
            // Find the next free RMI Server
            try {
                // Check the service directories exist
                if(!tempDir.exists()){
                    tempDir.mkdirs();
                }

                if(!dataDir.exists()){
                    dataDir.mkdirs();
                }

                rmiPort = RegistryUtil.findNextRegistryPort(rmiSearchStartPort, 200);

                StringBuffer cmd = new StringBuffer();
                
                // Basic java executable
                String javaHome = System.getProperty("java.home");
                cmd.append(javaHome + File.separator + "bin" + File.separator + "java ");

                // Start in debug if required
                if(debugEnabled){
                    cmd.append(" -Xdebug -Xrunjdwp:transport=dt_socket,address=" + debuggerPort + ",server=y,suspend=y ");
                }

                cmd.append("-cp " + classpath + " com.connexience.server.service.ServiceProcess " );
                cmd.append(classname);
                cmd.append(" " + rmiName + " " + rmiPort + " " + parent.getClient().getServerUrl().toString() + " " + serviceDirectory.getPath() + " " + tempDir.getPath() + " " + dataDir.getPath() + " " + parent.getGuid());

                String cmdString = cmd.toString();

                resetLogFile();
                serviceProcess = Runtime.getRuntime().exec(cmdString);
                inputDumper = new StreamDumper(serviceProcess.getInputStream(), logFileStream);
                errorDumper = new StreamDumper(serviceProcess.getErrorStream(), logFileStream);
                notifyStartup();
                serviceProcess.waitFor();
                serviceProcess = null;
                if(inputDumper!=null){
                    inputDumper.stop();
                }

                if(errorDumper!=null){
                    errorDumper.stop();
                }
                inputDumper = null;
                errorDumper = null;

                if(flusherThread!=null){
                    flusherThread.setStopFlag();
                    flusherThread = null;
                }

                if(logFileStream!=null){
                    try {
                        logFileStream.println("Process exited");
                        logFileStream.flush();
                        logFileStream.close();
                    } catch (Exception ex){
                        System.out.println("Error closing log file");
                    }
                }
                logFileStream = null;
                notifyShutdown();
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("Error starting service: " + e.getMessage());
            }

        } else {
            System.out.println("Process is already running");
        }
    }

    /** Start the process */
    public void startProcess() throws Exception {
        new Thread(this).start();
    }

    /** Restart the proccess */
    public void restartProcess() throws Exception {
        if(running()){
            stopProcess();
            try {
                Thread.sleep(2000);
            } catch (Exception e){

            }
            startProcess();
        } else {
            throw new Exception("Process: " + name + " is not running");
        }
    }


    /** Notify the main server that this service has started */
    private void notifyStartup(){
        CallInvocationListener listener = new CallInvocationListener() {

            @Override
            public void callSucceeded(CallObject call) {
                
            }

            @Override
            public void callFailed(CallObject call) {
                System.out.println("Error notifying service startup: " + call.getStatusMessage());
            }
        };

        try {
            CallObject call = new CallObject("SVCServiceStartup");
            call.getCallArguments().add("HostIP", parent.getServerIp());
            call.getCallArguments().add("HostGUID", parent.getGuid());
            call.getCallArguments().add("ServiceName", name);
            call.getCallArguments().add("RMIPort", rmiPort);
            parent.getClient().asyncCall(call, listener);
        } catch (Exception e){
            System.out.println("Error sending startup notification: " + e.getMessage());
        }
    }

    /** Notify the main server that this service has finished */
    private void notifyShutdown(){
        CallInvocationListener listener = new CallInvocationListener() {

            @Override
            public void callSucceeded(CallObject call) {

            }

            @Override
            public void callFailed(CallObject call) {
                System.out.println("Error notifying service shutdown: " + call.getStatusMessage());
            }
        };

        try {
            CallObject call = new CallObject("SVCServiceShutdown");
            call.getCallArguments().add("HostIP", parent.getServerIp());
            call.getCallArguments().add("HostGUID", parent.getGuid());
            call.getCallArguments().add("ServiceName", name);
            parent.getClient().asyncCall(call, listener);
        } catch (Exception e){
            System.out.println("Error sending shutdown notification: " + e.getMessage());
        }
    }
    
    public String getName() {
        return name;
    }

    public String getRmiName(){
        return rmiName;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebuggerPort(int debuggerPort) {
        this.debuggerPort = debuggerPort;
    }

    public int getDebuggerPort() {
        return debuggerPort;
    }

    /** Thread to periodically flush the log file */
    private class LogFileFlusherThread extends Thread {
        private boolean stopFlag = false;

        public LogFileFlusherThread() {
            super();
            setDaemon(true);
        }

        public void run() {
            while (!stopFlag) {
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                }
                try {
                    if (logFileStream != null) {
                        logFileStream.flush();
                    }
                } catch (Exception e) {
                }
            }

        }
        public synchronized void setStopFlag(){
            stopFlag = true;
        }
    }

    /** Get the associated document version for this service if there is one */
    public DocumentVersionWrapper getVersion(){
        File docFile = new File(serviceDirectory, "_ver.ser");
        if(docFile.exists()){
            try {
                return (DocumentVersionWrapper)SerializationUtils.deserialize(docFile);
            } catch (Exception e){
                return null;
            }
        } else {
            return null;
        }
    }

    /** Get the associated document for this service if there is one */
    public DocumentRecordWrapper getDocument(){
        File docFile = new File(serviceDirectory, "_doc.ser");
        if(docFile.exists()){
            try {
                return (DocumentRecordWrapper)SerializationUtils.deserialize(docFile);
            } catch (Exception e){
                return null;
            }
        } else {
            return null;
        }
    }
}