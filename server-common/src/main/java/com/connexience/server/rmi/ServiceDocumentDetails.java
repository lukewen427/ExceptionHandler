/*
 * ServiceDocumentDetails.java
 */

package com.connexience.server.rmi;

import java.io.*;

/**
 * This class contains details of a deployed service if that service was
 * downloaded from the main data store.
 * @author hugo
 */
public class ServiceDocumentDetails implements Serializable {
    /** ID of the document record */
    private String documentId;

    /** Name of the document record */
    private String name;

    /** ID of the version */
    private String versionId;

    /** Version number */
    private int versionNumber;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }
    
}
