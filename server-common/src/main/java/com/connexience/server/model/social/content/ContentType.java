/*
 * ContentType.java
 */

package com.connexience.server.model.social.content;

import java.io.*;

/**
 * This class represents a type of content that can be stored in the system. It
 * contains a MIME type, icon and description. 
 * @author hugo
 */
public class ContentType implements Serializable {
    /** Database id of this content type */
    private String id;
    
    /** Label for this content type */
    private String label;
    
    /** Mimetype text describing this content */
    private String mimeType;
    
    /** Icon to display on the web pages for this content type */
    private String iconName;

    /** Organisation ID for this content type */
    private String organisationId;
    
    /** Get the database ID */
    public String getId() {
        return id;
    }

    /** Set the database ID */
    public void setId(String id) {
        this.id = id;
    }

    /** Get the ID of the organisation containing this content type */
    public String getOrganisationId(){
        return organisationId;
    }
    
    /** Set the ID of the organisation containing this content type */
    public void setOrganisationId(String organisationId){
        this.organisationId = organisationId;
    }
    
    /** Get the display label */
    public String getLabel() {
        return label;
    }

    /** Set the display label */
    public void setLabel(String label) {
        this.label = label;
    }

    /** Get the data type contained represented by this content type */
    public String getMimeType() {
        return mimeType;
    }

    /** Set the data type contained represented by this content type */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /** Get the icon displayed on the web pages for this content type */
    public String getIconName() {
        return iconName;
    }

    /** Set the icon displayed on the web pages for this content type */
    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
}