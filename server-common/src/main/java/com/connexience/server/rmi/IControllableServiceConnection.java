/*
 * IControllableServiceConnection.java
 */

package com.connexience.server.rmi;

import java.rmi.*;

/**
 * This interface provides a single connection to a controllable service object.
 * @author hugo
 */
public interface IControllableServiceConnection extends Remote {
    /** Stop the service */
    public void stopService() throws RemoteException;
}