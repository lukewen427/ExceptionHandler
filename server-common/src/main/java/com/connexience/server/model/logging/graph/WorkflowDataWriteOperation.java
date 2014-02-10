/*
 * WorkflowDataWriteOperation.java
 */

package com.connexience.server.model.logging.graph;

/**
 * This operation is triggered whenever a workflow service writes a piece of
 * data.
 * @author hugo
 */
public class WorkflowDataWriteOperation extends WorkflowGraphOperation{
    /** ID of the document that was read */
    String documentId ;

    /** Version ID of the document that was read */
    String versionId;

    /** UUID of the block that read the data */
    String blockUUID;

    /**
     * Name of the document being written
     */
    String documentName;

    /**
     * Version number of the document being written
     */
    String versionNumber;

    public String getBlockUUID() {
        return blockUUID;
    }

    public void setBlockUUID(String blockUUID) {
        this.blockUUID = blockUUID;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getDocumentName()
    {
      return documentName;
    }

    public void setDocumentName(String documentName)
    {
      this.documentName = documentName;
    }

    public String getVersionNumber()
    {
      return versionNumber;
    }

    public void setVersionNumber(String versionNumber)
    {
      this.versionNumber = versionNumber;
    }
}