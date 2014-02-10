/*
 * IControllableService.java
 */

package com.connexience.server.rmi;

import java.rmi.*;
/**
 * This is the standard interface used to access a server application that
 * can be controlled and managed.
 * @author hugo
 */
public interface IControllableService extends Remote {
    /** Open a control connection */
    public IControllableServiceConnection openConnection() throws RemoteException;
}