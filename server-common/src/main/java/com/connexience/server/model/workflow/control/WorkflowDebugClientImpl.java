/*
 * WorkflowDebugClient.java
 */
package com.connexience.server.model.workflow.control;

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.rmi.server.*;

/**
 * This class provides a debugger that connects to a workflow engine. This class is
 * designed to execute on the same machine as the workflow engine and only connects
 * to localhost. Remote sessions are connected using RMI.
 * @author hugo
 */
public class WorkflowDebugClientImpl extends UnicastRemoteObject implements IWorkflowDebugClient, Runnable {
    /** Last set of respose data */
    private ByteArrayOutputStream responseBuffer = new ByteArrayOutputStream(4096);
    
    /** Connection to the remote output writer stream */
    private PrintWriter writer = null;
    
    /** Workflow invocation that this client will connect to */
    private WorkflowInvocationRecord invocationRecord;
    
    /** Communication socket */
    private Socket commSocket = null;
    
    /** Socket input stream */
    private InputStream inStream = null;
    
    /** Thread to read input data */
    private Thread readerThread = null;
    
    /** Thread to remove idle connections */
    private TimeoutThread timeoutThread = null;
    
    /** Is this debugger connected */
    private volatile boolean connected = false;
    
    /** Last access time in milliseconds */
    private volatile long lastAccessTime = System.currentTimeMillis();
       
    /** Timeout in milliseconds */
    private int timeout = 120000;
        
    public WorkflowDebugClientImpl(WorkflowInvocationRecord invocationRecord) throws RemoteException {
        this.invocationRecord = invocationRecord;
        try {
            connect();
        } catch (Exception e){
            throw new RemoteException("Error connecting debugger: " + e.getMessage(), e);
        }
    }

    /** Connect this debugger to the workflow block */
    public void connect() throws IOException {
        commSocket = new Socket("localhost", invocationRecord.getCurrentBlockDebugPort());
        writer = new PrintWriter(commSocket.getOutputStream());
        inStream = commSocket.getInputStream();
        readerThread = new Thread(this);
        readerThread.setDaemon(true);
        readerThread.start();
        connected = true;
        timeoutThread = new TimeoutThread();
        timeoutThread.start();     
    }

    @Override
    public boolean isConnected() throws RemoteException {
        return connected;
    }
    
    @Override
    public void close() throws RemoteException {
        if(writer!=null){
            writer.flush();
            writer.close();
            writer = null;
        }
        
        if(inStream!=null){
            try {
                inStream.close();
            } catch (Exception e){}
        }
        
        try {
            commSocket.close();
        } catch (Exception e){
            
        }
        connected = false;
    }
    
    @Override
    public void sendCommand(String command) throws RemoteException {
        if(writer!=null){
            lastAccessTime = System.currentTimeMillis();
            writer.println(command);
            writer.flush();
        }
    }

    @Override
    public byte[] getLastResponseBuffer() throws RemoteException {
        lastAccessTime = System.currentTimeMillis();
        byte[] data = responseBuffer.toByteArray();
        responseBuffer.reset();
        return data;
    }

    @Override
    public boolean debugPortAvailable() throws RemoteException {
        if(invocationRecord.getCurrentBlockDebugPort()!=-1){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getContextId() throws RemoteException {
        return invocationRecord.getContextId();
    }

    @Override
    public void run() {
        byte[] buffer = new byte[4096];
        int len;
        try {
            while((len=inStream.read(buffer))!=-1){
                responseBuffer.write(buffer, 0, len);
            }
        } catch (IOException e){
            try {
                close();
            } catch (Exception ex){}
        }
    }
    
    /** Thread to close the debugger after a period of inactivity */
    private class TimeoutThread extends Thread {
        
        /** Flag to stop */
        private volatile boolean stopFlag = false;
        
        public TimeoutThread() {
            setDaemon(true);
        }

        @Override
        public void run() {
            while(!stopFlag){
                if(connected && System.currentTimeMillis() > (lastAccessTime + timeout)){
                    stopFlag = true;
                    try {
                        close();
                    } catch (Exception e){
                        
                    }
                }
                
                try {
                    Thread.sleep(500);
                } catch (Exception e){}
            }
        }
    }
}