/*
 * ISecuredObject.java
 */

package com.connexience.server.api;

/**
 * This interface defines the behaviour of a ServerObject that has security
 * associated with it.
 * @author hugo
 */
public interface ISecuredObject extends IObject {
    /** Get the ID of the object */
    public String getId();

    /** Set the ID of the object */
    public void setId(String id);

    /** Get the object name */
    public String getName();

    /** Get the object name */
    public void setName(String name);

    /** Get the object description */
    public String getDescription();

    /** Set the object description */
    public void setDescription(String description);

    /** Get the ID of the container */
    public String getContainerId();

    /** Set the ID of the container */
    public void setContainerId(String containerId);

    /** Get the ID of the owner */
    public String getOwnerId();

    /** Set the ID of the owner */
    public void setOwnerId(String ownerId);
}
