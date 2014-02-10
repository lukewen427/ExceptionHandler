/*
 * DocumentTypeWrapper.java
 */

package com.connexience.server.workflow.xmlstorage;

import org.pipeline.core.xmlstorage.*;

/**
 * This class provides a wrapper around a data type object so that the mime
 * types of documents can be recognised by the rich client.
 * @author nhgh
 */
public class DocumentTypeWrapper implements XmlStorable {
    /** MIME type of the document data */
    private String mimeType = "text/plain";

    /** File extension. This is specified without the '.'. */
    private String extension = "txt";

    /** Database ID */
    private String id;

    /** Object name */
    private String name;

    /** Organisation description */
    private String description;

    /** Get the object unique identifier */
    public String getId() {
        return id;
    }

    /** Set the object unique idendifier */
    public void setId(String id) {
        this.id = id;
    }

    /** Get the object name */
    public String getName() {
        return name;
    }

    /** Set the object name */
    public void setName(String name) {
        this.name = name;
    }

    /** Set the description of this object */
    public void setDescription(String description){
        this.description = description;
    }

    /** Get the description of this object */
    public String getDescription(){
        return description;
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

    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("DataTypeWrapper");
        store.add("ID", id);
        store.add("Name", name);
        store.add("Description", description);
        store.add("Extension", extension);
        store.add("MimeType", mimeType);
        return store;

    }

    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        id = store.stringValue("ID", "");
        name = store.stringValue("Name", "");
        description = store.stringValue("Description", "");
        extension = store.stringValue("Extension", "txt");
        mimeType = store.stringValue("MimeType", "text/plain");
    }
}
