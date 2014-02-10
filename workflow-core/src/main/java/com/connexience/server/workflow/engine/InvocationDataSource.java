/*
 * InvocationDataSource.java
 */

package com.connexience.server.workflow.engine;

import com.connexience.server.workflow.service.*;
import com.connexience.server.workflow.util.*;

import java.io.*;

/**
 * This class implements a disk based temporary data source for workflow
 * invocations
 * @author hugo
 */
public class InvocationDataSource {
    /** Storage directory */
    private File storageDir = null;

    /** Parent global storage */
    private GlobalDataSource parent;
    
    /** Invocation ID */
    private String invocationId;
    
    /** Construct with a file */
    public InvocationDataSource(String storageDirName, String invocationId, GlobalDataSource parent) {
        storageDir = new File(storageDirName);
        storageDir.mkdir();
        this.parent = parent;
        this.invocationId = invocationId;
    }
    
    /** Remove everything from the directory */
    public void emptyDirectory(){
        if(parent!=null){
            parent.removeInvocationDataSource(this);
        }

        try {
            ZipUtils.removeDirectory(storageDir);
        } catch (Exception e){
            System.out.println("Error emptying storage directory: " + e.getMessage());
        }

        /*
        File[] contents = storageDir.listFiles();
        for(int i=0;i<contents.length;i++){
            if(!contents[i].delete()){
                contents[i].deleteOnExit();
            }
        }
        if(!storageDir.delete()){
            storageDir.deleteOnExit();
        }
         */
    }
    
    /** Get the parent global data source */
    public GlobalDataSource getParent(){
        return parent;
    }
    
    /** Get the invocation Id */
    public String getInvocationId() {
        return invocationId;
    }

    /** Get an InputStream for a specific piece of data */
    public InputStream getInputStream(String contextId, String portName) throws DataProcessorException {
        // Load the transfer data from file
        File saveFile = new File(storageDir, portName + "-" + contextId + ".dat");
        if(!saveFile.exists()){
            throw new DataProcessorException("Transfer data file does not exist");
        }
        try {
            return new FileInputStream(saveFile);
        } catch (IOException ioe){
            throw new DataProcessorException("IOException getting data file: " + ioe.getMessage());
        }
    }

    /** Get the length of a specific piece of data */
    public long getInputDataLength(String contextId, String portName) throws DataProcessorException {
        File saveFile = new File(storageDir, portName + "-" + contextId + ".dat");
        if(saveFile.exists()){
            return saveFile.length();
        } else {
            throw new DataProcessorException("Transfer data file does not exist");
        }
    }
    
    public long getOutputDataLength(String contextId, String portName) throws DataProcessorException {
        File saveFile = new File(storageDir, portName + "-" + contextId + ".dat");
        if(saveFile.exists()){
            return saveFile.length();
        } else {
            throw new DataProcessorException("Transfer data file does not exist");
        }
    }

    /** Get an OutputStream to write to a specific piece of data */
    public OutputStream getOutputStream(String contextId, String portName) throws DataProcessorException {
        File saveFile = new File(storageDir, portName + "-" + contextId + ".dat");
        try {
            if(saveFile.exists()){
                saveFile.delete();
            }
            return new FileOutputStream(saveFile);
        } catch (IOException ioe){
            throw new DataProcessorException("IOException getting outputdata stream: " + ioe.getMessage());
        }
    }
    
    /** Set the MD5 for an output */
    public void setOutputHash(String invocationId, String contextId, String outputName, String hashValue) throws DataProcessorException {
        try {
            File hashFile = new File(storageDir, outputName + "-" + contextId + ".m5");
            ZipUtils.writeSingleLineFile(hashFile, hashValue);
        } catch (Exception e){
            throw new DataProcessorException("Error writing MD5 for output: " + outputName + ": " + e.getMessage(), e);
        }
    }
    
    /** Get the MD5 for an output */
    public String getOutputHash(String invocationId, String contextId, String outputName) throws DataProcessorException {
        try {
            File hashFile = new File(storageDir, outputName + "-" + contextId + ".m5");
            if(hashFile.exists()){
                return ZipUtils.readFirstLineOfFile(hashFile);
            } else {
                return  null;
            }
        } catch (Exception e){
            throw new DataProcessorException("Error writing MD5 for output: " + outputName + ": " + e.getMessage(), e);
        }
    }
    
    /** Get the storage directory */
    public File getStorageDir(){
        return storageDir;
    }
}