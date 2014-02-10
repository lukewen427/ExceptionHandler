/*
 * InkspotMetaDataItem.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;

/**
 * This class provides a basic implementation of a metadata item
 * @author hugo
 */
public class InkspotMetaDataItem extends InkspotObject implements IMetaDataItem {

    public InkspotMetaDataItem() {
        super();
        putProperty("name", "");
        putProperty("objectid", "");
        putProperty("value", "");
    }

    public String getName() {
        return getPropertyString("name");
    }

    public String getObjectId() {
        return getPropertyString("objectid");
    }

    public String getValue() {
        return getPropertyString("value");
    }

    public void setName(String name) {
        putProperty("name", name);
    }

    public void setObjectId(String objectId) {
        putProperty("objectid", objectId);
    }

    public void setValue(String value) {
        putProperty("value", value);
    }
}