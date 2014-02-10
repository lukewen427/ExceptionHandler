/*
 * ContentQuery.java
 */
package com.connexience.server.model.social.content;

import java.io.*;
import java.util.*;

/**
 * This class defines a query for content type. It contains a hashtable of query
 * parameters that are interpreted by the specific content fetcher bean.
 * @author hugo
 */
public class ContentQuery implements Serializable {
    /** List of query parameters */
    Hashtable queryParameters = new Hashtable();
    
    /** Label for this query */
    private String label;
    
    public ContentQuery() {
    }

    /** Set the query label */
    public void setLabel(String label){
        this.label = label;
    }
    
    /** Get the query label */
    public String getLabel(){
        return label;
    }
    
    /** Add a query parameter */
    public void addParameter(String name, Object value){
        queryParameters.put(name, value);
    }
    
    /** Get a query parameter */
    public Object getParameter(String name){
        return queryParameters.get(name);
    }
    
    /** Does a query parameter exist */
    public boolean parameterExists(String name){
        return queryParameters.containsKey(name);
    }
    
    /** Get a list of names */
    public Iterator names() {
        return queryParameters.keySet().iterator();
    }
}