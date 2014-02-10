/*
 * IServiceHost.java
 */

package com.connexience.server.rmi;
import java.rmi.*;

/**
 * This interface defines the functionality of a high level service host
 * that can manage multiple services such as the data-service, cloud-workflow-engine
 * etc.
 * @author hugo
 */
public interface IServiceHost extends Remote {
    /** Open a control connection */
    public IServiceHostConnection openConnection(String username, String password) throws RemoteException;
}