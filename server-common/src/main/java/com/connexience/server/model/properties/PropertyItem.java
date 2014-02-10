/*
 * PropertyItem.java
 */

package com.connexience.server.model.properties;

import java.io.*;

/**
 * This class represents a single string property item that can be associated
 * with a property group
 * @author nhgh
 */
public class PropertyItem implements Serializable {
    /** ID of this property item */
    private long id;
    
    /** ID of the parent group */
    private long groupId;
    
    /** Property name */
    private String name;
    
    /** Property string value */
    private String value;

    /** Property type label */
    private String type = "String";
    
    public PropertyItem() {
    }

    /** Copy an existing property item */
    public PropertyItem(PropertyItem item) {
        id = item.getId();
        groupId = item.getGroupId();
        name = item.getName();
        value = item.getValue();
        type = item.getType();
    }

    public PropertyItem copyWithoutId(){
        PropertyItem i = new PropertyItem();
        i.setGroupId(groupId);
        i.setName(name);
        i.setValue(value);
        i.setType(type);
        return i;
    }
    
    /** Get the ID of the group containing this property */
    public long getGroupId() {
        return groupId;
    }

    /** Set the ID of the group containing this property */
    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    /** Get the ID of this property */
    public long getId() {
        return id;
    }

    /** Set the ID of this property */
    public void setId(long id) {
        this.id = id;
    }

    /** Get the name of this property */
    public String getName() {
        return name;
    }

    /** Set the name of this property */
    public void setName(String name) {
        this.name = name;
    }

    /** Get the string value of this property */
    public String getValue() {
        return value;
    }

    /** Set the string value of this property */
    public void setValue(String value) {
        this.value = value;
    }
    
    /** Display the value as the toString */
    public String toString(){
        return name + ": " + value;
    }

    /** Get the property type label */
    public String getType() {
        return type;
    }

    /** Set the property type label */
    public void setType(String type) {
        this.type = type;
    }
}