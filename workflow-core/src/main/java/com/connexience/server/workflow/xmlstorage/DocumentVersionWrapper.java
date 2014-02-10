/*
 * DocumentVersionWrapper.java
 */

package com.connexience.server.workflow.xmlstorage;
import com.connexience.server.api.*;
import com.connexience.server.api.impl.*;
import org.pipeline.core.xmlstorage.*;

import java.io.*;
import java.util.*;
import java.text.*;

/**
 * This class wraps up details of a document version.
 * @author nhgh
 */
public class DocumentVersionWrapper extends InkspotSecuredObject implements Serializable, XmlStorable, IDocumentVersion {
    /** Document ID */
    private String id;

    /** Version number */
    private int versionNumber = 0;

    /** ID of the user that saved this version */
    private String userId;

    /** Comments */
    private String comments;

    /** Timestamp */
    private Date timestamp = new Date();

    /** Date formatter */
    private static DateFormat dateFormatter = DateFormat.getDateTimeInstance();

    /** ID of the document that this version refers to */
    private String documentId;

    /** Size of the document */
    private long size = 0;
    
    public DocumentVersionWrapper() {
    }

    public DocumentVersionWrapper(IDocumentVersion wrapper){
        copyData(wrapper);
    }

    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("DocumentVersionWrapper");
        store.add("ID", id);
        store.add("VersionNumber", versionNumber);
        store.add("UserID", userId);
        store.add("Comments", comments);
        store.add("Timestamp", timestamp);
        store.add("DocumentID", documentId);
        store.add("Size", size);
        return store;

    }

    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        id = store.stringValue("ID", null);
        versionNumber = store.intValue("VersionNumber", 0);
        userId = store.stringValue("UserID", null);
        comments = store.stringValue("Comments", null);
        timestamp = store.dateValue("Timestamp", new Date());
        documentId = store.stringValue("DocumentID", null);
        size = store.longValue("Size", 0);
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getModificationTime() {
        return dateFormatter.format(timestamp);
    }

    public void setModificationTime(String modificationTime) {
        
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String toString(){
        return versionNumber + ": " + DateFormat.getDateTimeInstance().format(timestamp);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DocumentVersionWrapper){
            if(((DocumentVersionWrapper)obj).getId().equals(id)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /** Copy data from an IDocumentVersionWrapper */
    public final void copyData(IDocumentVersion wrapper){
        setDocumentId(wrapper.getDocumentId());
        setId(wrapper.getId());
        setModificationTime(wrapper.getModificationTime());
        setUserId(wrapper.getUserId());
        setVersionNumber(wrapper.getVersionNumber());
        setSize(wrapper.getSize());
    }
}