/*
 * IDocumentVersion.java
 */

package com.connexience.server.api;

/**
 * This interface represents a single immutable version of a specific document
 * @author hugo
 */
public interface IDocumentVersion extends IObject {
    /** XML Name for object */
    public static final String XML_NAME = "DocumentVersion";

    /** Database ID */
    public String getId();

    /** Set the ID */
    public void setId(String id);

    /** Version number */
    public int getVersionNumber();

    /** Set the version number */
    public void setVersionNumber(int versionNumber);

    /** Document ID */
    public String getDocumentId();

    /** Set the Document ID */
    public void setDocumentId(String documentId);

    /** User ID */
    public String getUserId();

    /** Set the user ID */
    public void setUserId(String userId);

    /** Timestamp */
    public String getModificationTime();

    /** Set the Timestamp */
    public void setModificationTime(String modificationTime);
    
    /** Get the file size */
    public long getSize();
    
    /** Set the file size */
    public void setSize(long size);
    
    /** Get the comments */
    public String getComments();
    
    /** Set the comments */
    public void setComments(String comments);
}