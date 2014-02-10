/*
 * IPropertyList.java
 */

package com.connexience.server.api;

import java.util.*;

/**
 * This interface defines a list of properties that can be stored on the server
 * @author nhgh
 */
public interface IPropertyList extends IObjectList {
    /** XML Document name */
    public static final String XML_NAME = "PropertyList";

    /** Get the ID of the object that this property list refers to */
    public String getObjectId();

    /** Set the ID of the object that this property list refers to */
    public void setObjectId(String objectId);

    /** Get the number of properties */
    public int size();

    /** Get a specific property by name */
    public IPropertyItem get(String name);

    /** Get all properties with a name */
    public List<IPropertyItem> getAll(String name);
    
    /** Get a property by index */
    public IPropertyItem get(int index);

    /** Set a property value */
    public void set(String name, String value);
    
    /** Set an integer property value */
    public void set(String name, int value);
    
    /** Set a boolean property value */
    public void set(String name, boolean value);
    
    /** Set a date value */
    public void set(String name, Date value);
    
    /** Set a double value */
    public void set(String name, double value);
    
    /** Get a string value */
    public String stringValue(String name, String defaultValue);
    
    /** Get a boolean value */
    public boolean booleanValue(String name, boolean defaultValue);
    
    /** Get a double value */
    public double doubleValue(String name, double defaultValue);
    
    /** Get an integer value */
    public int intValue(String name, int defaultValue);
    
    /** Get a date value */
    public Date dateValue(String name, Date defaultValue);
    
    /** Clear the properties */
    public void clear();
    
    /** Remove a property */
    public void remove(String name);

    /** Remove a property */
    public void remove(IPropertyItem property);

    /** Add a property */
    public void add(IPropertyItem property);

    /** Get an Iterator of properties */
    public Iterator<IPropertyItem> properties();

    /** Does a property exist */
    public boolean propertyExists(String name);
}