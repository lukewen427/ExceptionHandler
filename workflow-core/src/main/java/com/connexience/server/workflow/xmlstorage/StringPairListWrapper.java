/*
 * StringPairListWrapper.java
 */

package com.connexience.server.workflow.xmlstorage;

import org.pipeline.core.xmlstorage.*;

import java.io.*;
import java.util.*;

/**
 * This class provides a wrapper for a two column list of strings
 * @author nhgh
 */
public class StringPairListWrapper implements XmlStorable, Serializable {
    /** First column of strings */
    private Vector<String> column1 = new Vector<String>();
    
    /** Second column of strings */
    private Vector<String> column2 = new Vector<String>();

    public StringPairListWrapper() {
    }

    /** Get the size of this wrapper */
    public int getSize(){
        return column1.size();
    }
    
    /** Get a value */
    public String getValue(int row, int column){
        if(column==0){
            return column1.get(row);
        } else {
            return column2.get(row);
        }
    }
    
    /** Set a value */
    public void setValue(int row, int column, String value){
        if(column==0){
            column1.insertElementAt(value, row);
            column1.remove(row + 1);
        } else {
            column2.insertElementAt(value, row);
            column2.remove(row + 1);
        }
    }
    
    /** Add a value */
    public void add(String column1Value, String column2Value){
        column1.add(column1Value);
        column2.add(column2Value);
    }
    
    /** Remove a value */
    public void remove(int index){
        column1.remove(index);
        column2.remove(index);
    }

    /** Get a toString value */
    @Override
    public String toString() {
        return "List";
    }
    
    /** Return as a string matrix */
    public String[][] toStringArray(){
        String[][] list = new String[column1.size()][2];
        for(int i=0;i<column1.size();i++){
            list[i][0] = column1.get(i);
            list[i][1] = column2.get(i);
        }
        return list;
    }
    
    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("StringPairListWrapper");
        store.add("Size", column1.size());
        for(int i=0;i<column1.size();i++){
            store.add("C0R" + i, column1.get(i));
            store.add("C1R" + i, column2.get(i));
        }
        return store;
    }

    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        column1.clear();
        column2.clear();
        int size = store.intValue("Size", 0);
        for(int i=0;i<size;i++){
            add(store.stringValue("C0R" + i, ""), store.stringValue("C1R" + i, ""));
        }
    }
}