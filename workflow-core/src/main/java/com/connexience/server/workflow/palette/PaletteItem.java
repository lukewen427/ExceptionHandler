/*
 * PaletteItem.java
 */

package com.connexience.server.workflow.palette;

import com.connexience.server.workflow.service.*;

import org.pipeline.core.xmlstorage.*;
import java.awt.datatransfer.*;
import java.io.*;
/**
 * This class represents a single item within the palette
 * @author hugo
 */
public class PaletteItem implements XmlStorable, Transferable, Serializable {
    /** ID of the service that this palette item refers to  */
    private String serviceId;
    
    /** Title of this item */
    private String title;
    
    /** Desired palette category for this item */
    private String category;

    /** Service description */
    private String description;
    
    
    public PaletteItem() {
    }

    public PaletteItem(String title, String category, String serviceId) {
        this.serviceId = serviceId;
        this.title = title;
        this.category = category;
    }

    /** Set the service description */
    public void setDescription(String description){
        this.description = description;
    }

    /** Get the service description */
    public String getDescription(){
        return description;
    }
    
    /** Override the toString method */
    @Override
    public String toString(){
        return title;
    }


    /** Get the title that will be displayed in the palette */
    public String getTitle() {
        return title;
    }

    /** Set the title that will be displayed in the palette */
    public void setTitle(String title) {
        this.title = title;
    }

    /** Get the ID of the service that this palette item refers to */
    public String getServiceId(){
        return serviceId;
    }

    /** Set the ID of the service that this palette item refers to */
    public void setServiceId(String serviceId){
        this.serviceId = serviceId;
    }
    
    /** Get the desired category for this item */
    public String getCategory(){
        return category;
    }
    
    /** Set the desired category for this item */
    public void setCategory(String category){
        this.category = category;
    }

    /** Save this item to storage */
    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("PaletteItem");
        store.add("Category", category);
        store.add("ServiceID", serviceId);
        store.add("Title", title);
        return store;
    }

    /** Recreate this item from storage */
    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        category = store.stringValue("Category", "");
        title = store.stringValue("Title", "");
        serviceId = store.stringValue("ServiceID", "");
    }

    /** Get the transfer data from this object */
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return this;
    }

    /** Get the transfer data flavors */
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor f = new DataFlavor(PaletteItem.class, "Palette Item");
        f.setHumanPresentableName("Workflow Palette item");
        return new DataFlavor[]{f};
    }

    /** Is a data flavor supported */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        if(flavor.getMimeType().equals(DataFlavor.javaSerializedObjectMimeType) && flavor.getDefaultRepresentationClass().equals(getClass())){
            return true;
        } else {
            return false;
        }
    }
}
