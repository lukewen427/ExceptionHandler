/*
 * ServiceProcessConnection.java
 */

package com.connexience.server.service;

import com.connexience.server.rmi.*;
import java.rmi.*;
import java.rmi.server.*;

/**
 * This class provides a generic connection to a server process
 * @author hugo
 */
public class ServiceProcessConnection extends UnicastRemoteObject implements IControllableServiceConnection {
    /** Parent process */
    private ServiceProcess parent;

    public ServiceProcessConnection(ServiceProcess parent) throws RemoteException {
        super();
        this.parent = parent;
    }

    public void stopService() throws RemoteException {
        parent.stopServiceRequest();
    }
    
}
