/*
 * DataStore.java
 */

package com.connexience.server.model.storage;

import com.connexience.server.model.*;
import com.connexience.server.model.document.*;
import com.connexience.server.*;

import java.io.*;
import java.util.*;

/**
 * This is the base class for an object that can act as a data store
 * @author nhgh
 */
public abstract class DataStore extends ServerObject implements Serializable {
    /** Server host */
    private String server = "localhost";
    
    /** Server port for the storage server */
    private int port = 8080;
    
    /** Does this store support complete document history deletion */
    protected boolean bulkDeleteSupported = false;
    
    /** Is the size of this store limited */
    protected boolean sizeLimited = false;
    
    /** Can the store return the storage statistics */
    protected boolean spaceReportingSupported = false;
    
    /** Does the store support direct access */
    protected boolean directAccessSupported = false;
    
    /** Creates a new instance of DataStore */
    public DataStore() {
    }

    public boolean isBulkDeleteSupported() {
        return bulkDeleteSupported;
    }
    
    /** Can the store report space */
    public boolean isSpaceReportingSupported() {
        return spaceReportingSupported;
    }
    
    /** Is space finite */
    public boolean isSizeLimited() {
        return sizeLimited;
    }

    /** Does this store support direct access */
    public boolean isDirectAccessSupported() {
        return directAccessSupported;
    }
    
    /** Get an InputStream that can be used to read the contents of the document */
    public abstract InputStream getInputStream(DocumentRecord document, DocumentVersion version) throws ConnexienceException;
    
    /** Read a record from a File */
    public abstract DocumentVersion readFromFile(DocumentRecord document, DocumentVersion record, File file) throws ConnexienceException;
    
    /** Read a record from an InputStream */
    public abstract DocumentVersion readFromStream(DocumentRecord document, DocumentVersion record, InputStream stream) throws ConnexienceException;
    
    /** Write a record to an OutputStream */
    public abstract void writeToStream(DocumentRecord document, DocumentVersion record, OutputStream stream) throws ConnexienceException;
    
    /** Remove a record */
    public abstract void removeRecord(DocumentRecord document, DocumentVersion record) throws ConnexienceException;    
    
    /** Remove all versions of a document */
    public abstract void bulkDelete(String organisationId, ArrayList<String> documentIds) throws ConnexienceException;

    /** Get the total size of the store if supported */
    public long getTotalStoreSize() throws ConnexienceException {
        return -1;
    }
    
    /** Get the available storage */
    public long getAvailableStoreSize() throws ConnexienceException {
        return -1;
    }
}
