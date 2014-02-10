/*
 * ServiceHostConnection.java
 */

package com.connexience.server.service;
import com.connexience.server.model.security.Ticket;
import com.connexience.server.rmi.*;
import com.connexience.server.workflow.xmlstorage.*;
import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

/**
 * This class provides a connection to a service host
 * @author hugo
 */
public class ServiceHostConnection extends UnicastRemoteObject implements IServiceHostConnection {
    /** Parent host */
    private ServiceHost parent;

    public ServiceHostConnection(ServiceHost parent) throws RemoteException {
        this.parent = parent;
    }

    public List<ServiceDescriptor> listServices() throws RemoteException {
        ArrayList<ServiceDescriptor> results = new ArrayList<ServiceDescriptor>();
        Enumeration<ServiceProcessContainer> services = parent.getServices();
        ServiceDescriptor descriptor;
        ServiceProcessContainer service;
        while(services.hasMoreElements()){
            service = services.nextElement();
            descriptor = new ServiceDescriptor();
            descriptor.setName(service.getName());
            descriptor.setRmiName(service.getRmiName());
            descriptor.setRunning(service.running());
            descriptor.setRmiRegistryPort(service.getRmiPort());
            results.add(descriptor);
        }
        return results;
    }

    public String getServicePropertiesXml(String serviceName) throws RemoteException {
        ServiceProcessContainer service = parent.getService(serviceName);
        if(service!=null){
            try {
                return service.getServicePropertiesXml();
            } catch (Exception e){
                throw new RemoteException("Error getting service xml file", e);
            }
        } else {
            throw new RemoteException("Service: " + serviceName + " does not exist");
        }
    }


    public void shutdownHost() throws RemoteException {
        try {
            parent.stop();
        } catch (Exception e){
            throw new RemoteException("Error stopping service host", e);
        }
    }

    public void startService(String name) throws RemoteException {
        try {
            parent.startService(name);
        } catch (Exception e){
            throw new RemoteException("Error starting: " + name + ": " + e.getMessage(), e);
        }
    }

    public void startServiceDebug(String name, int port) throws RemoteException {
        try {
            parent.startServiceDebug(name, port);
        } catch (Exception e){
            throw new RemoteException("Error debugging: " + name + ": " + e.getMessage(), e);
        }
    }

    public void stopService(String name) throws RemoteException {
        try {
            parent.stopService(name);
        } catch (Exception e){
            throw new RemoteException("Error stopping: " + name + ": " + e.getMessage(), e);
        }
    }


    public void undeployService(String name) throws RemoteException {
        try {
            parent.deleteService(name);
        } catch (Exception e){
            throw new RemoteException("Error undploying: " + name + ": " + e.getMessage(), e);
        }
    }


    public void deployLocalServiceFile(File serviceZip, boolean start) throws RemoteException {
        try {
            parent.deployServiceFile(serviceZip, start);
        } catch (Exception e){
            throw new RemoteException("Error deploying local file: " + e.getMessage(), e);
        }
    }

    public IRMIInputStream openLogFile(String serviceName) throws RemoteException {
        ServiceProcessContainer service = parent.getService(serviceName);
        if(service!=null){
            File logFile = service.getLogFile();
            if(logFile.exists()){
                try {
                    FileInputStream stream = new FileInputStream(logFile);
                    return new RMIInputStreamServer(stream);
                } catch (IOException ioe){
                    throw new RemoteException("IOException reading log file: " + ioe.getMessage(), ioe);
                }

            } else {
                throw new RemoteException("Log file for service: " + serviceName + " not found");
            }
        } else {
            throw new RemoteException("No such service: " + serviceName);
        }
    }

    public void deployServiceFromServer(String documentId, boolean start) throws RemoteException {
        try {
            parent.deployServiceFromServer(documentId, start);
        } catch (Exception e){
            throw new RemoteException("Error deploying server file: " + e.getMessage(), e);
        }
    }

    public ServiceDocumentDetails getServiceDocumentDetails(String serviceName) throws RemoteException {
        try {
            ServiceProcessContainer service = parent.getService(serviceName);
            if(service!=null){
                DocumentRecordWrapper doc = service.getDocument();
                DocumentVersionWrapper ver = service.getVersion();
                if(doc!=null && ver!=null){
                    ServiceDocumentDetails details = new ServiceDocumentDetails();
                    details.setDocumentId(doc.getId());
                    details.setName(doc.getName());
                    details.setVersionId(ver.getId());
                    details.setVersionNumber(ver.getVersionNumber());
                    return details;
                } else {
                    throw new Exception("Service does not contain document information");
                }
            } else {
                throw new Exception("No such service: " + serviceName);
            }
        } catch (Exception e){
            throw new RemoteException("Error getting service document details: " + e.getMessage(), e);
        }
    }


    public void deleteServiceDataDirectory(String serviceName) throws RemoteException {
        try {
            parent.deleteServiceDataDirectory(serviceName);
        } catch (Exception e){
            throw new RemoteException("Error deleting service data directory: " + e.getMessage());
        }
    }

    public void sendServicePropertiesXml(String serviceName, String propertiesXml) throws RemoteException {
        ServiceProcessContainer service = parent.getService(serviceName);
        if(service!=null){
            try {
                service.setServicePropertiesXml(propertiesXml);
            } catch (Exception e){
                throw new RemoteException("Error sending service xml file", e);
            }
        } else {
            throw new RemoteException("Service: " + serviceName + " does not exist");
        }
    }

    public void deleteProperties(String serviceName) throws RemoteException {
        ServiceProcessContainer service = parent.getService(serviceName);
        if(service!=null){
            try {
                service.deleteServicePropertiesXml();
            } catch (Exception e){
                throw new RemoteException("Error sending service xml file", e);
            }
        } else {
            throw new RemoteException("Service: " + serviceName + " does not exist");
        }
    }

    @Override
    public IServiceHostShell openShell(Ticket user) throws RemoteException {
        boolean admin = false;
        try {
            admin = parent.isAdminTicket(user);
        } catch (Exception e){
            throw new RemoteException("Error checking user credentials: " + e.getMessage(), e);
        }
        
        if(admin){
            try {
                return new ServiceHostShellServer(parent);
            } catch (RemoteException re){
                throw re;
            }
        } else {
            throw new RemoteException("User is not an administrator");
        }
    }
}