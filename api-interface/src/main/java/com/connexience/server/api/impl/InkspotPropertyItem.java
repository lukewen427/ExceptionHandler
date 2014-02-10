/*
 * InkspotPropertyItem.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;

/**
 * This class provides an implementation of the IPropertyItem interface.
 * @author nhgh
 */
public class InkspotPropertyItem extends InkspotObject implements IPropertyItem {

    public InkspotPropertyItem() {
        super();
        putProperty("name", "");
        putProperty("value", "");
        putProperty("type", PROPERTY_TYPE_STRING);
        putProperty("default", "");
        putProperty("description", "");
        putProperty("id", "0");
    }

    public String getName() {
        return getPropertyString("name");
    }

    public void setName(String name) {
        putProperty("name", name);
    }

    public void setValue(String value) {
        putProperty("value", value);
    }

    public String stringValue() {
        return getPropertyString("value");
    }

    /** Get the property type */
    public String getType() {
        return getPropertyString("type");
    }

    /** Set the property type */
    public void setType(String type) {
        putProperty("type", type);
    }

    /** Get the default property value */
    public String getDefaultValue() {
        return getPropertyString("default");
    }

    /** Set the default property value */
    public void setDefaultValue(String defaultValue) {
        putProperty("default", defaultValue);
    }


    /** Get the property description */
    public String getDescription() {
        return getPropertyString("description");
    }

    /** Set the property description */
    public void setDescription(String description) {
        putProperty("description", description);
    }

    public long getId() {
        return Long.parseLong(getPropertyString("id"));
    }

    public void setId(long id) {
        putProperty("id", Long.toString(id));
    }
}