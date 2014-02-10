/*
 * WorkflowInvocationFolder.java
 */

package com.connexience.server.model.workflow;

import com.connexience.server.model.folder.*;
import java.util.*;

/**
 * This class extends the basic folder record to provide a store for all of the
 * outputs of a specific workflow invocation.
 * @author nhgh
 */
public class WorkflowInvocationFolder extends Folder {
    /** Workflow is waiting */
    public static final int INVOCATION_WAITING = 0;

    /** Workflow is executing */
    public static final int INVOCATION_RUNNING = 1;

    /** Workflow has finished with no errors */
    public static final int INVOCATION_FINISHED_OK = 2;

    /** Workflow has finished but has errors */
    public static final int INVOCATION_FINISHED_WITH_ERRORS = 3;
    
    /** Workflow is waiting for a debugger on the current block */
    public static final int INVOCATION_WAITING_FOR_DEBUGGER = 4;
    
    /** Invocation ID. This links into the workflow database */
    private String invocationId;
    
    /** ID of the workflow document associated with this invocation */
    private String workflowId;
    
    /** Version ID of the workflow document associated with this invocation */
    private String versionId;
    
    /** Timestamp of the invocation */
    private Date invocationDate = new java.util.Date();

    /** Time this invocation was started */
    private Date queuedTime = null;

    /** Time this invocation was taken from the queue */
    private Date dequeuedTime = null;

    /** Time this invocation was started */
    private Date executionStartTime = null;

    /** Time this invocation was completed */
    private Date executionEndTime = null;

    /** Status of execution */
    private int invocationStatus = INVOCATION_WAITING;

    /** ID of the block currently being executed */
    private String currentBlockId;

    /** Is the streaming progress known for the current block */
    private boolean streamingProgressKnown = false;

    /** Total number of bytes to stream */
    private long totalBytesToStream = 0;

    /** Total number of bytes streamed */
    private long bytesStreamed = 0;

    /** ID of the engine running the workflow */
    private String engineId;

    /** Status message */
    private String message;

    public WorkflowInvocationFolder(){
        super();
    }

    /** Get the status flag of this invocation */
    public int getInvocationStatus() {
        return invocationStatus;
    }

    /** Set the status flag of this invocation */
    public void setInvocationStatus(int invocationStatus) {
        this.invocationStatus = invocationStatus;
    }

    /** Get the id of the currently executing block */
    public String getCurrentBlockId() {
        return currentBlockId;
    }

    /** Set the id of the currently executing block */
    public void setCurrentBlockId(String currentBlockId) {
        this.currentBlockId = currentBlockId;
    }

    /** Get the id of the engine that is running this workflow */
    public String getEngineId() {
        return engineId;
    }

    /** Set the id of the engine that is running this workflow */
    public void setEngineId(String engineId) {
        this.engineId = engineId;
    }

    /** Get the ID of the invocation in the workflow database */
    public String getInvocationId() {
        return invocationId;
    }

    /** Set the ID of the invocation in the workflow database */
    public void setInvocationId(String invocationId) {
        this.invocationId = invocationId;
    }
    
    /** Get the workflow ID */
    public String getWorkflowId(){
        return workflowId;
    }
    
    /** Set the workflow ID */
    public void setWorkflowId(String workflowId){
        this.workflowId = workflowId;
    }
    
    /** Get the invocation timestamp */
    public Date getInvocationDate(){
        return invocationDate;
    }
    
    /** Set the invocation timestamp */
    public void setInvocationDate(Date newDate){
        if(newDate!=null){
            invocationDate = new java.util.Date(newDate.getTime());
        }
    }

    public void setBytesStreamed(long bytesStreamed) {
        this.bytesStreamed = bytesStreamed;
    }

    public long getBytesStreamed() {
        return bytesStreamed;
    }

    public boolean isStreamingProgressKnown() {
        return streamingProgressKnown;
    }

    public void setStreamingProgressKnown(boolean streamingProgressKnown) {
        this.streamingProgressKnown = streamingProgressKnown;
    }
    
    public void setTotalBytesToStream(long totalBytesToStream) {
        this.totalBytesToStream = totalBytesToStream;
    }

    public long getTotalBytesToStream() {
        return totalBytesToStream;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getQueuedTime() {
        return queuedTime;
    }

    public void setQueuedTime(Date queuedTime) {
        this.queuedTime = queuedTime;
    }

    public Date getDequeuedTime() {
        return dequeuedTime;
    }

    public void setDequeuedTime(Date dequeuedTime) {
        this.dequeuedTime = dequeuedTime;
    }

    public Date getExecutionStartTime() {
        return executionStartTime;
    }

    public void setExecutionStartTime(Date executionStartTime) {
        this.executionStartTime = executionStartTime;
    }

    public Date getExecutionEndTime() {
        return executionEndTime;
    }

    public void setExecutionEndTime(Date executionEndTime) {
        this.executionEndTime = executionEndTime;
    }
}