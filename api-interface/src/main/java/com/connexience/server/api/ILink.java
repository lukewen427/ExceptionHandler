/*
 * ILink.java
 */

package com.connexience.server.api;

/**
 * This class represents a link between two objects in the database.
 * @author hugo
 */
public interface ILink extends ISecuredObject {
    /** XML Name for this object */
    public static final String XML_NAME = "Link";

    /** Get the source object id */
    public String getSourceObjectId();

    /** Set the source object id */
    public void setSourceObjectId(String sourceObjectId);

    /** Get the target object id */
    public String getTargetObjectId();

    /** Set the target object id */
    public void setTargetObjectId(String targetObjectId);
}
