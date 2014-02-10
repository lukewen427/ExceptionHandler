/*
 * ExternalObjectWrapper.java
 */

package com.connexience.server.workflow.xmlstorage;

import org.pipeline.core.xmlstorage.*;
import java.io.*;

/**
 * This class provides a wrapper so that an external object can be specified
 * as a workflow property.
 * @author hugo
 */
public class ExternalObjectWrapper implements XmlStorable, Serializable {
    /** Document ID */
    private String id;
    
    /** Document Name */
    private String name;
    
    /** DocumentDescription */
    private String description;

    /** Type description string */
    private String typeString;

    /** ID of the creator application */
    private String applicationId;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }



    @Override
    public String toString() {
        return name;
    }


    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("ExternalObjectWrapper");
        store.add("ID", id);
        store.add("Name", name);
        store.add("Description", description);
        store.add("TypeString", typeString);
        store.add("ApplicationID", applicationId);
        return store;
    }

    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        id = store.stringValue("ID", "");
        name = store.stringValue("Name", "");
        description = store.stringValue("Description", "");
        typeString = store.stringValue("TypeString", "");
        applicationId = store.stringValue("ApplicationID", "");
    }
}