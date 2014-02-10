/*
 * DynamicWorkflowLibraryWrapper.java
 */

package com.connexience.server.workflow.xmlstorage;

import org.pipeline.core.xmlstorage.*;

/**
 * This class wraps up a dynamic workflow service object in an XmlStorable
 * wrapper.
 * @author nhgh
 */
public class DynamicWorkflowLibraryWrapper implements XmlStorable {
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

    /** Short name of the library used for dependency resolution */
    private String libraryName;

    /** Size of the current version */
    private long currentVersionSize = 0;
    
    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("DynamicWorkflowServiceWrapper");
        store.add("ID", id);
        store.add("Name", name);
        store.add("Description", description);
        store.add("ContainerID", containerId);
        store.add("OrganisationID", organisationId);
        store.add("CreatorID", creatorId);
        store.add("VersionID", versionId);
        store.add("LibraryName", libraryName);
        store.add("CurrentVersionSize", currentVersionSize);
        return store;
    }

    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        id = store.stringValue("ID", null);
        name = store.stringValue("Name", "");
        description = store.stringValue("Description", "");
        containerId = store.stringValue("ContainerID", null);
        organisationId = store.stringValue("OrganisationID", null);
        creatorId = store.stringValue("CreatorID", null);
        versionId = store.stringValue("VersionID", null);
        libraryName = store.stringValue("LibraryName", "");
        currentVersionSize = store.longValue("CurrentVersionSize", 0);


    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
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

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
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

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public long getCurrentVersionSize() {
        return currentVersionSize;
    }

    public void setCurrentVersionSize(long currentVersionSize) {
        this.currentVersionSize = currentVersionSize;
    }


}
