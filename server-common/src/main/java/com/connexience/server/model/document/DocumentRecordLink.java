/*
 * DocumentRecordLink.java
 */

package com.connexience.server.model.document;

/**
 * This class provides a link to a document record. It contains the ID
 * of the document that it points to, and the StorageUtils class knows
 * to follow this link when downloading / uploading data.
 * @author hugo
 */
public class DocumentRecordLink extends DocumentRecord {
    /** ID of the document that this link points to */
    private String sinkObjectId;

    public DocumentRecordLink() {
        super();
    }

    public DocumentRecordLink(DocumentRecord sinkDocument) {
        this.setName(sinkDocument.getName());
        this.setSinkObjectId(sinkDocument.getId());
        this.setDocumentTypeId(sinkDocument.getDocumentTypeId());
        this.setDescription(sinkDocument.getDescription());
        this.setOrganisationId(sinkDocument.getOrganisationId());
        this.setVersioned(sinkDocument.isVersioned());
        this.setLimitVersions(sinkDocument.isLimitVersions());
        this.setMaxVersions(sinkDocument.getMaxVersions());
        this.setCurrentVersionNumber(sinkDocument.getCurrentVersionNumber());
    }


    public String getSinkObjectId() {
        return sinkObjectId;
    }

    public void setSinkObjectId(String sinkObjectId) {
        this.sinkObjectId = sinkObjectId;
    }
}