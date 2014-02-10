/*
 * ServiceRemote.java
 */

package com.connexience.server.ejb.service;
import com.connexience.server.ConnexienceException;
import com.connexience.server.model.security.*;
import com.connexience.server.model.service.*;
import com.connexience.server.rmi.*;

import java.util.*;
import javax.ejb.Remote;

/**
 * This interface defines the behavior of the service management bean that
 * controls running services on various service hosts. These services run
 * the data service, workflow enactment service etc.
 * @author hugo
 */
@Remote
public interface ServiceRemote {
    /** Notify startup of a host */
    public void notifyHostStartup(Ticket ticket, String hostId, String hostIp) throws ConnexienceException;

    /** Notify shutdown of a host */
    public void notifyHostShutdown(Ticket ticket, String hostId, String hostIp) throws ConnexienceException;

    /** Get a host record by IP address */
    public ServiceHostMachine getHostById(Ticket ticket, String hostId) throws ConnexienceException;
    
    /** Notify startup of a service */
    public void notifyServiceStartup(Ticket ticket, String hostId, String hostIp, String serviceName, int rmiPort) throws ConnexienceException;

    /** Notify shutdown of a service */
    public void notifyServiceShutdown(Ticket ticket, String hostId, String hostIp, String serviceName) throws ConnexienceException;

    /** Notify the deployment of a service */
    public void notifyServiceDeployment(Ticket ticket, String hostId, String hostIp, String serviceName) throws ConnexienceException;

    /** Notify the undeployment of a service */
    public void notifyServiceUndeployment(Ticket ticket, String hostId, String hostIp, String serviceName) throws ConnexienceException;

    /** List all of the running instances of a service */
    public List listServiceInstances(Ticket ticket, String serviceName) throws ConnexienceException;
    
    /** Search for a running instance of a service */
    public ServiceInstance findRunningServiceInstance(String serviceName) throws ConnexienceException;

    /** Retrieve a service instance running on a specific machine */
    public ServiceInstance findRunningServiceInstanceOnHostId(String serviceName, String hostId) throws ConnexienceException;

    /** List all of the detected service hosts */
    public List listHosts(Ticket ticket) throws ConnexienceException;

    /** List all of the deployed services on a host */
    public List listServicesOnHostId(Ticket ticket, String hostId) throws ConnexienceException;

    /** Start a service on a host */
    public void startServiceOnHostId(Ticket ticket, String hostId, String serviceName) throws ConnexienceException;

    /** Stop a service on a host */
    public void stopServiceOnHostId(Ticket ticket, String hostId, String serviceName) throws ConnexienceException;

    /** Get the IP address for a host */
    public String getHostIPAddress(Ticket ticket, String hostId) throws ConnexienceException;

    /** Undeploy a service from a host */
    public void undeployServiceFromHost(Ticket ticket, String hostId, String serviceName) throws ConnexienceException;

    /** Deploy a service from the local file system to a host */
    public void deployServiceFileToHost(Ticket ticket, String hostId, String serviceDocumentId) throws ConnexienceException;

    /** Get the xml properties file for a service */
    public String getServicePropertiesXml(Ticket ticket, String hostId, String serviceName) throws ConnexienceException;

    /** Set the XML properties file for a service */
    public void setServicePropertiesXml(Ticket ticket, String hostId, String serviceName, String propertiesXml) throws ConnexienceException;

    /** Delete the XML properties file for a service */
    public void deleteServicePropertiesXml(Ticket ticket, String hostId, String serviceName) throws ConnexienceException;
}