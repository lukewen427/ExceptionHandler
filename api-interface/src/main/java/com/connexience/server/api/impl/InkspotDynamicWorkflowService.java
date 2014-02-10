/*
 * InkspotDynamicWorkflowService.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;

/**
 * This class provides a standard implementation of a dynamic workflow service
 * object.
 * @author nhgh
 */
public class InkspotDynamicWorkflowService extends InkspotDocument implements IDynamicWorkflowService {

    public InkspotDynamicWorkflowService() {
        super();
        putProperty("category", "");
        putProperty("requestedobjectid", "");
    }

    /** Get the service category */
    public String getCategory() {
        return getPropertyString("category");
    }

    /** Set the service category */
    public void setCategory(String category) {
        putProperty("category", category);
    }

    /** Get the requested ID for saving this object */
    public String getRequestedObjectId() {
        return getPropertyString("requestedobjectid");
    }

    /** Set the requested ID for saving this object */
    public void setRequestedObjectId(String requestedObjectId) {
        putProperty("requestedobjectid", requestedObjectId);
    }
}