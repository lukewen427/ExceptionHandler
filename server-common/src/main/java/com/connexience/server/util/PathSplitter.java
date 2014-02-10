/*
 * PathSplitter.java
 */

package com.connexience.server.util;
import java.util.*;
import java.io.*;
/**
 * This class splits a path based on a delimiter into a Vector of Strings
 * that represent each folder along the chain.
 * @author nhgh
 */
public class PathSplitter implements Serializable {
    ArrayList<String>pathElements = new ArrayList<String>();
    
    /** Current walk position */
    private int walkPosition = 0;
    
    /** Last path item */
    private String lastItem;
    
    /** Creates a new instance of PathSplitter */
    public PathSplitter(String path) {
        splitPath(path, "/");
    }
    
    /** Split with a custom delimiter */
    public PathSplitter(String path, String delim) {
        splitPath(path, delim);
    }
    
    /** Split the path using a StringTokenizer */
    private void splitPath(String path, String delim){
        StringTokenizer tokens = new StringTokenizer(path, delim);
        pathElements.clear();
        String token = null;
        while(tokens.hasMoreElements()){
            token = tokens.nextToken();
            pathElements.add(token);
        }
        lastItem = token;
    }
    
    /** Get all of the elements */
    public List<String>getPathElements(){
        return pathElements;
    }
    
    /** Reset the walk index */
    public void resetWalk(){
        walkPosition = 0;
    }
    
    /** Get the next element in a walk through the list */
    public String nextElement(){
        if(walkPosition<pathElements.size()){
            String value = pathElements.get(walkPosition);
            walkPosition++;
            return value;
        } else {
            return null;
        }
    }
    
    /** Are there any elements left */
    public boolean hasNextElement(){
        if(walkPosition<pathElements.size()){
            return true;
        } else {
            return false;
        }
    }
    
    /** Get the last item */
    public String getLastItem(){
        return lastItem;
    }
}
