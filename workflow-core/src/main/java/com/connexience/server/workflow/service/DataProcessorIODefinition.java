/*
 * DataProcessorIODefinition.java
 */

package com.connexience.server.workflow.service;

import org.pipeline.core.xmlstorage.*;
import java.io.*;

/**
 * This class defines an input or output definition for a DataProcessorService
 * @author hugo
 */
public class DataProcessorIODefinition implements XmlStorable, Serializable {
    /** Input definition */
    public static final int INPUT_DEFINITION = 0;
    
    /** Output definition */
    public static final int OUPUT_DEFINITION = 1;

    /** Streaming connection type */
    public static final String STREAMING_CONNECTION = "Streaming";

    /** Non-Streaming connection type */
    public static final String NON_STREAMING_CONNECTION = "NonStreaming";

    /** Definition type. Input or output */
    private int type = INPUT_DEFINITION;
    
    /** Definition name. This is the name that services use to get hold of the
     * transfer data */
    private String name = "";
    
    /** Name of the data type to enforce */
    private String dataTypeName = "data-wrapper";

    /** Connection transfer mode */
    private String mode = NON_STREAMING_CONNECTION;

    /** Get the transfer mode */
    public String getMode(){
        return mode;
    }

    /** Set the transfer mode */
    public void setMode(String mode){
        if(mode.equals(STREAMING_CONNECTION) || mode.equals(NON_STREAMING_CONNECTION)){
            this.mode = mode;
        }
    }

    /** Get the definition type */
    public int getType(){
        return type;
    }
    
    /** Set the definition type */
    public void setType(int type){
        this.type = type;
    }
    
    /** Get the data type name for this definition */
    public String getDataTypeName(){
        return dataTypeName;
    }
    
    /** Set the data type name for this definition */
    public void setDataTypeName(String dataTypeName){
        this.dataTypeName = dataTypeName;
    }
    
    /** Get the definition name */
    public String getName(){
        return name;
    }

    /** Set the definition name */
    public void setName(String name){
        this.name = name;
    }

    /** Save to storage */
    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("DataProcessorIODefinition");
        store.add("Type", type);
        store.add("Name", name);
        store.add("DataTypeName", dataTypeName);
        store.add("Mode", mode);
        return store;
    }

    /** Recreate from storage */
    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        this.type = store.intValue("Type", INPUT_DEFINITION);
        this.name = store.stringValue("Name", "");
        this.dataTypeName = store.stringValue("DataTypeName", "data-wrapper");
        this.mode = store.stringValue("Mode", NON_STREAMING_CONNECTION);
    }
}