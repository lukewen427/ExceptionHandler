/*
 * StringListWrapper.java
 */

package com.connexience.server.workflow.xmlstorage;
import org.pipeline.core.xmlstorage.*;

import java.util.*;
import java.io.*;

/**
 * This class provides an XmlStorable array of Strings
 * @author nhgh
 */
public class StringListWrapper implements XmlStorable, Serializable {
    /** String values */
    private ArrayList<String> values = new ArrayList<String>();

    /** Get the size of this wrapper */
    public int getSize(){
        return values.size();
    }
    
    /** Get a value */
    public String getValue(int index){
        return values.get(index);
    }
    
    /** Add a value */
    public void add(String value){
        values.add(value);
    }
    
    /** Remove a value */
    public void remove(int index){
        values.remove(index);
    }

    /** Get a toString value */
    @Override
    public String toString() {
        return "List";
    }
    
    /** Return as a string array */
    public String[] toStringArray(){
        String[] list = new String[values.size()];
        for(int i=0;i<values.size();i++){
            list[i] = values.get(i);
        }
        return list;
    }
    
    /** Save this object to Xml */
    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("StringList");
        store.add("Size", values.size());
        for(int i=0;i<values.size();i++){
            store.add("Value" + i, values.get(i));
        }
        return store;
    }

    /** Recreate this object from Xml */
    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        int size = store.intValue("Size", 0);
        values.clear();
        for(int i=0;i<size;i++){
            values.add(store.stringValue("Value" + i, ""));
        }
    }
}