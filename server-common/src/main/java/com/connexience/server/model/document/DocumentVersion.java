/*
 * DocumentVersion.java
 */

package com.connexience.server.model.document;
import com.connexience.server.util.SignatureUtils;

import java.util.Date;
import java.io.Serializable;
import java.security.cert.X509Certificate;
import java.beans.*;

/**
 * This class represents a single version of a file in the repository. This class
 * contains the ID of the document record that it relates to, its version number,
 * the ID of the underlying data file in the storage system, the id of the User 
 * that modified the data, a timestamp and a signature stored as a byte array
 * which can be verified using the certificate of the user that saved the file.
 * @author nhgh
 */
public class DocumentVersion implements Comparable, Serializable {    
    /** Database ID of the version. This the same as the ID of the stored file */
    private String id;
    
    /** ID of the document record this file belongs to */
    private String documentRecordId;
    
    /** Version number of the file */
    private int versionNumber;
    
    /** ID of the User that created the file */
    private String userId;
    
    /** Timestamp for the file */
    private long timestamp;
    
    /** Size of data in bytes */
    private long size = 0;

    /** Comments entered during upload */
    private String comments;
    
    /** Creates a new instance of DocumentVersion */
    public DocumentVersion() {
    }
    
    /** Set the upload comments for this version */
    public void setComments(String comments){
        this.comments = comments;
    }
    
    /** Set the upload comments for this version */
    public String getComments(){
        return comments;
    }
    
    /** Get the size in bytes */
    public long getSize(){
        return size;
    }
    
    /** Set the size in bytes */
    public void setSize(long size){
        this.size = size;
    }
    
    /** Get the database id of this version */
    public String getId() {
        return id;
    }

    /** Set the database id of this version */
    public void setId(String id) {
        this.id = id;
    }

    /** Get the id of the associated DocumentRecord */
    public String getDocumentRecordId() {
        return documentRecordId;
    }

    /** Set the id of the associated DocumentRecord */
    public void setDocumentRecordId(String documentRecordId) {
        this.documentRecordId = documentRecordId;
    }

    /** Get the version number of this document */
    public int getVersionNumber() {
        return versionNumber;
    }

    /** Set the version number of this document */
    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    /** Get the ID of the user that created this version */
    public String getUserId() {
        return userId;
    }

    /** Set the ID of the user that created this version */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /** Get the modification timestamp of the data */
    public Date getTimestampDate() {
        return new java.util.Date(timestamp);
    }

    /** Set the modification timestamp of the data */
    public void setTimestampDate(Date timestamp) {
        this.timestamp = timestamp.getTime();
    }

    /** Get the timestamp as a Long */
    public long getTimestamp(){
        return timestamp;
    }
    
    /** Set the timestamp as a Long */
    public void setTimestamp(long timestamp){
        this.timestamp = timestamp;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     */
    public int compareTo(Object o) {
        if(o instanceof DocumentVersion){
            int version = ((DocumentVersion)o).getVersionNumber();
            if(version>getVersionNumber()){
                return -1;
            } else if(version<getVersionNumber()){
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
}