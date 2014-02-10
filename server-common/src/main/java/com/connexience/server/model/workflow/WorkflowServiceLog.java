/*
 * WorkflowServiceLog.java
 */

package com.connexience.server.model.workflow;

import java.io.*;

/**
 * This class contains the output from the service process after it has finished
 * executing. This is updated for each service invocation during a workflow run
 * so that intermediate results can be viewed in the editor.
 * @author nhgh
 */
public class WorkflowServiceLog implements Serializable {
    /** Service has not been executed yet */
    public static final String SERVICE_NOT_EXECUTED_YET = "notexecuted";
    
    /** Service executed Ok */
    public static final String SERVICE_EXECUTION_OK = "ok";
    
    /** Service executed with an error */
    public static final String SERVICE_EXECUTION_ERROR = "error";
    
    /** Log message ID */
    private long id;

    /** Workflow invocation ID */
    private String invocationId;

    /** Block context id */
    private String contextId;

    /** Message data */
    private String outputText;

    /** Status text */
    private String statusText = SERVICE_NOT_EXECUTED_YET;
    
    /** Status message */
    private String statusMessage = "";
    
    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInvocationId() {
        return invocationId;
    }

    public void setInvocationId(String invocationId) {
        this.invocationId = invocationId;
    }

    public String getOutputText() {
        return outputText;
    }

    public void setOutputText(String outputText) {
        this.outputText = outputText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
    
    
}