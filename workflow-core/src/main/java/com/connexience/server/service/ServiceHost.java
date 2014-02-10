/*
 * ServiceHost.java
 */

package com.connexience.server.service;
import com.connexience.server.service.util.*;
import com.connexience.server.rmi.*;
import com.connexience.server.util.RegistryUtil;
import com.connexience.server.workflow.util.ZipUtils;
import com.connexience.server.util.RandomGUID;
import com.connexience.server.util.XmlUtils;
import com.connexience.server.util.SerializationUtils;
import com.connexience.server.workflow.rpc.*;
import com.connexience.server.model.workflow.*;
import com.connexience.server.model.security.*;

import org.pipeline.core.xmlstorage.prefs.*;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;
import java.net.*;


/**
 * This class provides a container process that can be used to manage
 * various types of service such as the cloud workflow engine, database
 * service etc.
 * @author hugo
 */
public class ServiceHost extends UnicastRemoteObject implements IServiceHost {
    /** List of open shells */
    private ArrayList<ServiceHostShellServer> shells = new ArrayList<ServiceHostShellServer>();
    
    /** List of service processes */
    private Hashtable<String, ServiceProcessContainer> services = new Hashtable<String, ServiceProcessContainer>();

    /** Working directory */
    private File workingDir;

    /** Services top level directory */
    private File servicesDir;

    /** Temporary storage dir for services */
    private File tempDir;

    /** Persistent data storage for services */
    private File dataDir;

    /** Client for communicating with server */
    private RPCClient client;

    /** GUID for this host */
    private String guid;

    public ServiceHost(File workingDir) throws RemoteException {
        this.workingDir = workingDir;
        this.servicesDir = new File(this.workingDir, "services");
        this.tempDir = new File(this.workingDir, "temp");
        this.dataDir = new File(this.workingDir, "data");

        // Create services directory if needed
        if(!servicesDir.exists()){
            servicesDir.mkdirs();
        }

        // Create temp directory if needed
        if(!tempDir.exists()){
            tempDir.mkdirs();
        }

        // Create data directory if needed
        if(!dataDir.exists()){
            dataDir.mkdirs();
        }
    }

    /** Open a connection */
    public IServiceHostConnection openConnection(String username, String password) throws RemoteException {
        return new ServiceHostConnection(this);
    }

    /** Startup the service host */
    public void start() throws Exception {
        // Load the properties
        if(!PreferenceManager.loadPropertiesFromHomeDir(".inkspot", "ServiceHost.xml")){
            // Create preferences
            PreferenceManager.getSystemPropertyGroup("Server").add("Host", "localhost");
            PreferenceManager.getSystemPropertyGroup("Server").add("Port", 8080);

            PreferenceManager.getSystemPropertyGroup("Server").add("OverrideDetectedLocalIP", false);
            PreferenceManager.getSystemPropertyGroup("Server").add("LocalIP", "127.0.0.1");

            PreferenceManager.saveProperties();
        }

        // Load the server GUID
        File guidFile = PreferenceManager.getFileFromHomeDir(".inkspot", "hostguid");
        if(guidFile.exists()){
            guid = PreferenceManager.readSingleLineFileFromHomeDir(".inkspot", "hostguid");
            System.out.println("Read GUID from file: " + guid);
        } else {
            guid = new RandomGUID().toString();
            System.out.println("Creating GUID file: " + guid);
            PreferenceManager.createSingleLineFileInHomeDir(".inkspot", "hostguid", guid);
        }

        // Load public and private keys
        if(!PreferenceManager.loadKeystoreFromHomeDir(".inkspot", "ServiceHost.keystore")) {
            System.out.println("Need to fetch the keystore from the server first");
            System.exit(1);
        }

        // Set up the RPC Client with the appropriate keys
        client = new RPCClient("http://" + PreferenceManager.getSystemPropertyGroup("Server").stringValue("Host", "localhost") + ":" + PreferenceManager.getSystemPropertyGroup("Server").intValue("Port", 8080) + "/WorkflowServer/WorkflowServlet");
        client.setSecurityMethod(RPCClient.PRIVATE_KEY_SECURITY);
        client.setSigningUserId(PreferenceManager.getCertificateOwnerId());
        client.setPrivateKey(PreferenceManager.getPrivateKey());
        client.setSerializationMethod(RPCClient.XML_SERIALIZATION);
        
        // Create a registry
        if(!alreadyRunning()){
            RegistryUtil.registerToRegistry("ServiceHost", this, true);

            // Tell the server that this host has started
            notifyStartup();

            // Start everything going
            new Thread(new Runnable(){
                public void run(){
                    buildProcessContainers();
                    startProcesses();
                }
            }).start();

        } else {
            System.out.println("Service Host is already running on this machine");
            System.exit(1);
        }
    }

    /** Is the service already running */
    private boolean alreadyRunning(){
        try {
            Object obj = RegistryUtil.lookup("localhost", "ServiceHost");
            if(obj instanceof IServiceHost){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            return false;
        }
    }
    /** Stop this service host */
    public void stop() throws Exception {
        RegistryUtil.unregisterFromRegistry("ServiceHost");
        stopProcesses();

        CallInvocationListener listener = new CallInvocationListener() {
            @Override
            public void callSucceeded(CallObject call) {
                System.exit(0);
            }

            @Override
            public void callFailed(CallObject call) {
                System.out.println("Could not notify server of host shutdown: " + call.getStatusMessage());
                System.exit(1);
            }
        };

        try {
            CallObject call = new CallObject("SVCHostShutdown");
            if(PreferenceManager.getSystemPropertyGroup("Server").booleanValue("OverrideDetectedLocalIP", false)){
                String ip = PreferenceManager.getSystemPropertyGroup("Server").stringValue("LocalIP", "127.0.0.1");
                System.out.println("Registering shutdown with overridden IP: " + ip);
                call.getCallArguments().add("HostIP", ip);
                call.getCallArguments().add("HostGUID", guid);
            } else {
                String ip = InetAddress.getLocalHost().getHostAddress();
                System.out.println("Registering shutdown with detected IP: " + ip);
                call.getCallArguments().add("HostIP", ip);
                call.getCallArguments().add("HostGUID", guid);
            }
            
            client.asyncCall(call, listener);
        } catch (Exception e){
            System.out.println("Error notifying server of host startup: " + e.getMessage());
        }
    }
    

    /** Get an enumeration of the services */
    public Enumeration<ServiceProcessContainer> getServices(){
        return services.elements();
    }

    /** Build container objects for all of the service subdirectory in /services */
    private synchronized void buildProcessContainers(){
        File[] subdirs = servicesDir.listFiles();
        File serviceTempDir;
        File serviceDataDir;
        ServiceProcessContainer service;
        String name;
        for(int i=0;i<subdirs.length;i++){
            if(subdirs[i].isDirectory()){
                // Create a service
                name = subdirs[i].getName();
                if(!name.equals("bin") && !name.equals("target") && !name.equals("src")){
                    if(!services.containsKey(subdirs[i].getName())){
                        // Check service temporary dir
                        serviceTempDir = new File(tempDir, subdirs[i].getName());
                        serviceDataDir = new File(dataDir, subdirs[i].getName());
                        if(!serviceTempDir.exists()){
                            serviceTempDir.mkdirs();
                        }
                        service = new ServiceProcessContainer(subdirs[i], serviceTempDir, serviceDataDir, this);
                        services.put(service.getName(), service);
                        notifyDeploy(name);
                    }
                }
            }
        }
    }

    /** Start all of the processes */
    private synchronized void startProcesses(){
        Enumeration<ServiceProcessContainer> i = services.elements();
        ServiceProcessContainer service;
        int rmiLocation = 1100;

        while(i.hasMoreElements()){
            service = i.nextElement();
            service.setRmiSearchStartPort(rmiLocation);
            rmiLocation = rmiLocation + 10;
            if(!service.running()){
                try {
                    service.startProcess();
                } catch (Exception e){
                    System.out.println("Error starting process: " + service.getName());
                }
            }
        }
    }

    /** Stop all of the services */
    private synchronized void stopProcesses(){
        Enumeration<ServiceProcessContainer> i = services.elements();
        ServiceProcessContainer service;
        while(i.hasMoreElements()){
            service = i.nextElement();
            if(service.running()){
                service.stopProcess();
            }
        }
    }

    /** Tell the server that something has been deployed */
    private void notifyDeploy(String name){
        CallInvocationListener listener = new CallInvocationListener() {
            public void callSucceeded(CallObject call) {

            }
            public void callFailed(CallObject call) {
                System.out.println("Error registering service deploy: " + call.getStatusMessage());
            }
        };

        try {
            CallObject call = new CallObject("SVCServiceDeployment");
            call.getCallArguments().add("HostIP", getServerIp());
            call.getCallArguments().add("HostGUID", guid);
            call.getCallArguments().add("UserID", PreferenceManager.getCertificateOwnerId());
            call.getCallArguments().add("ServiceName", name);
            client.asyncCall(call, listener);
        } catch (Exception e){
            System.out.println("Error notifying server of service deploy: " + e.getMessage());
        }
    }

    /** Tell the server that a service has been undeployed */
    private void notifyUndeploy(String name) {
        CallInvocationListener listener = new CallInvocationListener() {
            public void callSucceeded(CallObject call) {

            }
            public void callFailed(CallObject call) {
                System.out.println("Error registering service undeploy: " + call.getStatusMessage());
            }
        };

        try {
            CallObject call = new CallObject("SVCServiceUndeployment");
            call.getCallArguments().add("HostIP", getServerIp());
            call.getCallArguments().add("HostGUID", guid);
            call.getCallArguments().add("UserID", PreferenceManager.getCertificateOwnerId());
            call.getCallArguments().add("ServiceName", name);
            client.asyncCall(call, listener);
        } catch (Exception e){
            System.out.println("Error notifying server of service undeploy: " + e.getMessage());
        }
    }

    /** Does a ticket belong to an admin user */
    public boolean isAdminTicket(Ticket t) throws Exception {
        CallObject call = new CallObject("SVCIsAdminUser");
        call.getCallArguments().add("UserID", t.getUserId());
        client.syncCall(call);
        if(call.getStatus()==CallObject.CALL_EXECUTED_OK){
            return call.getReturnArguments().booleanValue("IsAdmin", false);
        } else {
            throw new Exception("Error executing call");
        }
    }
    
    /** Notify the server that this process has started */
    private void notifyStartup(){
        try {
            CallObject call = new CallObject("SVCHostStartup");
            call.getCallArguments().add("HostIP", getServerIp());
            call.getCallArguments().add("HostGUID", guid);
            call.getCallArguments().add("UserID", PreferenceManager.getCertificateOwnerId());
            client.syncCall(call);
        } catch (Exception e){
            System.out.println("Error notifying server of host startup: " + e.getMessage());
        }
    }

    /** Get the server IP address */
    public String getServerIp() throws Exception {
        String ip;
        if(PreferenceManager.getSystemPropertyGroup("Server").booleanValue("OverrideDetectedLocalIP", false)){
            ip = PreferenceManager.getSystemPropertyGroup("Server").stringValue("LocalIP", "127.0.0.1");
        } else {
            ip = InetAddress.getLocalHost().getHostAddress();
        }
        return ip;
    }

    /** Deploy a service file from the server */
    public void deployServiceFromServer(String documentId, boolean start) throws Exception {
        // Try and get the file
        FileFetcher fetcher = new FileFetcher(new URL(client.getServerUrl()), documentId);
        File downloadedFile = fetcher.download(tempDir);
        if(downloadedFile.exists()){
            ServiceProcessContainer service = deployServiceFile(downloadedFile, start);
            if(!downloadedFile.delete()){
                downloadedFile.deleteOnExit();
            }

            // Copy the document record and document version into the deployment directory
            if(fetcher.getVersion()!=null && fetcher.getDocument()!=null){
                SerializationUtils.serialize(fetcher.getDocument(), new File(service.getServiceDirectory(), "_doc.ser"));
                SerializationUtils.serialize(fetcher.getVersion(), new File(service.getServiceDirectory(), "_ver.ser"));
            }
            
        } else {
            throw new Exception("Could not download file from server");
        }
    }
    
    /** Deploy a service file into the system */
    private ServiceProcessContainer deployServiceFile(String serviceName, File zipFile, boolean start) throws Exception {
        String sn = serviceName.trim();
        if(services.containsKey(sn)){
            deleteService(sn);
        }

        // Unzip the file into the target directory
        File serviceDir = new File(servicesDir, sn);
        File serviceTempDir = new File(tempDir, sn);
        File serviceDataDir = new File(dataDir, sn);

        if(serviceDir.exists()){
            serviceDir.deleteOnExit();
            throw new Exception("Service files already exist: Restart the server to remove");
        }
        if(serviceDir.mkdir()){
            ZipUtils.unzip(zipFile, serviceDir);

            if(!serviceTempDir.exists()){
                serviceTempDir.mkdirs();
            }

            if(!serviceDataDir.exists()){
                serviceDataDir.mkdirs();
            }

            ServiceProcessContainer service = new ServiceProcessContainer(new File(servicesDir, sn), serviceTempDir, serviceDataDir, this);

            // Look for an empty RMI port
            ArrayList<Integer> ports = new ArrayList<Integer>();
            Enumeration<ServiceProcessContainer> svcs = services.elements();
            while(svcs.hasMoreElements()){
                ports.add(new Integer(svcs.nextElement().getRmiPort()));
            }
            int port = -1;
            for(int i=1100;i<5000;i++){
                if(!ports.contains(new Integer(i))){
                    port = i;
                }
            }
            if(port!=-1){
                service.setRmiSearchStartPort(port);
            } else {
                throw new Exception("Could not find a vacant RMI port");
            }
            
            services.put(sn, service);
            notifyDeploy(sn);
            if(start){
                service.startProcess();
            }
            return service;
        } else {
            throw new Exception("Could not create service directory");
        }
    }

    /** Deploy a local service file. This method searches for a /service.xml file and extracts the
     * relevant service properties from there before deploying */
    public ServiceProcessContainer deployServiceFile(File zipFile, boolean start) throws Exception {
        String[] files = new String[]{"service.xml"};
        FileInputStream stream = new FileInputStream(zipFile);
        XMLDataExtractor extractor = new XMLDataExtractor(files);
        extractor.extractXmlData(stream);
        stream.close();
        if(extractor.allDataPresent()){
            Properties props = XmlUtils.extractChildNodes(XmlUtils.readXmlDocumentFromString(extractor.getEntry("service.xml")));
            String serviceName = props.getProperty("name");
            ServiceProcessContainer service = deployServiceFile(serviceName, zipFile, start);
            return service;
        } else {
            throw new Exception("service.xml file missing from service zip file");
        }
    }

    /** Delete a service from the system */
    public void deleteService(String serviceName) throws Exception {
        if(services.containsKey(serviceName)){
            ServiceProcessContainer service = services.get(serviceName);
            if(service.running()){
                service.stopProcess();
            }
            ZipUtils.removeDirectory(service.getServiceDirectory());
            ZipUtils.removeDirectory(service.getTemporaryDirectory());
            services.remove(serviceName);
            notifyUndeploy(serviceName);
        }
    }

    /** Delete a service data directory */
    public void deleteServiceDataDirectory(String serviceName) throws Exception {
        File serviceDataDir = new File(dataDir, serviceName);

        if(services.containsKey(serviceName)){
            ServiceProcessContainer svc = getService(serviceName);
            if(svc.running()){
                throw new Exception("Service is still running");
            } else {
                if(serviceDataDir.exists()){
                    if(!serviceDataDir.delete()){
                        serviceDataDir.deleteOnExit();
                    }
                }
            }
            
        } else {
            // Delete directory for service that has been undeployed
            if(serviceDataDir.exists()){
                if(!serviceDataDir.delete()){
                    serviceDataDir.deleteOnExit();
                }
            }
        }
    }

    /** Stop a service running without deleting it */
    public void stopService(String serviceName) throws Exception {
        if(services.containsKey(serviceName)){
            ServiceProcessContainer service = services.get(serviceName);
            if(service.running()){
                service.stopProcess();
            }
        } else {
            throw new Exception("Service: " + serviceName + " does not exist");
        }
    }

    /** Start a service running */
    public void startService(String serviceName) throws Exception {
        if(services.containsKey(serviceName)){
            ServiceProcessContainer service = services.get(serviceName);
            if(!service.running()){
                service.setDebugEnabled(false);
                service.startProcess();
            } else {
                throw new Exception("Service: " + serviceName + " is already running");
            }
        } else {
            throw new Exception("Service: " + serviceName + " does not exist");
        }
    }

    /** Start a service running in debug mode */
    public void startServiceDebug(String serviceName, int port) throws Exception {
        if(services.containsKey(serviceName)){
            ServiceProcessContainer service = services.get(serviceName);
            if(!service.running()){
                service.setDebugEnabled(true);
                service.setDebuggerPort(port);
                service.startProcess();
            } else {
                throw new Exception("Service: " + serviceName + " is already running");
            }
        } else {
            throw new Exception("Service: " + serviceName + " does not exist");
        }
    }
    
    /** Add a shell */
    public void addShell(ServiceHostShellServer shell){
        shells.add(shell);
    }
    
    /** Remove a shell */
    public void removeShell(ServiceHostShellServer shell){
        shells.remove(shell);
    }
    
    /** Get a service by name */
    public ServiceProcessContainer getService(String name){
        return services.get(name);
    }

    /** Get the RPC Client that can be used to communicate with the server */
    public RPCClient getClient(){
        return client;
    }

    /** Get the host GUID */
    public String getGuid(){
        return guid;
    }
    
    /** Main entry point. Argument is the working directory of the server */
    public static void main(String[] args){
        System.setSecurityManager(new RMISecurityManager());
        if(args.length==1){
            try {
                File dir = new File(args[0]);
                ServiceHost host = new ServiceHost(dir);
                host.start();
            } catch (Exception e){
                System.out.println("Error starting service host: " + e.getMessage());
                System.exit(1);
            }
        } else {
            System.out.println("Incorrect number of arguments for service host");
        }
    }
}