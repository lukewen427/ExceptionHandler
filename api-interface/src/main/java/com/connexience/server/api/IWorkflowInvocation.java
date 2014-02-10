/*
 * IWorkflowInvocation.java
 */

package com.connexience.server.api;

/**
 * This class represents a single run of a workflow
 * @author nhgh
 */
public interface IWorkflowInvocation extends IFolder {
    /** Name to use in XML document */
    public static final String XML_NAME = "WorkflowInvocation";

    /** Workflow is waiting to start */
    public static final String WORKFLOW_WAITING = "Waiting";

    /** Workflow is running */
    public static final String WORKFLOW_RUNNING = "Running";

    /** Workflow has finished with no errors */
    public static final String WORKFLOW_FINISHED_OK = "FinishedOk";

    /** Workflow has finished, but contains errors */
    public static final String WORKFLOW_FINISHED_WITH_ERRORS = "FinishedWithErrors";

    /** Workflow is waiting for a debugger */
    public static final String WORKFLOW_WAITING_FOR_DEBUGGER = "DebugWait";
    
    /** Unknown status */
    public static final String WORKFLOW_STATUS_UNKNOWN = "Unknown";

    /** Get the Id of the workflow that was run */
    public String getWorkflowId();

    /** Set the Id of the workflow that was run */
    public void setWorkflowId(String workflowId);

    /** Set the invocation id of this run */
    public void setInvocationId(String invocationId);

    /** Get the workflow engine invocation ID for this run */
    public String getInvocationId();

    /** Get the workflow status */
    public String getStatus();

    /** Set the workflow status */
    public void setStatus(String status);

    /** Get the ID of the currently executing block */
    public String getCurrentBlockId();

    /** Set the ID of the currently executing block */
    public void setCurrentBlockId(String blockId);
    
    /** Get the workflow execution message */
    public String getMessage();
    
    /** Set the workflow execution message */
    public void setMessage(String message);
}