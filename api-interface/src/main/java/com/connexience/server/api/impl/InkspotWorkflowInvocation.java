/*
 * InkspotWorkflowInvocation.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;

/**
 * This class provides an implementation of the workflow invocation object
 * @author nhgh
 */
public class InkspotWorkflowInvocation extends InkspotFolder implements IWorkflowInvocation {

    public InkspotWorkflowInvocation() {
        super();
        putProperty("workflowid", "");
        putProperty("status", WORKFLOW_STATUS_UNKNOWN);
        putProperty("invocationid", "");
        putProperty("currentblockid", "");
        putProperty("message", "");
    }

    public String getWorkflowId() {
        return getPropertyString("workflowid");
    }

    public void setWorkflowId(String workflowId) {
        putProperty("workflowid", workflowId);
    }

    public String getStatus(){
        return getPropertyString("status");
    }

    public void setStatus(String status){
        putProperty("status", status);
    }

    public void setInvocationId(String invocationId) {
        putProperty("invocationid", invocationId);
    }

    public String getInvocationId(){
        return getPropertyString("invocationid");
    }

    public String getCurrentBlockId() {
        return getPropertyString("currentblockid");
    }

    public void setCurrentBlockId(String blockId) {
        if(blockId!=null){
            putProperty("currentblockid", blockId);
        } else {
            putProperty("currentblockid", "");
        }
    }

    public String getMessage() {
        return getPropertyString("message");
    }

    public void setMessage(String message) {
        putProperty("message", message);
    }

    

}