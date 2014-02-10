/*
 * IMetaDataItem.java
 */

package com.connexience.server.api;

/**
 * This interface defines the functionality of a single name value pair
 * piece of meta-data.
 * @author hugo
 */
public interface IMetaDataItem extends IObject {
    /** Set the object ID */
    public void setObjectId(String objectId);

    /** Get the object ID */
    public String getObjectId();

    /** Set the name of the metadata item */
    public void setName(String name);

    /** Get the name of the metadata item */
    public String getName();

    /** Set the value of the metadata item */
    public void setValue(String value);

    /** Get the value of the metadata item */
    public String getValue();
}