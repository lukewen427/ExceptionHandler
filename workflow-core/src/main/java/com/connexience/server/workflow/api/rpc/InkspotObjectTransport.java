/*
 * InkspotObjectTransport.java
 */

package com.connexience.server.workflow.api.rpc;

import com.connexience.server.api.*;
import com.connexience.server.api.impl.*;
import org.pipeline.core.xmlstorage.*;
import com.connexience.server.util.*;

/**
 * This class provides an XmlStorable wrapper for an InkspotObject API object.
 * This allows API objects to be easily sent via the Xml RPC code that the
 * workflow editor and rich client uses.
 * @author nhgh
 */
public class InkspotObjectTransport implements XmlStorable {
    /** Object being stored */
    private InkspotObject storedObject = null;

    public InkspotObjectTransport(){

    }
    
    /** Create with an object interface */
    public InkspotObjectTransport(Object obj) throws ClassCastException {
        try {
            storedObject = (InkspotObject)obj;
        } catch (Exception e){
            throw new ClassCastException("InkspotObjectTransport only works with the standard object implementation");
        }
    }

    /** Get the object */
    public IObject getObject(){
        return storedObject;
    }

    /** Does this store contains a valid object */
    public boolean containsObject(){
        if(storedObject!=null){
            return true;
        } else {
            return false;
        }
    }
    
    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("InkspotObject");
        if(storedObject!=null){
            try {
                byte[] objectData = SerializationUtils.serialize(storedObject);
                store.add("ContainsObject", true);
                store.add("ObjectClassName", storedObject.getClass().getName());
                store.add("ObjectData", objectData);
            } catch (Exception e){
                store.add("ContainsObject", false);
            }
        } else {
            store.add("ContainsObject", false);
        }
        return store;
    }

    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        if(store.booleanValue("ContainsObject", false)){
            byte[] objectData = store.byteArrayValue("ObjectData");
            try {
                storedObject = (InkspotObject)SerializationUtils.deserialize(objectData);
                if(storedObject!=null){
                     String expectedClassName = store.stringValue("ObjectClassName", "");
                     if(!storedObject.getClass().getName().equals(expectedClassName)){
                         throw new Exception("Expected class does not match loaded object");
                     }
                } else {
                    throw new Exception("No object data found");
                }
            } catch (Exception e){
                throw new XmlStorageException("Cannot deserialize object: " + e.getMessage());
            }

        } else {
            storedObject = null;
        }
    }
}