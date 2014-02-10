/*
 * ServiceHostShellServer.java
 */
package com.connexience.server.service;

import com.connexience.server.model.security.*;
import com.connexience.server.rmi.IServiceHostShell;
import com.connexience.server.workflow.util.StreamDumper;

import java.rmi.*;
import java.rmi.server.*;
import java.io.*;
/**
 * This class provides a server for a service host shell
 * @author hugo
 */
public class ServiceHostShellServer extends UnicastRemoteObject implements IServiceHostShell, Runnable {
    /** Parent service host */
    private ServiceHost parent;
    
    /** Shell process object */
    private Process shell;
    
    /** Output stream buffer */
    private StreamDumper outStreamDumper;
    
    /** Error stream buffer */
    private StreamDumper errStreamDumper;
    
    /** Output buffer */
    private ByteArrayOutputStream outBuffer;
    
    /** Shell command to execute */
    private String shellCommand = "/bin/bash";
    
    /** Writer to send commands the the shell */
    private PrintWriter cmdWriter;
    
    /** Writer to send output back to the output buffer */
    private PrintWriter outBufferWriter;
    
    public ServiceHostShellServer(ServiceHost parent) throws RemoteException {
        super();
        this.parent = parent;
        parent.addShell(this);
        try {
            startShell();
        } catch (Exception e){
            throw new RemoteException("Error starting shell: " + e.getMessage(),e );
        }
    }

    private void startShell() throws Exception {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            if(shell==null){
                outBuffer = new ByteArrayOutputStream();
                outBufferWriter = new PrintWriter(outBuffer);                
                outBufferWriter.println("Starting shell");
                outBufferWriter.flush();
                shell = Runtime.getRuntime().exec(shellCommand);

                outStreamDumper = new StreamDumper(shell.getInputStream(), outBuffer);
                errStreamDumper = new StreamDumper(shell.getErrorStream(), outBuffer);

                cmdWriter = new PrintWriter(shell.getOutputStream());
                shell.waitFor();
                shell = null;
                parent.removeShell(this);
                cmdWriter.close();
            }
        } catch (Exception e){
            if(outBuffer!=null){
                try {
                    outBufferWriter.println("Error running shell: " + e.getMessage());
                    outBufferWriter.flush();
                } catch (Exception ex){}
            }
        }
    }
    
    @Override
    public byte[] getLastResponseBuffer() throws RemoteException {
        try {
            outBuffer.flush();
        } catch (Exception e){}
        byte[] data = outBuffer.toByteArray();
        outBuffer.reset();
        return data;
    }

    @Override
    public void sendCommand(String command) throws RemoteException {
        try {
            if(command.equals("_++QUIT")){
                close();
            } else {
                outBufferWriter.println("<p style=\"margin-bottom: 0px; color:blue;\">> " + command + "</p>");
                outBufferWriter.flush();
                cmdWriter.println(command);
                cmdWriter.flush();            
            }
        } catch (Exception e){
            System.out.println("Error sending command");
        }
    }
    
    @Override
    public void close() throws RemoteException {
        if(shell!=null){
            shell.destroy();
        }
        parent.removeShell(this);
    }

    @Override
    public boolean isRunning() throws RemoteException {
        if(shell!=null){
            return true;
        } else {
            return false;
        }
    }
}