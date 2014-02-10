/*
 * IServiceHostShell.java
 */
package com.connexience.server.rmi;
import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * This class provides an interface to a shell on the service host
 * @author hugo
 */
public interface IServiceHostShell extends Remote {
    /** Send a command line to the remote server */
    public void sendCommand(String command) throws RemoteException;
    
    /** Get the last set of response data */
    public byte[] getLastResponseBuffer() throws RemoteException;    
    
    /** Close the debugger */
    public void close() throws RemoteException;    
    
    /** Is this shell running */
    public boolean isRunning() throws RemoteException;
}