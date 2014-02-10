/*
 * DataAnnotationIO.java
 */
package org.pipeline.core.data.io;

import com.connexience.server.api.*;
import org.pipeline.core.data.*;

/**
 * This class saves and retrieves data annotations as object properties
 * via the API
 * @author hugo
 */
public class DataAnnotationIO {
    DataAnnotationList annotations;
    IDocument document;
    API link;
    
    public DataAnnotationIO(DataAnnotationList annotations, API link, IDocument document) {
        this.annotations = annotations;
        this.document = document;
        this.link = link;
    }

    public DataAnnotationIO(API link, IDocument document) {
        this.annotations = null;
        this.document = document;
        this.link = link;
    }

    public DataAnnotationList getAnnotations() {
        return annotations;
    }
    
    public void write() throws APIInstantiationException, APIConnectException, APISecurityException, APIParseException {     
        IPropertyList properties = (IPropertyList)link.createObject(IPropertyList.XML_NAME);
        properties.setObjectId(document.getId());
        DataAnnotation annotation;
        
        properties.set("AnnotationCount", annotations.getSize());
        
        for(int i=0;i<annotations.getSize();i++){
            annotation = annotations.getAnnotation(i);
            properties.set("Annotation" + i + "Start", annotation.getStartRow());
            properties.set("Annotation" + i + "End", annotation.getEndRow());
            properties.set("Annotation" + i + "Text", annotation.getText());
            properties.set("Annotation" + i + "Title", annotation.getTitle());
            if(annotation.getRelatedObjectId()!=null){
                properties.set("Annotation" + i + "HasLink", true);
                properties.set("Annotation" + i + "LinkID", annotation.getRelatedObjectId());
            } else {
                properties.set("Annotation" + i + "HasLink", false);
                properties.set("Annotation" + i + "LinkID", "");
                
            }
        }
        
        link.setObjectProperties(document.getId(), "Annotations", properties);
    }
    
    public DataAnnotationList read() throws APIInstantiationException, APIConnectException, APISecurityException, APIParseException{
        IPropertyList properties = link.getObjectProperties(document.getId(), "Annotations");
        annotations = new DataAnnotationList();
        DataAnnotation annotation;
        int size = properties.intValue("AnnotationCount", 0);
        int startRow;
        int endRow;
        String title;
        String text;
        String objectId;
        
        for(int i=0;i<size;i++){
            startRow = properties.intValue("Annotation" + i + "Start", 0);
            endRow = properties.intValue("Annotation" + i + "End", 0);
            text = properties.stringValue("Annotation" + i + "Text", "");
            title = properties.stringValue("Title", "");
            if(properties.booleanValue("Annotation" + i + "HasLink", false)){
                objectId = properties.stringValue("Annotation" + i + "LinkID", null);
            } else {
                objectId = null;
            }
            annotation = new DataAnnotation();
            annotation.setEndRow(endRow);
            annotation.setStartRow(startRow);
            annotation.setRelatedObjectId(objectId);
            annotation.setText(text);
            annotation.setTitle(title);
            annotations.addAnnotation(annotation);
        }
        return annotations;
    }
}