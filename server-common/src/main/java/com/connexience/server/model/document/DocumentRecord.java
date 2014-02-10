/*
 * DocumentRecord.java
 */

package com.connexience.server.model.document;
import com.connexience.server.model.*;
import java.io.*;
/**
 * This class contains a record of a document stored in the server. The actual
 * document data is retrieved from the data servlet using the id from this
 * record.
 * @author hugo
 */
public class DocumentRecord extends ServerObject implements Serializable {
    /** ID of the document type for this record */
    private String documentTypeId;
    
    /** Does this record support versioning */
    private boolean versioned = true;
    
    /** Set the maximum number of previous versions */
    private int maxVersions = 10;
    
    /** Limit the maximum number of previous versions */
    private boolean limitVersions = false;
    
    /** Current version number */
    private int currentVersionNumber = 0;

    /** Current document size **/
    private long currentVersionSize = 0;
    
    /** Creates a new instance of DocumentRecord */
    public DocumentRecord() {
        super();
    }
    
    /** Populate an object with fields from this one */
    public void populateCopy(DocumentRecord doc){
        super.populateCopy(doc);
        doc.setVersioned(true);
        doc.setLimitVersions(limitVersions);
        doc.setDocumentTypeId(documentTypeId);
    }
  
    /** Create a set of default meta data for this object */
    public MetaDataCollection createDefaultMetaData(){
        return new MetaDataCollection();
    }

    /** Get the id of the document type */
    public String getDocumentTypeId() {
        return documentTypeId;
    }

    /** Set the id of the document type */
    public void setDocumentTypeId(String documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    /** Is this document versioned in the data store */
    public boolean isVersioned() {
        return versioned;
    }

    /** Set whether this document versioned in the data store */
    public void setVersioned(boolean versioned) {
        this.versioned = versioned;
    }

    /** Get the maximum number of versions allowed in the data store */
    public int getMaxVersions() {
        return maxVersions;
    }

    /** Set the maximum number of versions allowed in the data store */
    public void setMaxVersions(int maxVersions) {
        this.maxVersions = maxVersions;
    }

    /** Is there an upper limit on the number of versions */
    public boolean isLimitVersions() {
        return limitVersions;
    }

    /** Set whether there is an upper limit on the number of versions */
    public void setLimitVersions(boolean limitVersions) {
        this.limitVersions = limitVersions;
    }

    /** Get the current version number */
    public int getCurrentVersionNumber() {
        return currentVersionNumber;
    }

    /** Set the current version number */
    public void setCurrentVersionNumber(int currentVersionNumber) {
        this.currentVersionNumber = currentVersionNumber;
    }

    /** Get the size of the current version */
    public long getCurrentVersionSize() {
        return currentVersionSize;
    }
    /** Set the size of the current version */
    public void setCurrentVersionSize(long currentVersionSize) {
        this.currentVersionSize = currentVersionSize;
    }

    /** Try and get the document extension */
    public String getExtension() {
        String filename = this.getName();
        int lastDotIdx = filename.lastIndexOf(".");
        if (lastDotIdx > 0 && lastDotIdx < filename.length() - 1) {
            return filename.substring(lastDotIdx + 1).trim();
        }
        return null;
    }

}