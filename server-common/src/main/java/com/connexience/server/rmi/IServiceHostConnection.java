/*
 * IServiceHostConnection.java
 */

package com.connexience.server.rmi;
import com.connexience.server.model.security.*;
import java.io.*;
import java.util.*;
import java.rmi.*;

/**
 * This interface defines the functionality of a connection to a service
 * host process. It can be used to query installed services, open connections
 * to these services and deploy / undeploy services.
 * @author hugo
 */
public interface IServiceHostConnection extends Remote {
    /** List all of the currently active services in the host */
    public List<ServiceDescriptor> listServices() throws RemoteException;

    /** Get the properties xml file for a specified service */
    public String getServicePropertiesXml(String serviceName) throws RemoteException;

    /** Send a set of properties xml for a specified service */
    public void sendServicePropertiesXml(String serviceName, String propertiesXml) throws RemoteException;

    /** Delete the properties. Next time the process is started, a set of defaults will be created */
    public void deleteProperties(String serviceName) throws RemoteException;
    
    /** Stop the service host and close all the service processes */
    public void shutdownHost() throws RemoteException;

    /** Start a service */
    public void startService(String name) throws RemoteException;

    /** Start a service in debug mode */
    public void startServiceDebug(String name, int port) throws RemoteException;
    
    /** Stop a service */
    public void stopService(String name) throws RemoteException;

    /** Undeploy a service */
    public void undeployService(String name) throws RemoteException;

    /** Deploy a service file */
    public void deployLocalServiceFile(File serviceZip, boolean start) throws RemoteException;

    /** Deploy a file directly from the server file store */
    public void deployServiceFromServer(String documentId, boolean start) throws RemoteException;
    
    /** Open an input stream on a service log file */
    public IRMIInputStream openLogFile(String serviceName) throws RemoteException;

    /** Get the document details for a deployed service */
    public ServiceDocumentDetails getServiceDocumentDetails(String serviceName) throws RemoteException;

    /** Delete the data directory for a service. This will fail if the service is running */
    public void deleteServiceDataDirectory(String serviceName) throws RemoteException;
    
    /** Open a shell connection */
    public IServiceHostShell openShell(Ticket user) throws RemoteException;
}