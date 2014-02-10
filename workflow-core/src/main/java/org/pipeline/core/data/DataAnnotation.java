/*
 * DataAnnotation.java
 */
package org.pipeline.core.data;

import org.pipeline.core.xmlstorage.*;
import org.json.*;

/**
 * This class represents an annotation of a piece of data.
 * @author hugo
 */
public class DataAnnotation implements XmlStorable {
    /** Start point for the annotation */
    private int startRow = 0;
    
    /** End point for the annotation */
    private int endRow = 10;
    
    /** Title of the annotation */
    private String title = "";
    
    /** Text of the annotation */
    private String text = "";
    
    /** Link to another object */
    private String relatedObjectId = null;

    public DataAnnotation() {
    }
    
    public DataAnnotation(JSONObject json) throws JSONException {
        parseJson(json);
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public String getRelatedObjectId() {
        return relatedObjectId;
    }

    public void setRelatedObjectId(String relatedObjectId) {
        this.relatedObjectId = relatedObjectId;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    @Override
    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("DataAnnotation");
        store.add("StartRow", startRow);
        store.add("EndRow", endRow);
        store.add("Text", text);
        store.add("Title", title);
        if(relatedObjectId!=null){
            store.add("RelatedObjectID", relatedObjectId);
        }
        return store;
    }

    @Override
    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        startRow = store.intValue("StartRow", 0);
        endRow = store.intValue("EndRow", 10);
        text = store.stringValue("Text", "");
        title = store.stringValue("Title", "");
        relatedObjectId = store.stringValue("RelatedObjectID", null);
    }
    
    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        
        json.put("startRow", startRow);
        json.put("endRow", endRow);
        json.put("text", text);
        json.put("title", title);
        if(relatedObjectId!=null){
            json.put("hasRelatedObjectId", true);
            json.put("relatedObjectId", relatedObjectId);
        } else {
            json.put("hasRelatedObjectId", false);
        }
        
        return json;
    }
    
            
    public void parseJson(JSONObject json) throws JSONException {
        startRow = json.getInt("startRow");
        endRow = json.getInt("endRow");
        text = json.getString("text");
        title = json.getString("title");
        if(json.getBoolean("hasRelatedObjectId")==true){
            relatedObjectId = json.getString("relatedObjectId");
        } else {
            relatedObjectId = null;
        }
    }
}