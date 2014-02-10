/*
 * WorkflowInvocationRecord.java
 */

package com.connexience.server.model.workflow.control;

import java.io.*;
import java.util.*;

/**
 * This class represents a single invocation on the workflow engine
 * @author nhgh
 */
public class WorkflowInvocationRecord implements Serializable {
    /** Invocation ID */
    private String invocationId;

    /** Time this invocation was started */
    private Date startTime;

    /** Has this invocation been started */
    private boolean running;

    /** PID value as recognised by the engine that is running the workflow */
    private long pid;

    /** ContextID of the current block */
    private String contextId;
    
    /** Debug port of the current block if there is one */
    private int currentBlockDebugPort = -1;
    
    /** Workflow name */
    private String workflowName;
    
    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }
   
    public String getInvocationId() {
        return invocationId;
    }

    public void setInvocationId(String invocationId) {
        this.invocationId = invocationId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public int getCurrentBlockDebugPort() {
        return currentBlockDebugPort;
    }

    public void setCurrentBlockDebugPort(int currentBlockDebugPort) {
        this.currentBlockDebugPort = currentBlockDebugPort;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }
    
    
}