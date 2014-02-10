/*
 * SimpleJsonPropertiesImporter.java
 */
package org.pipeline.core.data.io;

import org.pipeline.core.xmlstorage.*;
import org.json.*;

/**
 * This class imports a properties object from a JSON structure
 * @author hugo
 */
public class SimpleJsonPropertiesImporter {
    /** JSON to import */
    private JSONObject json;

    public SimpleJsonPropertiesImporter(JSONObject json) {
        this.json = json;
    }
    
    
    public XmlDataStore parseJson() throws XmlStorageException, JSONException {
        XmlDataStore store = new XmlDataStore();
        JSONArray propertyArray = json.getJSONArray("properties");
        
        JSONObject propertyJson;
        XmlDataObject property;
        
        for(int i=0;i<propertyArray.length();i++){
            propertyJson = propertyArray.getJSONObject(i);
            property = XmlDataObjectFactory.createDataObject(propertyJson.getString("type"), propertyJson.getString("type"), propertyJson.getString("value"));
            property.setDescription(propertyJson.getString("description"));
            store.add(property);
        }
        
        return store;
    }
}