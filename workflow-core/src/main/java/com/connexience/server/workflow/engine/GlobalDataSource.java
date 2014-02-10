/*
 * GlobalDataSource.java
 */

package com.connexience.server.workflow.engine;

import com.connexience.server.workflow.service.*;

import java.util.*;
import java.io.*;

/**
 * This class provides access to the global store of drawing execution data. It
 * is used to create individual Data sources for workflow invocations
 * @author hugo
 */
public class GlobalDataSource implements DataProcessorDataSource {
    /** Base directory for data storage */
    private String baseDirectory = "";
    
    /** List of data stores for each invocation */
    private Hashtable<String,InvocationDataSource> invocationSources = new Hashtable<String,InvocationDataSource>();
    
    /** Create with a directory */
    public GlobalDataSource(String baseDirectory){
        this.baseDirectory = baseDirectory;
    }
    
    /** Get the base directory */
    public String getBaseDirectory() {
        return baseDirectory;
    }
    
    /** Set the base directory */
    public void setBaseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }
    
    /** Create a data source for an invocation */
    public InvocationDataSource createInvocationDataSource(String invocationId){
        InvocationDataSource source = new InvocationDataSource(baseDirectory + File.separator + invocationId, invocationId, this);
        
        invocationSources.put(invocationId, source);
        return source;
    }
    
    /** Remove an invocation data source */
    public void removeInvocationDataSource(InvocationDataSource source){
        invocationSources.remove(source.getInvocationId());
    }
    
    /** Get the data source for an invociation */
    public InvocationDataSource getDataSource(String invocationId){
        return invocationSources.get(invocationId);
    }

    /** Get an InputStream for a piece of data */
    public InputStream getInputDataStream(String invocationId, String contextId, String inputName) throws DataProcessorException {
        InvocationDataSource source = getDataSource(invocationId);
        if(source!=null){
            return source.getInputStream(contextId, inputName);
        } else {
            throw new DataProcessorException("Cannot locate data for invocation: " + invocationId);
        }
    }

    /** Get an OutputStream for a piece of data */
    public OutputStream getOutputDataStream(String invocationId, String contextId, String outputName, String dataType) throws DataProcessorException {
        InvocationDataSource source = getDataSource(invocationId);
        if(source!=null){
            return source.getOutputStream(contextId, outputName);
        } else {
            throw new DataProcessorException("Cannot locate data for invocation: " + invocationId);
        }
    }

    @Override
    public long getInputDataLength(String invocationId, String contextId, String inputName) throws DataProcessorException {
        InvocationDataSource source = getDataSource(invocationId);
        return source.getInputDataLength(contextId, inputName);
    }

    @Override
    public long getOutputDataLength(String invocationId, String contextId, String outputName) throws DataProcessorException {
        InvocationDataSource source = getDataSource(invocationId);
        return source.getOutputDataLength(contextId, outputName);
    }

    @Override
    public void setOutputHash(String invocationId, String contextId, String outputName, String hashValue) throws DataProcessorException {
        InvocationDataSource source = getDataSource(invocationId);
        source.setOutputHash(invocationId, contextId, outputName, hashValue);
    }

    @Override
    public String getOutputHash(String invocationId, String contextId, String outputName) throws DataProcessorException {
        InvocationDataSource source = getDataSource(invocationId);
        return source.getOutputHash(invocationId, contextId, outputName);
    }
    
    /** This data source allows access to the filesystem */
    public boolean allowsFileSystemAccess() {
        return true;
    }

    /** Get the working directory */
    public String getStorageDirectory(String invocationId) throws DataProcessorException {
        return baseDirectory + File.separator + invocationId;
    }
}