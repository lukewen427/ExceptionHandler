/*
 * RMIInputStreamServer.java
 */

package com.connexience.server.rmi;

import java.rmi.*;
import java.rmi.server.*;
import java.io.*;

/**
 * This class provides a server for an input stream that can transfer the data
 * via RMI
 * @author hugo
 */
public class RMIInputStreamServer extends UnicastRemoteObject implements IRMIInputStream {
    /** Source stream */
    private InputStream source;

    /** Is the stream readining finished */
    private boolean finished = false;

    public RMIInputStreamServer(InputStream source) throws RemoteException {
        this.source = source;
    }

    public boolean finished() throws RemoteException {
        return finished;
    }

    public byte[] next() throws RemoteException {
        if(source!=null){
            try {
                byte[] buffer = new byte[4096];
                int len = source.read(buffer);
                if(len!=-1){
                    if(len<buffer.length){
                        // Need to trim the buffer
                        byte[] trimmedBuffer = new byte[len];
                        for(int i=0;i<len;i++){
                            trimmedBuffer[i] = buffer[i];
                        }
                        return trimmedBuffer;
                    } else {
                        // Return full buffer
                        return buffer;
                    }
                } else {
                    finished = true;
                    source.close();
                    return null;
                }
            } catch (IOException e){
                throw new RemoteException("IO Error reading stream: " + e.getMessage(), e);
            }
        } else {
            throw new RemoteException("No source stream defined in server");
        }
    }

    public void close() throws RemoteException {
        if(source!=null){
            try {
                source.close();
            } catch (Exception e){}
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if(source!=null){
            try {
                source.close();
            } catch (Exception e){}
        }
    }
}