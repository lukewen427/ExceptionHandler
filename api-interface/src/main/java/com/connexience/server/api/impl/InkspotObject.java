/*
 * InkspotObject.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;
import java.io.*;
import java.util.*;
/**
 * This is the inkspot implementation of the base object
 * @author hugo
 */
public class InkspotObject implements IObject, Serializable {
    /** Object properties */
    protected Hashtable<String,Object> properties = new Hashtable<String,Object>();

    /** Get the object properties */
    public Hashtable<String,Object> getProperties(){
        return properties;
    }

    /** Put a value into the properties. This method checks for null */
    protected void putProperty(String name, Object value){
        if(value!=null){
            properties.put(name, value);
        }
    }

    /** Get a property as a string*/
    protected String getPropertyString(String name){
        if(properties.containsKey(name)){
            if(properties.get(name)!=null){
                return properties.get(name).toString();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    /** Get a property as an object */
    protected Object getPropertyObject(String name){
        return properties.get(name);
    }

}