/*
 * DocumentType.java
 */

package com.connexience.server.model.document;
import com.connexience.server.model.*;
import java.io.*;

/**
 * This class represents a document type that is recognised by the database. It contains
 * details of the data format and MIME type, which is used to give client applications
 * hints as to which viewer to use.
 * @author nhgh
 */
public class DocumentType extends ServerObject implements Serializable {
    /** Binary data format */
    public static final int BINARY_DATA = 0;
    
    /** XML text format */
    public static final int XML_DATA = 1;
    
    /** Plain row-by-row text file */
    public static final int PLAIN_TEXT = 2;
    
    /** Format type */
    private int formatType = BINARY_DATA;
    
    /** MIME type of the document data */
    private String mimeType = "application/octet-stream";
    
    /** File extension. This is specified without the '.'. */
    private String extension = "txt";
    
    /** Creates a new instance of DocumentType */
    public DocumentType() {
    }

    /** Create with data */
    public DocumentType(String mimeType, String extension){
        this.mimeType = mimeType;
        this.extension = extension;
        this.setName(this.extension + " document");
    }
    
    /** Get the basic data format type */
    public int getFormatType() {
        return formatType;
    }

    /** Set the basic data format type */
    public void setFormatType(int formatType) {
        this.formatType = formatType;
    }

    /** Get the mime type which is used as a viewer hint */
    public String getMimeType() {
        return mimeType;
    }

    /** Set the mime type which will be used as a viewer hint */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /** Get the file extension */
    public String getExtension() {
        return extension;
    }

    /** Set the file extension. This should be specified without the '.' */
    public void setExtension(String extension) {
        this.extension = extension;
    }
}