/*
 * JSONPropertiesExporter.java
 */

package com.connexience.server.workflow.json;
import com.connexience.server.workflow.xmlstorage.*;
import org.pipeline.core.xmlstorage.*;
import org.pipeline.core.xmlstorage.xmldatatypes.*;
import org.json.*;
import java.util.*;

/**
 * This class exports an XmlDataStore to a JSON object
 * @author nhgh
 */
public class JSONPropertiesExporter {
    /** Properties to export */
    private XmlDataStore properties;

    public JSONPropertiesExporter(XmlDataStore properties) {
        this.properties = properties;
    }

    /** Export to JSON */
    public JSONObject createPropertiesJson() throws Exception {

        JSONObject json = new JSONObject();
        JSONArray propertyArray = new JSONArray();
        JSONObject propertyJson;

        
        Vector n = properties.getNames();
        Collections.sort(n);
        Enumeration names = n.elements();
        String name;
        int count = 0;

        XmlDataObject value;
        while(names.hasMoreElements()){
            name = names.nextElement().toString();
            value = properties.get(name);

            propertyJson = new JSONObject();
            propertyJson.put("name", name);
            propertyJson.put("type", value.getTypeLabel());
            propertyJson.put("description", value.getDescription());
            propertyJson.put("value", properties.getPropertyString(name));
            propertyJson.put("category", value.getCategory());
            
            if(value.getTypeLabel().equalsIgnoreCase("XmlStorable")){

                String className = ((XmlStorableDataObject)value).getClassName();
                if(className.equals("com.connexience.server.workflow.xmlstorage.DocumentRecordWrapper")){
                    propertyJson.put("type", "Document");
                    propertyJson.put("jsonValue", createDocumentJsonObject((DocumentRecordWrapper)((XmlStorableDataObject)value).getValue()));

                } else if(className.equals("com.connexience.server.workflow.xmlstorage.FolderWrapper")){
                    propertyJson.put("type", "Folder");
                    propertyJson.put("jsonValue", createFolderJsonObject((FolderWrapper)((XmlStorableDataObject)value).getValue()));

                } else if(className.equals("com.connexience.server.workflow.xmlstorage.StringListWrapper")){
                    propertyJson.put("type", "StringList");
                    propertyJson.put("jsonValue", createStringListObject((StringListWrapper)((XmlStorableDataObject)value).getValue()));
                    
                } else if(className.equals("com.connexience.server.workflow.xmlstorage.StringPairListWrapper")){
                    propertyJson.put("type", "TwoColumnList");
                    propertyJson.put("jsonValue", createStringPairListObject((StringPairListWrapper)((XmlStorableDataObject)value).getValue()));

                } else if(className.equals("com.connexience.server.workflow.xmlstorage.ExternalObjectWrapper")){
                    propertyJson.put("type", "ExternalObject");
                    propertyJson.put("jsonValue", createExternalObjectJsonObject((ExternalObjectWrapper)((XmlStorableDataObject)value).getValue()));
                    
                }

            }
            propertyArray.put(propertyJson);
            count++;

        }
        json.put("propertyCount", count);
        json.put("propertyArray", propertyArray);

        return json;


    }

    /** Create an auxiliart JSON object for an ExternalObject */
    private JSONObject createExternalObjectJsonObject(ExternalObjectWrapper externalObject) throws Exception {
        JSONObject externalObjectJson = new JSONObject();
        externalObjectJson.put("name", externalObject.getName());
        externalObjectJson.put("id", externalObject.getId());
        externalObjectJson.put("applicationId", externalObject.getApplicationId());
        externalObjectJson.put("typeString", externalObject.getTypeString());
        return externalObjectJson;
    }

    /** Create an auxiliary JSON object for a document */
    private JSONObject createDocumentJsonObject(DocumentRecordWrapper document) throws Exception {
        JSONObject documentJson = new JSONObject();
        documentJson.put("id", document.getId());
        documentJson.put("name", document.getName());
        documentJson.put("description", document.getDescription());
        return documentJson;
    }

    /** Create an auxiliary JSON object for a folder */
    private JSONObject createFolderJsonObject(FolderWrapper folder) throws Exception {
        JSONObject folderJson = new JSONObject();
        folderJson.put("id", folder.getId());
        folderJson.put("name", folder.getName());
        folderJson.put("description", folder.getDescription());
        return folderJson;
    }

    /** Create a String List JSON object */
    private JSONObject createStringListObject(StringListWrapper stringList) throws Exception {
        JSONObject listJson = new JSONObject();
        JSONArray values = new JSONArray();
        for(int i=0;i<stringList.getSize();i++){
            values.put(stringList.getValue(i));
        }
        listJson.put("values", values);
        listJson.put("valueCount", stringList.getSize());
        return listJson;
    }

    /** Create a two column list wrapper */
    private JSONObject createStringPairListObject(StringPairListWrapper stringList) throws Exception {
        JSONObject listJson = new JSONObject();
        JSONArray column1 = new JSONArray();
        JSONArray column2 = new JSONArray();
        for(int i=0;i<stringList.getSize();i++){
            column1.put(stringList.getValue(i, 0));
            column2.put(stringList.getValue(i, 1));
        }
        listJson.put("column1", column1);
        listJson.put("column2", column2);
        listJson.put("valueCount", stringList.getSize());
        return listJson;
    }
}
