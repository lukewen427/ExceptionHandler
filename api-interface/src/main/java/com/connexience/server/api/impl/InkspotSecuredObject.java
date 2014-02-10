/*
 * InkspotSecuredObject.java
 */

package com.connexience.server.api.impl;
import com.connexience.server.api.*;
/**
 * This class provides a default implementation of a secured object
 * @author hugo
 */
public class InkspotSecuredObject extends InkspotObject implements ISecuredObject {

    public InkspotSecuredObject() {
        putProperty("id", "");
        putProperty("name", "");
        putProperty("description", "");
        putProperty("containerid", "");
        putProperty("ownerid", "");
    }

    /** Get the ID of the containing object */
    public String getContainerId() {
        return getPropertyString("containerid");
    }

    /** Set the id of the containing object */
    public void setContainerId(String containerId) {
        putProperty("containerid", containerId);
    }

    /**
     * @return the id
     */
    public String getId() {
        return getPropertyString("id");
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        putProperty("id", id);
    }

    /**
     * @return the name
     */
    public String getName() {
        return getPropertyString("name");
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        putProperty("name", name);
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return getPropertyString("description");
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        putProperty("description", description);
    }

    /** Get the owner for this object */
    public String getOwnerId() {
        return getPropertyString("ownerid");
    }

    /** Set the owner for this object */
    public void setOwnerId(String ownerId) {
        putProperty("ownerid", ownerId);
    }


}
