/*
 * DynamicWorkflowServiceWrapper.java
 */

package com.connexience.server.workflow.xmlstorage;

import org.pipeline.core.xmlstorage.*;

/**
 * This class wraps up a dynamic workflow service object from the server.
 * @author nhgh
 */
public class DynamicWorkflowServiceWrapper implements XmlStorable {
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

    /** ID of the document creator */
    private String creatorId;

    /** ID of the version if there is one */
    private String versionId;

    /** Service category */
    private String category;

    /** Size of the current version */
    private long currentVersionSize = 0;
    
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

    public String getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }

    /** Get the creator id */
    public String getCreatorId(){
        return creatorId;
    }

    /** Set the creator id */
    public void setCreatorId(String creatorId){
        this.creatorId = creatorId;
    }

    /** Create an empty wrapper */
    public DynamicWorkflowServiceWrapper(){
    }

    @Override
    public String toString() {
        return name;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getCurrentVersionSize() {
        return currentVersionSize;
    }

    public void setCurrentVersionSize(long currentVersionSize) {
        this.currentVersionSize = currentVersionSize;
    }


    /** Copy data from another DocumentRecordWrapper */
    public void copyData(DynamicWorkflowServiceWrapper wrapper){
        id = wrapper.getId();
        name = wrapper.getName();
        description = wrapper.getDescription();
        containerId = wrapper.getContainerId();
        organisationId = wrapper.getOrganisationId();
        creatorId = wrapper.getCreatorId();
        category = wrapper.getCategory();
        currentVersionSize = wrapper.getCurrentVersionSize();
        
    }

    /** Store to XML data */
    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("DocumentRecordWrapper");
        store.add("ID", id);
        store.add("Name", name);
        store.add("Description", description);
        store.add("ContainerID", containerId);
        store.add("OrganisationID", organisationId);
        store.add("CreatorID", creatorId);
        store.add("VersionID", versionId);
        store.add("Category", category);
        store.add("CurrentVersionSize", currentVersionSize);
        return store;
    }

    /** Recreate from XML data */
    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        id = store.stringValue("ID", null);
        name = store.stringValue("Name", "");
        description = store.stringValue("Description", "");
        containerId = store.stringValue("ContainerID", null);
        organisationId = store.stringValue("OrganisationID", null);
        creatorId = store.stringValue("CreatorID", null);
        versionId = store.stringValue("VersionID", null);
        category = store.stringValue("Category", "");
        currentVersionSize = store.longValue("CurrentVersionSize", 0);
    }

    /** Print to an output stream */
    public void debugPrint(){
        System.out.println("Name: " + name);
        System.out.println("Description: " + description);
        System.out.println("ContainerID: " + containerId);
        System.out.println("ID: " + id);
        System.out.println("OrganisationID: " + organisationId);
        System.out.println("CreatorID: " + creatorId);
        System.out.println("CurrentVersionSize: " + currentVersionSize);
    }
}