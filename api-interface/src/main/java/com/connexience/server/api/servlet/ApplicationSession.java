/*
 * ApplicationSession.java
 */

package com.connexience.server.api.servlet;

import com.connexience.server.api.*;

import java.io.*;
import java.util.*;

/**
 * This class represents a session within an API application that can be used
 * across web domains to store API link objects and properties.
 * @author nhgh
 */
public class ApplicationSession implements Serializable {
    /** Configured client object */
    private API link = null;

    /** Session properties */
    private Hashtable<String, Object> properties = new Hashtable<String, Object>();

    /** Session ID */
    private String id;

    /** Last access time of this session */
    private long lastAccessTime = System.currentTimeMillis();

    /** Update the last access time */
    private void updateAccessTime(){
        lastAccessTime = System.currentTimeMillis();
    }

    /** Get the last access time */
    public long getLastAccessTime(){
        return lastAccessTime;
    }

    /** Get the session ID */
    public String getId(){
        updateAccessTime();
        return id;
    }

    /** Set the session ID */
    public void setId(String id){
        updateAccessTime();
        this.id = id;
    }
    
    /** Add a property */
    public void addProperty(String name, Object value){
        updateAccessTime();
        properties.put(name, value);
    }

    /** Get a property */
    public Object getProperty(String name){
        updateAccessTime();
        return properties.get(name);
    }

    /** Remove a property */
    public void removeProperty(String name){
        updateAccessTime();
        properties.remove(name);
    }

    /** Get the API link */
    public API getLink(){
        updateAccessTime();
        return link;
    }

    /** Set the API link object */
    public void setLink(API link){
        updateAccessTime();
        this.link = link;
    }
}
