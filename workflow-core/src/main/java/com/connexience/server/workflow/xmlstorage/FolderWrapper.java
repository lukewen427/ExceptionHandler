/*
 * FolderWrapper.java
 */

package com.connexience.server.workflow.xmlstorage;

import com.connexience.server.api.*;
import com.connexience.server.api.impl.*;
import org.pipeline.core.xmlstorage.*;

import java.io.*;
import java.util.*;

/**
 * This class provides a wrapper for a Folder in the connexience database
 * @author nhgh
 */
public class FolderWrapper extends InkspotSecuredObject implements XmlStorable, Serializable, IFolder {
    /** Document ID */
    private String id;
    
    /** Document Name */
    private String name;
    
    /** DocumentDescription */
    private String description;
    
    /** Parent ID */
    private String containerId;
    
    /** OrganisationID */
    private String organisationId;
    
    /** Child Folders */
    private Vector childFolders = new Vector();
    
    /** Child Documents */
    private Vector<DocumentRecordWrapper> childDocuments = new Vector<DocumentRecordWrapper>();
    
    /** Have the child folders been populated */
    private boolean folderDataPresent = false;
    
    /** Create an empty wrapper */
    public FolderWrapper(){
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
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

    public String getOwnerId() {
        return this.getOwnerId();
    }

    public void setOwnerId(String ownerId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public String getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }

    @Override
    public String toString() {
        return name;
    }
    
    /** Get the number of child folders */
    public int getChildCount(){
        return childFolders.size();
    }
    
    /** Get a child folder */
    public FolderWrapper getChild(int index){
        return (FolderWrapper)childFolders.get(index);
    }
        
    /** Set the child folders */
    public void setChildFolders(Vector childFolders){
        this.childFolders = childFolders;
    }
    
    /** Return the index of a child folder */
    public int getChildIndex(FolderWrapper child){
        return childFolders.indexOf(child);
    }
    
    /** Are the child folders populated */
    public boolean isFolderDataPresent(){
        return folderDataPresent;
    }
    
    /** Set the status of the child folder population flag */
    public void setFolderDataPresent(boolean folderDataPresent){
        this.folderDataPresent = folderDataPresent;
    }
           
    /** Copy data from another DocumentRecordWrapper */
    public void copyData(FolderWrapper wrapper){
        id = wrapper.getId();
        name = wrapper.getName();
        description = wrapper.getDescription();
        containerId = wrapper.getContainerId();
        organisationId = wrapper.getOrganisationId();
        setOwnerId(wrapper.getOwnerId());
    }
    
    /** Copy from an IFolder object */
    public void copyData(IFolder wrapper){
        containerId = wrapper.getContainerId();
        description = wrapper.getDescription();
        id = wrapper.getId();
        name = wrapper.getName();
        setOwnerId(wrapper.getOwnerId());
    }
    
    /** Store to XML data */
    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("Folder");
        store.add("ID", id);
        store.add("Name", name);
        store.add("Description", description);
        store.add("ContainerID", containerId);
        store.add("OrganisationID", organisationId);
        return store;
    }

    /** Recreate from XML data */
    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        id = store.stringValue("ID", null);
        name = store.stringValue("Name", "");
        description = store.stringValue("Description", "");
        containerId = store.stringValue("ContainerID", null);
        organisationId = store.stringValue("OrganisationID", null);
    }
}
