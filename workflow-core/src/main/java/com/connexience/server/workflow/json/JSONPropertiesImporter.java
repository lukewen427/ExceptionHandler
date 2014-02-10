/*
 * JSONPropertiesImporter.java
 */

package com.connexience.server.workflow.json;

import com.connexience.server.workflow.xmlstorage.DocumentRecordWrapper;
import com.connexience.server.workflow.xmlstorage.ExternalObjectWrapper;
import com.connexience.server.workflow.xmlstorage.FolderWrapper;
import com.connexience.server.workflow.xmlstorage.StringListWrapper;
import com.connexience.server.workflow.xmlstorage.StringPairListWrapper;
import org.pipeline.core.xmlstorage.*;
import org.pipeline.core.xmlstorage.xmldatatypes.*;
import org.json.*;
import java.util.*;

/**
 * This class reads an XmlDataStore from a JSON representation
 * @author nhgh
 */
public class JSONPropertiesImporter {
    /** Propertie JSON object */
    private JSONObject propertiesJson;

    public JSONPropertiesImporter(JSONObject propertiesJson) {
        this.propertiesJson = propertiesJson;
    }

    /** Parse the properties and create an XmlDataStore */
    public XmlDataStore createXmlDataStore() throws Exception {
        JSONObject propertyJson;
        XmlDataObject property;
        XmlDataStore loadedProperties = new XmlDataStore();

        int propertyCount = propertiesJson.getInt("propertyCount");
        JSONArray propertyArray = propertiesJson.getJSONArray("propertyArray");

        for(int i=0;i<propertyCount;i++){
            propertyJson = propertyArray.getJSONObject(i);
            try {
                if(propertyJson.has("name") && propertyJson.has("description") && propertyJson.has("type")){
                    property = createProperty(propertyJson);
                    if(property!=null){
                        loadedProperties.add(property);
                    }
                }
            } catch (XmlStorageException xmlse){
                System.out.println("Storage error: " + xmlse.getMessage());
            }
        }

        return loadedProperties;
    }

    /** Merge an existing property collection with the properties contained
     * in this importer */
    public void mergeXmlDataStore(XmlDataStore existingProperties) throws Exception {
        existingProperties.copyProperties(createXmlDataStore());
    }

    /** Create a property object */
    private XmlDataObject createProperty(JSONObject propertyJson) throws Exception {
        String name = propertyJson.getString("name");
        String type = propertyJson.getString("type");
        String description = propertyJson.getString("description");

        String value = null;
        if(propertyJson.has("value")){
            value = propertyJson.getString("value");
        }
        
        String category = null;
        if(propertyJson.has("category")){
            category = propertyJson.getString("category");
        }

        JSONObject jsonValue = null;
        if(propertyJson.has("jsonValue")){
            jsonValue = propertyJson.getJSONObject("jsonValue");
        }
        XmlDataObject property = null;

        if(type.equals("Document")){
            DocumentRecordWrapper docWrapper = new DocumentRecordWrapper();
            if(jsonValue.has("name")){docWrapper.setName(jsonValue.getString("name"));}
            if(jsonValue.has("id")){docWrapper.setId(jsonValue.getString("id"));}
            property = new XmlStorableDataObject(name, docWrapper);

        } else if(type.equals("Folder")){
            FolderWrapper folderWrapper = new FolderWrapper();
            if(jsonValue.has("name")){folderWrapper.setName(jsonValue.getString("name"));}
            if(jsonValue.has("id")){folderWrapper.setId(jsonValue.getString("id"));}
            property = new XmlStorableDataObject(name, folderWrapper);
            
        } else if(type.equals("StringList")){
            StringListWrapper stringList = createStringListWrapper(jsonValue);
            property = new XmlStorableDataObject(name, stringList);
            
        } else if(type.equals("TwoColumnList")){
            StringPairListWrapper stringPairList = createStringPairListWrapper(jsonValue);
            property = new XmlStorableDataObject(name, stringPairList);

        } else if(type.equals("ExternalObject")){
            ExternalObjectWrapper externalObject = createExternalObjectWrapper(jsonValue);
            property = new XmlStorableDataObject(name, externalObject);

        } else {
            property = XmlDataObjectFactory.createDataObject(type, value, name);
        }

        if(property!=null){
            property.setDescription(description);
            property.setCategory(category);
            return property;
        } else {
            return null;
        }
    }

    /** Create a wrapper for an external object */
    private ExternalObjectWrapper createExternalObjectWrapper(JSONObject externalObjectJson) throws Exception {
        ExternalObjectWrapper wrapper = new ExternalObjectWrapper();
        if(externalObjectJson.has("applicationId")){
            wrapper.setApplicationId(externalObjectJson.getString("applicationId"));
        } else {
            wrapper.setApplicationId("");
        }

        if(externalObjectJson.has("id")){
            wrapper.setId(externalObjectJson.getString("id"));
        } else {
            wrapper.setId("");
        }

        if(externalObjectJson.has("name")){
            wrapper.setName(externalObjectJson.getString("name"));
        } else {
            wrapper.setName("");
        }

        if(externalObjectJson.has("typeString")){
            wrapper.setTypeString(externalObjectJson.getString("typeString"));
        } else {
            wrapper.setTypeString("");
        }
        return wrapper;
    }

    /** Create a String List object */
    private StringListWrapper createStringListWrapper(JSONObject stringListJson) throws Exception {
        StringListWrapper wrapper = new StringListWrapper();
        int size = stringListJson.getInt("valueCount");
        JSONArray values = stringListJson.getJSONArray("values");
        for(int i=0;i<size;i++){
            wrapper.add(values.getString(i));
        }
        return wrapper;
    }

    /** Create a two column string list */
    private StringPairListWrapper createStringPairListWrapper(JSONObject stringPairListJson) throws Exception {
        StringPairListWrapper wrapper = new StringPairListWrapper();
        int size = stringPairListJson.getInt("valueCount");
        JSONArray column1 = stringPairListJson.getJSONArray("column1");
        JSONArray column2 = stringPairListJson.getJSONArray("column2");
        for(int i=0;i<size;i++){
            wrapper.add(column1.getString(i), column2.getString(i));
        }
        return wrapper;
    }
}
