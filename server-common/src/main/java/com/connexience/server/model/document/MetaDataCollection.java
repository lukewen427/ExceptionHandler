/*
 * MetaDataCollection.java
 */

package com.connexience.server.model.document;

import java.util.*;
import java.io.*;
/**
 * This class contains a collection of meta data information for a specific object
 * @author hugo
 */
public class MetaDataCollection implements Serializable {
    /** Hashtable of metadata objects */
    private Vector metadataList = new Vector();
    
    /** ID of object that this metadata refers to */
    private String objectId;
    
    /** Creates a new instance of MetaDataCollection */
    public MetaDataCollection() {
                
    }
    
    /** Get the Metadata Vector */
    public Vector getMetadataList(){
        return metadataList;
    }
    
    /** Set the Metadata Vector */
    public void setMetadataList(Vector metadataList){
        this.metadataList = metadataList;
    }
    
    /** Get the ID of the associated object */
    public String getObjectId(){
        return objectId;
    }
    
    /** Set the ID of the associated object */
    public void setObjectId(String objectId){
        this.objectId = objectId;
    }
    
    /** Add a list of metadata */
    public void addMetadata(List metadata){
        for(int i=0;i<metadata.size();i++){
            addMetadata((PropertyMetadata)metadata.get(i));
        }
    }
    
    /** Add a piece of metadata */
    public void addMetadata(PropertyMetadata metadata){
        metadata.setObjectId(objectId);
        metadataList.add(metadata);
    }
            
    /** Remove a piece of metadata */
    public void removeMetadata(int index){
        metadataList.remove(index);
    }
    
    /** Get the number of metadata pieces */
    public int getSize(){
        return metadataList.size();
    }
    
    /** Get a specific piece of metadata */
    public PropertyMetadata getMetadata(int index){
        return (PropertyMetadata)metadataList.get(index);
    }
    
    /** Get a copy of the meta data held in this collection without the id fields set. This
     * is then suitable for re-persiting in the database */
    public List<PropertyMetadata> getPersistableCopy() {
        ArrayList<PropertyMetadata> copyList = new ArrayList<PropertyMetadata>();
        Iterator<PropertyMetadata> i = metadataList.iterator();
        PropertyMetadata md;
        PropertyMetadata oldmd;
        
        while(i.hasNext()){
            oldmd = i.next();
            md = new PropertyMetadata();
            md.setName(oldmd.getName());
            md.setObjectId(oldmd.getObjectId());
            md.setValue(oldmd.getValue());
            copyList.add(md);
        }
        return copyList;
    }
}
