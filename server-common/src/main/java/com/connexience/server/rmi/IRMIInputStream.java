/*
 * IRMIInputStream.java
 */

package com.connexience.server.rmi;

import java.rmi.*;
/**
 * This class defines an input stream server that can be used to transfer
 * data via RMI
 * @author hugo
 */
public interface IRMIInputStream extends Remote {
    /** Is the source finished */
    public boolean finished() throws RemoteException;

    /** Fetch the next chunk of data */
    public byte[] next() throws RemoteException;

    /** Close the connection and finish with the stream */
    public void close() throws RemoteException;
}