/*
 * DynamicWorkflowService.java
 */

package com.connexience.server.model.workflow;

import com.connexience.server.model.document.*;
/**
 * This class represents a dynamically deployed workflow service. It contains
 * details of the service category and the document record itself contains
 * a zip file in a standard format with a service.xml document within it. This
 * service.xml is extracted whenever the service is saved and is attached
 * to the document record as a ServiceXML object.
 * @author nhgh
 */
public class DynamicWorkflowService extends DocumentRecord {
    /** Service category */
    private String category;

    /** ID of the block project file that created this block if there is one */
    private String projectFileId;
    
    /** Get the service category */
    public String getCategory(){
        return category;
    }

    /** Set the service category */
    public void setCategory(String category){
        this.category = category;
    }
    
    /** Get the ID of the project file that created this block */
    public String getProjectFileId(){
        return this.projectFileId;
    }
    
    /** Set the ID of the project file that created this block */
    public void setProjectFileId(String projectFileId){
        this.projectFileId = projectFileId;
    }
}