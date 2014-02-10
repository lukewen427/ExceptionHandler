/*
 * IDocument.java
 */

package com.connexience.server.api;

/**
 * This interface represents a document record within the system. It does
 * not provide the actual document data as this is done using the IDocumentVersion
 * interface.
 * @author hugo
 */
public interface IDocument extends ISecuredObject {
    /** XML Name for object */
    public static final String XML_NAME = "Document";
    
    /** Set the size of the latest version */
    public void setCurrentVersionSize(long size);
    
    /** Get the size of the latest version */
    public long getCurrentVersionSize();
}