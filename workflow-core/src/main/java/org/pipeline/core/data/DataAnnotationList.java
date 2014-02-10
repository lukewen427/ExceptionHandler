/*
 * DataAnnotationList.java
 */
package org.pipeline.core.data;

import org.pipeline.core.xmlstorage.*;
import java.util.*;
import org.json.*;

/**
 * This class contains a set of data annotation objects
 * @author hugo
 */
public class DataAnnotationList implements XmlStorable {
    /** Annotation list */
    private ArrayList<DataAnnotation> annotations = new ArrayList<DataAnnotation>();

    public DataAnnotationList() {
    }

    public DataAnnotationList(JSONObject json) throws JSONException {
        parseJson(json);
    }
    
    public int getSize(){
        return annotations.size();
    }
    
    public DataAnnotation getAnnotation(int index){
        return annotations.get(index);
    }
    
    public void addAnnotation(DataAnnotation annotation){
        annotations.add(annotation);
    }
    
    public void removeAnnotation(DataAnnotation annotation){
        annotations.remove(annotation);
    }
    
    public void removeAnnotation(int index){
        annotations.remove(index);
    }
    
    @Override
    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("DataAnnotationList");
        store.add("AnnotationCount", annotations.size());
        for(int i=0;i<annotations.size();i++){
            store.add("Annotation" + i, annotations.get(i));
        }
        return store;
    }

    @Override
    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        annotations.clear();
        int size = store.intValue("AnnotationCount", 0);
        for(int i=0;i<size;i++){
            annotations.add((DataAnnotation)store.xmlStorableValue("Annotation" + i));
        }
    }
    
    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        JSONArray annotationsJson = new JSONArray();
        for(int i=0;i<annotations.size();i++){
            annotationsJson.put(annotations.get(i).toJson());
        }
        json.put("items", annotationsJson);
        return json;
    }
    
    public void parseJson(JSONObject json) throws JSONException {
        JSONArray annotationsJson = json.getJSONArray("items");
        annotations.clear();
        for(int i=0;i<annotationsJson.length();i++){
            annotations.add(new DataAnnotation(annotationsJson.getJSONObject(i)));
        }
    }
}