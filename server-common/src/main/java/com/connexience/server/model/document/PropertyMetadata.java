/*
 * PropertyMetadata.java
 */

package com.connexience.server.model.document;
import java.io.*;
/**
 * This class provides a piece of property information that can be attached
 * to a server object. This is represented as a name=value type property.
 * @author hugo
 */
public class PropertyMetadata implements Serializable {
    /** Database ID */
    private long id;
    
    /** Property name */
    private String name;
    
    /** Property value */
    private String value;
    
    /** Object that this metadata refers to */
    private String objectId;
    
    /**
     * Creates a new instance of PropertyMetadata
     */
    public PropertyMetadata() {
    }

    /** Construct with a name and a value */
    public PropertyMetadata(String name, String value){
        this.name = name;
        this.value = value;
    }
    
    /** Get the database id of this piece of metadata */
    public long getId() {
        return id;
    }

    /** Set the database id of this piece of metadata */
    public void setId(long id) {
        this.id = id;
    }

    /** Get the label of this piece of metadata */
    public String getName() {
        return name;
    }

    /** Set the label of this piece of metadata */
    public void setName(String name) {
        this.name = name;
    }

    /** Get the value of this piece of metadata */
    public String getValue() {
        return value;
    }

    /** Set the value of this piece of metadata */
    public void setValue(String value) {
        this.value = value;
    }

    /** Get the id of the object that this piece of metadata refers to */
    public String getObjectId() {
        return objectId;
    }

    /** Set the id of the object that this piece of metadata refers to */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}