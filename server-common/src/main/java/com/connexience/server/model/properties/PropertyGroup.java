/*
 * PropertyGroup.java
 */

package com.connexience.server.model.properties;

import java.io.*;
import java.util.*;

/**
 * This class contains a group of system wide properties that components of the
 * system can read. These properties can be related to server items, or can be
 * assigned to system users so that persistent properties can be accessed.
 * @author nhgh
 */
public class PropertyGroup implements Serializable {
    /** Property id */
    private long id;
    
    /** Is this property group assigned to a server object */
    private boolean objectProperty = false;
    
    /** ID of the object that owns this property if there is one */
    private String objectId;
    
    /** Name of the property group */
    private String name;
    
    /** Description of the property group */
    private String description;

    /** ID of the organisation containing this group */
    private String organisationId;
    
    /** Hashtable of properties for this group */
    private Hashtable properties = new Hashtable();
    
    /** Get the property group description */
    public String getDescription() {
        return description;
    }

    /** Get all of the properties */
    public Hashtable getProperties(){
        return properties;
    }
    
    /** Get the value of a property */
    public String getValue(String name, String defaultValue){
        if(properties.containsKey(name)){
            return ((PropertyItem)properties.get(name)).getValue();
        } else {
            return defaultValue;
        }
    }
    
    /** Set the value of a property */
    public void setValue(String name, String value){
        if(properties.containsKey(name)){
            ((PropertyItem)properties.get(name)).setValue(value);
        } else {
            PropertyItem item = new PropertyItem();
            item.setName(name);
            item.setValue(value);
            item.setGroupId(id);
            properties.put(name, item);
        }
    }
    
    /** Set value with a type string */
    public void setValue(String name, String value, String type){
        if(properties.containsKey(name)){
            ((PropertyItem)properties.get(name)).setValue(value);
            ((PropertyItem)properties.get(name)).setType(type);
        } else {
            PropertyItem item = new PropertyItem();
            item.setName(name);
            item.setType(type);
            item.setValue(value);
            item.setGroupId(id);
            properties.put(name, item);
        }        
    }
    
    /** Set all of the properties */
    public void setProperties(Hashtable properties){
        this.properties = properties;
        Enumeration e = properties.elements();
        while(e.hasMoreElements()){
            ((PropertyItem)e.nextElement()).setGroupId(id);
        }
    }

    /** Get the number of properties */
    public int getSize() {
        return properties.size();
    }

    /** Get a property by index */
    public PropertyItem getProperty(int index){
        Enumeration e = properties.elements();
        int count = 0;
        while(count < index){
            e.nextElement();
            count++;
        }
        return (PropertyItem)e.nextElement();
    }

    /** Get a property by name */
    public PropertyItem getProperty(String name){
        if(properties.containsKey(name)){
            return (PropertyItem)properties.get(name);
        } else {
            return null;
        }
    }
    
    /** Get propery names */
    public ArrayList<String> getNames(){
        ArrayList<String> names = new ArrayList<String>();
        Enumeration e = properties.elements();
        PropertyItem p;
        while(e.hasMoreElements()){
            p = (PropertyItem)e.nextElement();
            names.add(p.getName());
        }
        return names;
    }
    
    /** Remove a property */
    public void removeProperty(String name){
        properties.remove(name);
    }
    
    /** Set the property group description */
    public void setDescription(String description) {
        this.description = description;
    }

    /** Get the property database id */
    public long getId() {
        return id;
    }

    /** Set the property database id */
    public void setId(long id) {
        this.id = id;
    }

    /** Get the name of this property group */
    public String getName() {
        return name;
    }

    /** Set the name of this property group */
    public void setName(String name) {
        this.name = name;
    }

    /** Get the ID of the object that owns this property group if there is one */
    public String getObjectId() {
        return objectId;
    }

    /** Set the ID of the object that owns this property group if there is one */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /** Is this property group owned by an object */
    public boolean isObjectProperty() {
        return objectProperty;
    }

    /** Set whether this property group owned by an object */
    public void setObjectProperty(boolean objectProperty) {
        this.objectProperty = objectProperty;
    }
    
    /** Get the organisation ID of this property group */
    public String getOrganisationId(){
        return organisationId;
    }
    
    /** Set the organisation ID of this property group */
    public void setOrganisationId(String organisationId){
        this.organisationId = organisationId;
    }
    
    /** Provide the name as the toString */
    public String toString(){
        return name;
    }
}