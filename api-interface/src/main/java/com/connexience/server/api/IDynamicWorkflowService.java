/*
 * IDynamicWorkflowService.java
 */

package com.connexience.server.api;

/**
 * This interface defines a dynamic workflow service object that can be dynamically
 * deployed to cloud workflow engine.
 * @author nhgh
 */
public interface IDynamicWorkflowService extends IDocument {

    public static final String XML_NAME = "DynamicWorkflowService";
    
    /** Get the service category */
    public String getCategory();

    /** Set the service category */
    public void setCategory(String category);
    
    /** Set the requested object ID */
    public void setRequestedObjectId(String requestedObjectId);
    
    /** Get the requested object ID */
    public String getRequestedObjectId();
}
