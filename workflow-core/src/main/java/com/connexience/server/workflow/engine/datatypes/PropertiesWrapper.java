/*
 * PropertiesWrapper
 */

package com.connexience.server.workflow.engine.datatypes;
import com.connexience.server.util.DigestingOutputStream;
import com.connexience.server.workflow.engine.HashableTransferObject;
import java.io.InputStream;
import java.io.OutputStream;
import org.pipeline.core.drawing.*;
import org.pipeline.core.xmlstorage.*;
import org.pipeline.core.xmlstorage.io.*;

/**
 * This data wrapper allows a set of name-value pairs to be passed
 * between workflow blocks (services).
 * @author hugo
 */
public class PropertiesWrapper implements TransferData, StorableTransferData, HashableTransferObject {
    /** Properties being transferred */
    private XmlDataStore properties;

    /** MD5 hash value */
    private String hashValue = null;
    
    public PropertiesWrapper(){
        properties = new XmlDataStore("Properties");
    }
    
    /** Construct with properties */
    public PropertiesWrapper(XmlDataStore properties) {
        this.properties = properties;
    }
   
    /** Get the properties payload */
    public Object getPayload() {
        return properties;
    }

    /** Get a copy of this transfer data */
    public TransferData getCopy() throws DrawingException {
        PropertiesWrapper wrapper = new PropertiesWrapper((XmlDataStore)properties.getCopy());
        return wrapper;
    }

    public void loadFromInputStream(InputStream stream) throws DrawingException {
        try {
            XmlDataStoreStreamReader reader = new XmlDataStoreStreamReader(stream);
            this.properties = reader.read();
        } catch (Exception e){
            throw new DrawingException("Error loading property data: " + e.getMessage(), e);
        }
    }

    public void saveToOutputStream(OutputStream stream) throws DrawingException {
        try {
            DigestingOutputStream digestStream = new DigestingOutputStream(stream);
            XmlDataStoreStreamWriter writer = new XmlDataStoreStreamWriter(properties);
            writer.writeToOutputStream(digestStream);
            stream.flush();
            digestStream.flush();
            digestStream.close();
            hashValue = digestStream.getHash();
        } catch (Exception e){
            throw new DrawingException("Error saving property data: " + e.getMessage());
        }
    }    
    
    public XmlDataStore properties() {
        return properties;
    }

    @Override
    public String getHash() throws DrawingException {
        if(hashValue!=null){
            return hashValue; 
        } else {
            throw new DrawingException("PropertiesWrapper must be saved before a hash is obtained");
        }
    }
    
    
}