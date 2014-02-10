/*
 * DataPropertiesIO.java
 */
package org.pipeline.core.data.io;

import com.connexience.server.api.*;
import org.pipeline.core.xmlstorage.*;

import java.io.*;
import java.util.*;

/**
 * This class saves and retrieves sets of properties for data using the API
 * @author hugo
 */
public class DataPropertiesIO {
    /** Properties to be saved */
    private XmlDataStore properties;
    
    /** Document holding the data */
    private IDocument document;
    
    /** API link for accessing server */
    private API link;

    public DataPropertiesIO(XmlDataStore properties, API link, IDocument document) {
        this.properties = properties;
        this.document = document;
        this.link = link;
    }

    public DataPropertiesIO(API link, IDocument document) {
        this.document = document;
        this.link = link;
        this.properties = null;
    }
    
    public XmlDataStore getProperties(){
        return properties;
    }
    
    public void write() throws APIInstantiationException, APIConnectException, APISecurityException, APIParseException, XmlStorageException {     
        IPropertyList propertyList = (IPropertyList)link.createObject(IPropertyList.XML_NAME);
        propertyList.setObjectId(document.getId());
        XmlDataObject propertyObject;
        
        propertyList.set("PropertyCount", properties.size());
        int count = 0;
        Enumeration e = properties.elements();
        while(e.hasMoreElements()){
            propertyObject = (XmlDataObject)e.nextElement();
            if(XmlDataObjectFactory.canDataObjectParseString(propertyObject.getTypeLabel())){
                
                propertyList.set("Property" + count + "Name", propertyObject.getName());
                propertyList.set("Property" + count + "Type", propertyObject.getTypeLabel());
                propertyList.set("Property" + count + "Value", propertyObject.getValue().toString());

            
            } else {
                throw new APIConnectException("Cannot save complex properties");
            }
            count++;

        }
        link.setObjectProperties(document.getId(), "Attributes", propertyList);        
    }
    
    public XmlDataStore read() throws APIInstantiationException, APIConnectException, APISecurityException, APIParseException, XmlStorageException {
        IPropertyList propertyList = link.getObjectProperties(document.getId(), "Attributes");
        XmlDataObject propertyObject;
        int count = propertyList.intValue("PropertyCount", 0);
        properties = new XmlDataStore();
        String name;
        String value;
        String type;
        
        for(int i=0;i<count;i++){
            type = propertyList.stringValue("Property" + i + "Type", null);
            if(type!=null && XmlDataObjectFactory.canDataObjectParseString(type)){
                name = propertyList.stringValue("Property" + i + "Name", null);
                value = propertyList.stringValue("Property" + i + "Value", null);
                if(name!=null && !name.isEmpty() && value!=null){
                    propertyObject = XmlDataObjectFactory.createDataObject(type, value, name);
                    properties.add(propertyObject);
                }
            }
        }
        return properties;
    }
}
