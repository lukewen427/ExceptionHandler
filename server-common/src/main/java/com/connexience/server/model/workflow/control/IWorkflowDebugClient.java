/*
 * IWorkflowDebugClient.java
 */
package com.connexience.server.model.workflow.control;

import java.rmi.*;

/**
 * This interface defines a client that can connect to and debug a workflow
 * invocation.
 * @author hugo
 */
public interface IWorkflowDebugClient extends Remote {
    /** Send a command line to the remote server */
    public void sendCommand(String command) throws RemoteException;
    
    /** Get the last set of response data */
    public byte[] getLastResponseBuffer() throws RemoteException;
    
    /** Does the current block of this invocation have a valid connection port */
    public boolean debugPortAvailable() throws RemoteException;
    
    /** Context ID of the connected block */
    public String getContextId() throws RemoteException;
    
    /** Close the debugger */
    public void close() throws RemoteException;
    
    /** Is this client still connected */
    public boolean isConnected() throws RemoteException;
}