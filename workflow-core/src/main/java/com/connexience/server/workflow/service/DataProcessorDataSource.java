/*
 * DataProcessorDataSource.java
 */

package com.connexience.server.workflow.service;

import java.io.*;

/**
 * This interface defines a data source that supplies
 * data to a remote data processor service. There are
 * clients available that use standard GET / POST methods
 * to return data streams. WebService and Standard file based
 * clients are also available.
 * @author hugo
 */
public interface DataProcessorDataSource {
    // Type definitions for data sources
    
    /** File based data source */
    public static final String FILE_DATA_SOURCE = "file";
    
    /** Web based data source */
    public static final String WEB_DATA_SOURCE = "web";
    
    /** Get an InputStream representing a set of data for an invocation and input name */
    public InputStream getInputDataStream(String invocationId, String contextId, String inputName) throws DataProcessorException;
    
    /** Get an OutputStream that can be used to write data to */
    public OutputStream getOutputDataStream(String invocationId, String contextId, String outputName, String dataTypeName) throws DataProcessorException;

    /** Get the length in bytes of a piece of input data */
    public long getInputDataLength(String invocationId, String contextId, String inputName) throws DataProcessorException;
    
    /** Get the length in bytes of a piece of exported data */
    public long getOutputDataLength(String invocationId, String contextId, String outputName) throws DataProcessorException;
    
    /** Set the hash for an output */
    public void setOutputHash(String invocationId, String contextId, String outputName, String hashValue) throws DataProcessorException;
    
    /** Get the hash of an output */
    public String getOutputHash(String invocationId, String contextId, String outputName) throws DataProcessorException;
    
    /** Get the directory within the filesystem where the data is stored */
    public String getStorageDirectory(String invocationId) throws DataProcessorException;

    /** Does this source allow access to the filesystem */
    public boolean allowsFileSystemAccess();
}