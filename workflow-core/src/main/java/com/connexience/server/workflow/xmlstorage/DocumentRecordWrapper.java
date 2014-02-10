/*
 * DocumentRecordWrapper.java
 */

package com.connexience.server.workflow.xmlstorage;
import com.connexience.server.api.*;
import com.connexience.server.api.impl.*;

import org.pipeline.core.xmlstorage.*;
import java.io.*;
import java.awt.datatransfer.*;
/**
 * This class provides an XmlStorable wrapper for a DocumentRecord object that
 * can be placed within and XmlDataStore and edited using the properties table.
 * @author nhgh
 */
public class DocumentRecordWrapper extends InkspotSecuredObject implements XmlStorable, Serializable, Transferable, IDocument {
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

    /** Version number if there is one */
    private int versionNumber = 0;

    /** Class name of the actual object. This is used to distinguish between different object types */
    private String shortClassName;

    /** Transfer data flavor */
    private static DataFlavor FLAVOR = new DataFlavor(DocumentRecordWrapper.class, "Document");

    /** Size of the current version */
    private long currentVersionSize = 0;
    
    public DocumentRecordWrapper(){
    }

    public DocumentRecordWrapper(IDocument wrapper){
        copyData(wrapper);
    }

    public String getContainerId() {
        return containerId;
    }

    public String getShortClassName() {
        return shortClassName;
    }

    public void setShortClassName(String shortClassName){
        this.shortClassName = shortClassName;
    }

    public long getCurrentVersionSize() {
        return currentVersionSize;
    }

    public void setCurrentVersionSize(long currentVersionSize) {
        this.currentVersionSize = currentVersionSize;
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

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public int getVersionNumber() {
        return versionNumber;
    }


    /** Copy data from an IDocument object */
    public final void copyData(IDocument wrapper){
        id = wrapper.getId();
        name = wrapper.getName();
        description = wrapper.getDescription();
        containerId = wrapper.getContainerId();
        creatorId = wrapper.getOwnerId();
        currentVersionSize = wrapper.getCurrentVersionSize();
    }

    /** Copy data from another DocumentRecordWrapper */
    public final void copyData(DocumentRecordWrapper wrapper){
        id = wrapper.getId();
        name = wrapper.getName();
        description = wrapper.getDescription();
        containerId = wrapper.getContainerId();
        organisationId = wrapper.getOrganisationId();
        creatorId = wrapper.getCreatorId();
        versionId = wrapper.getVersionId();
        versionNumber = wrapper.getVersionNumber();
        shortClassName = wrapper.getShortClassName();
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
        store.add("VersionNumber", versionNumber);
        store.add("ShortClassName", shortClassName);
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
        versionNumber = store.intValue("VersionNumber", 0);
        shortClassName = store.stringValue("ShortClassName", "");
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

    /** Get the transfer data from this object */
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return this;
    }

    /** Get the transfer data flavors */
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{FLAVOR};
    }

    /** Is a data flavor supported */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.isMimeTypeEqual(FLAVOR);
    }

}