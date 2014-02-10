/*
 * ServiceProcess.java
 */

package com.connexience.server.service;
import com.connexience.server.rmi.*;
import com.connexience.server.util.RegistryUtil;
import com.connexience.server.workflow.util.ZipUtils;
import com.connexience.server.workflow.rpc.*;
import org.pipeline.core.xmlstorage.prefs.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.io.*;

/**
 * This class provides a generic service process that can be extended by
 * specific services.
 * @author hugo
 */
public abstract class ServiceProcess extends UnicastRemoteObject implements IControllableService {
    /** Class creation error exit code */
    public static final int CLASS_CREATION_ERROR = 1;

    /** Could not register the server with the RMI registry */
    public static final int RMI_REGISTRATION_ERROR = 2;

    /** Error creating properties file */
    public static final int PROPERTIES_CREATION_ERROR = 3;

    /** Internal execution error */
    public static final int INTERNAL_EXECUTION_ERROR = 4;

    /** Process RMI name */
    private String rmiName;

    /** Service name */
    private String serviceName;

    /** Local RMI Registry for this service */
    private Registry localRegistry = null;

    /** Local RMI Registry port */
    private int localRegistryPort = -1;

    /** RPC Client */
    private RPCClient client;

    /** Service code directory */
    private File codeDir;

    /** Temporary service directory */
    private File tempDir;

    /** Persistent data directory that doesn't get deleted when the service is undeployed */
    private File dataDir;

    /** GUID of parent */
    private String parentGuid;

    public IControllableServiceConnection openConnection() throws RemoteException {
        return new ServiceProcessConnection(this);
    }

    public ServiceProcess() throws RemoteException {
        super();
    }

    public void setDataDir(File dataDir) {
        this.dataDir = dataDir;
    }

    public File getDataDir() {
        return dataDir;
    }

    public void setTempDir(File tempDir) {
        this.tempDir = tempDir;
    }

    public File getTempDir() {
        return tempDir;
    }

    public void setCodeDir(File codeDir) {
        this.codeDir = codeDir;
    }

    public File getCodeDir() {
        return codeDir;
    }

    public void setServiceName(String serviceName){
        this.serviceName = serviceName;
    }

    public String getServiceName(){
        return serviceName;
    }

    public void setParentGuid(String parentGuid) {
        this.parentGuid = parentGuid;
    }

    public String getParentGuid() {
        return parentGuid;
    }

    public void setRmiName(String rmiName) {
        this.rmiName = rmiName;
    }

    public String getRmiName(String rmiName){
        return rmiName;
    }
    
    /** Register this process with the RMI registry */
    public void rmiRegister() throws Exception {
        RegistryUtil.registerToRegistry(rmiName, this, false);
    }

    /** De-register the RMI entry */
    public void rmiUnRegister(){
        try {
            RegistryUtil.unregisterFromRegistry(rmiName);
        } catch (Exception e){
            System.out.println("Error unregistering control RMI server: " + e.getMessage());
        }
    }

    /** Get the local registry */
    public Registry getLocalRMIRegistry(){
        return localRegistry;
    }

    /** Get the local RMI registry port */
    public int getLocalRMIRegistryPort(){
        return localRegistryPort;
    }

    /** Create the local registry */
    private void createLocalRegistry(int port) throws Exception {
        localRegistry = LocateRegistry.createRegistry(port);
        localRegistryPort = port;
    }

    /** Set up the RPC Client */
    public void setupClient(String rpcUrl){
        client = new RPCClient(rpcUrl);
        client.setSecurityMethod(RPCClient.PRIVATE_KEY_SECURITY);
        client.setPrivateKey(PreferenceManager.getPrivateKey());
        client.setSigningUserId(PreferenceManager.getCertificateOwnerId());
    }

    /** Try and load the properties file */
    public void loadProperties(){
        File propertiesFile = new File(System.getProperty("user.home") + File.separator + ".inkspot" + File.separator + serviceName + ".xml");
        boolean loaded = PreferenceManager.loadPropertiesFromFile(propertiesFile);
        if(!loaded){
 
            createDefaultProperties();
            PreferenceManager.saveProperties();
            System.out.println("Created default service properties. Check these properties then restart the service");
            System.exit(1);

            // Try and load them agaiin
            loaded = PreferenceManager.loadPropertiesFromFile(propertiesFile);
            if(!loaded){
                System.out.println("Error creating properties file: " + propertiesFile.getPath());
                System.exit(PROPERTIES_CREATION_ERROR);
            } else {
                System.out.println("Loaded standard properties file: " + propertiesFile.getPath());
            }
        } else {
            System.out.println("Loaded standard properties file: " + propertiesFile.getPath());
        }
        
        // Load public and private keys
        if(!PreferenceManager.loadKeystoreFromHomeDir(".inkspot", "ServiceHost.keystore")) {
            System.out.println("Need to fetch the keystore from the server first");
            System.exit(1);
        }
    }

    /** Main entry point. This program needs two arguments - the first is the class
     * name of the service class to create. The second is the RMI name to use when
     * registering the service in the RMI registry. */
    public static void main(String[] args){
        if(args.length==8){
            String classname = args[0];
            String rmiName = args[1];
            int rmiPort = Integer.parseInt(args[2]);
            String rpcUrl = args[3];
            String codeDir = args[4];
            String tempDir = args[5];
            String dataDir = args[6];
            String parentGuid = args[7];

            ServiceProcess service = null;

            // Try and create the class
            try {
                Class serviceClass = Class.forName(classname);
                service = (ServiceProcess)serviceClass.newInstance();
            } catch (Exception e){
                System.out.println("Error creating service class: " + e.getMessage());
                System.exit(CLASS_CREATION_ERROR);
            }

            // Set the temporary directory
            service.setTempDir(new File(tempDir));
            System.out.println("Service temporary directory: " + tempDir);

            // Set the data directory
            service.setDataDir(new File(dataDir));
            System.out.println("Service data directory: " + dataDir);

            // Set the code directory
            service.setCodeDir(new File(codeDir));
            System.out.println("Service deployment directory: " + codeDir);

            // Set the parent GUID */
            service.setParentGuid(parentGuid);
            System.out.println("GUID for service host: " + parentGuid);
            
            // Try and create the local rmi registry
            try {
                service.createLocalRegistry(rmiPort);
                System.out.println("Created RMI registry on port: " + rmiPort);
            } catch (Exception e){
                System.out.println("Error creating local RMI registry");
            }

            // Try and register with the registry
            try {
                String name = service.getClass().getSimpleName();

                service.setServiceName(name);
                service.setRmiName(rmiName);
                service.rmiRegister();
                System.out.println("Registered service endpoint: " + rmiName);
            } catch (Exception e){
                System.exit(RMI_REGISTRATION_ERROR);
                System.out.println("Error registering service in registry: " + e.getMessage());
            }

            // Try and load the properties for the service
            service.loadProperties();

            // Setup the client
            service.setupClient(rpcUrl);
            
            // Finally start the service
            System.out.println("Starting service...");
            service.startProcess();
        }
    }

    
    // =========================================================
    // Abstract methods that subclasses must override
    // =========================================================
    
    /** A request has been made to stop the process */
    public abstract void stopServiceRequest();

    /** Create a default set of properties in the PreferenceManager */
    public abstract void createDefaultProperties();

    /** Start the process */
    public abstract void startProcess();

}
