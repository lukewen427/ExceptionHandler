/*
 * SimpleJsonPropertiesExporter.java
 */
package org.pipeline.core.data.io;

import org.pipeline.core.xmlstorage.*;

import org.json.*;
import java.util.*;

/**
 * This class exports a set of properties as a JSON object. It only works with
 * the simple property types.
 * @author hugo
 */
public class SimpleJsonPropertiesExporter {
    /** Properties to export */
    private XmlDataStore properties;

    public SimpleJsonPropertiesExporter(XmlDataStore properties) {
        this.properties = properties;
    }
    
    public JSONObject toJson() throws JSONException, XmlStorageException {
        JSONObject json = new JSONObject();
        JSONArray propertyArray = new JSONArray();
        JSONObject propertyJson;

        Enumeration names = properties.getNames().elements();
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
            propertyArray.put(propertyJson);
            count++;

        }
        json.put("properties", propertyArray);
        json.put("objectId", "");
        json.put("groupName", "");

        return json;   
    }
}