/*
 * DataTypes.java
 */

package com.connexience.server.workflow.engine;
import com.connexience.server.workflow.engine.datatypes.*;
import com.connexience.server.workflow.service.*;
import org.pipeline.core.drawing.*;

import java.util.*;
import java.io.*;

/**
 * This class contains the static singleton data types that
 * can be passed between services within a workflow.
 * @author hugo
 */
public class DataTypes {
    /** Standard column based set of data */
    public static final DataType DATA_WRAPPER_TYPE = new DataWrapperDataType();
    
    /** Set of name-value pairs data */
    public static final DataType PROPERTIES_WRAPPER_TYPE = new PropertiesWrapperDataType();
    
    /** Serialized object */
    public static final DataType OBJECT_WRAPPER_TYPE = new ObjectWrapperDataType();

    /** List of file names */
    public static final DataType FILE_WRAPPER_TYPE = new FileWrapperDataType();

    /** Hashtable of data types */
    private static Hashtable<String,DataType> dataTypes = new Hashtable<String,DataType>();
    
    // Initialise the data type map
    static {
        dataTypes.put("data-wrapper", DATA_WRAPPER_TYPE);
        dataTypes.put("properties-wrapper", PROPERTIES_WRAPPER_TYPE);
        dataTypes.put("object-wrapper", OBJECT_WRAPPER_TYPE);
        dataTypes.put("file-wrapper", FILE_WRAPPER_TYPE);
    }
    
    /** Get a list of data types */
    public static ArrayList<String> listDataTypeNames(){
        Enumeration<String> keys = dataTypes.keys();
        ArrayList<String> keyList = new ArrayList<String>();
        while(keys.hasMoreElements()){
            keyList.add(keys.nextElement());
        }
        return keyList;
    }
    
    /** Get a data type by name */
    public static DataType getDataType(String typeName){
        return dataTypes.get(typeName);
    }
    
    /** Get the name of a data type */
    public static DataType getDataType(TransferData data) {
        Enumeration<DataType> types = dataTypes.elements();
        DataType type;
        
        while(types.hasMoreElements()){
            type = types.nextElement();
            if(type.getDataClass().equals(data.getClass())){
                return type;
            }
        }
        return null;
    }
    
    /** Instantiate and load a data type from an InputStream */
    public static TransferData instantiateTransferData(String typeName, InputStream stream) throws DrawingException {
        DataType type = getDataType(typeName);
        if(type!=null){
            TransferData td = null;
            
            try {
                td = (TransferData)type.getDataClass().newInstance();
            } catch (Exception e){
                throw new DrawingException("Cannot instantiate data transfer object: " + e.getMessage());
            }
            
            // Load data if appropriate
            if(td instanceof StorableTransferData){
                ((StorableTransferData)td).loadFromInputStream(stream);
            } else if(td instanceof StreamableTransferData){
                ((StreamableTransferData)td).beginReading(stream);
            }
            
            return td;
        } else {
            throw new DrawingException("No such data type: " + typeName);
        }
    }

    /** Instantiate a data transfer object from an output stream. This only works if the
     * data type supports the streamable interface */
    public static TransferData instantiateTransferData(String typeName, OutputStream stream) throws DrawingException {
        DataType type = getDataType(typeName);
        if(type!=null){
            TransferData td = null;
            try {
                td = (TransferData)type.getDataClass().newInstance();
            } catch (Exception e){
                throw new DrawingException("Cannot instantiate data transfer object: " + e.getMessage());
            }

            // Set up with a stream
            if(td instanceof StreamableTransferData){
                StreamableTransferData std = (StreamableTransferData)td;
                std.beginWriting(stream);
                return td;
            } else {
                throw new DrawingException("Cannot instantiate output data with a stream for non-streamable data type");
            }
        } else {
            throw new DrawingException("No such data type: " + typeName);
        }
    }

    /** Is a data type streamable. TODO: This creates an instance of the transfer object, needs
     * to be fixed */
    public static boolean isStreamable(String typeName) throws DrawingException {
        DataType type = getDataType(typeName);
        if(type!=null){
            TransferData td = null;
            try {
                td = (TransferData)type.getDataClass().newInstance();
                if(td instanceof StreamableTransferData){
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e){
                throw new DrawingException("Cannot instantiate data transfer object: " + e.getMessage());
            }
        } else {
            throw new DrawingException("No such data type: " + typeName);
        }
    }
}

