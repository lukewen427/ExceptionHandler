/*
 * ContentItem.java
 */

package com.connexience.server.model.social.content;

import java.io.*;
import java.util.*;

/**
 * THis class represents a single item of content in the database.
 * @author hugo
 */
public class ContentItem implements Serializable {
    /** Label for this item */
    private String label;
    
    /** Source ID for this item */
    private String sourceId;
    
    /** List of attributes for this item. These are stored as a Hashtable
     * object and contain all of the data related to this item */
    private Hashtable attributes;

    /** Get the display label for this content item */
    public String getLabel() {
        return label;
    }

    /** Set the display label for this content item */
    public void setLabel(String label) {
        this.label = label;
    }

    /** Set the ID of the content source that provided this item */
    public String getSourceId() {
        return sourceId;
    }

    /** Get the ID of the content source that provided this item */
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    /** Get the attributes that represent the data contained in this content item */
    public Hashtable getAttributes() {
        return attributes;
    }

    /** Set the attributes that represent the data contained in this content item */
    public void setAttributes(Hashtable attributes) {
        this.attributes = attributes;
    }
    
    /** Set an attribute */
    public void setAttribute(String name, Object value){
        attributes.put(name, value);
    }
}
