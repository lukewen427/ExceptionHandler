/*
 * ObjectWrapper.java
 */

package com.connexience.server.workflow.engine.datatypes;

import com.connexience.server.util.DigestingOutputStream;
import com.connexience.server.util.SerializationUtils;
import com.connexience.server.workflow.engine.HashableTransferObject;
import com.connexience.server.workflow.util.ZipUtils;
import org.pipeline.core.drawing.*;

import java.io.*;

/**
 * This class provides a transfer data object which wraps up a serialized
 * java object.
 * @author nhgh
 */
public class ObjectWrapper implements TransferData, StorableTransferData, HashableTransferObject {
    /** Object being stored */
    private Object storedObject = null;

    /** Byte array storing content of the object */
    private byte[] objectData = null;
    
    /** MD5 hash value */
    String hashValue = null;
    
    /** Construct an empty object wrapper */
    public ObjectWrapper(){
        
    }
    
    /** Construct with a serializble object */
    public ObjectWrapper(Object storedObject) {
        this.storedObject = storedObject;
    }
    
    /** Construct with some object data */
    public ObjectWrapper(byte[] objectData){
        this.objectData = objectData;
    }
    
    /** Get a copy of the saved object */
    public TransferData getCopy() throws DrawingException {
        try {
            if(storedObject instanceof Serializable){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ObjectOutputStream objWriter = new ObjectOutputStream(stream);
                objWriter.writeObject(storedObject);
                objWriter.flush();
                objWriter.close();
                ByteArrayInputStream inStream = new ByteArrayInputStream(stream.toByteArray());
                ObjectInputStream objReader = new ObjectInputStream(inStream);
                Object newObj = objReader.readObject();
                return new ObjectWrapper(newObj);
            } else {
                throw new DrawingException("Cannot serialize object");
            }
            
        } catch (Throwable t){
            throw new DrawingException("Error copying data: " + t.getMessage());
        }
    }

    /** Get the contained object */
    public Object getPayload() {
        if(storedObject!=null){
            return storedObject;
        } else {
            try {
                storedObject = SerializationUtils.deserialize(objectData);
                return storedObject;
            } catch (Exception e){
                return null;
            }
        }
    }

    /** Load the object from an input stream */
    public void loadFromInputStream(InputStream inStream) throws DrawingException {
        try {
            ByteArrayOutputStream store = new ByteArrayOutputStream();
            ZipUtils.copyInputStream(inStream, store);
            this.objectData = store.toByteArray();
        } catch (Exception e){
            throw new DrawingException("Cannot load object from stream: " + e.getMessage());
        }
    }

    /** Save the object to an output stream */
    public void saveToOutputStream(OutputStream outStream) throws DrawingException {
        try {
            DigestingOutputStream digestStream = new DigestingOutputStream(outStream);
            
            if(storedObject!=null && objectData==null){
                // Write object
                
                ObjectOutputStream writer = new ObjectOutputStream(digestStream);
                writer.writeObject(storedObject);
                writer.flush();
                writer.close();
                
            } else if(objectData!=null){
                // Directly write data
                ByteArrayInputStream source = new ByteArrayInputStream(objectData);
                ZipUtils.copyInputStream(source, digestStream);
            }
            
            digestStream.flush();
            digestStream.close();
            hashValue = digestStream.getHash();            
        } catch (Exception e){
            throw new DrawingException("Error saving object to stream: " + e.getMessage());
        }
    }

    @Override
    public String getHash() throws DrawingException {
        if(hashValue!=null){
            return hashValue;
        } else {
            throw new DrawingException("ObjectWrapper must be saved before a hash is obtained");
        }
    }
    
    
    /** Get the raw object data */
    public byte[] getObjectData(){
        return objectData;
    }
}