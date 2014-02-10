/*
 * WorkflowLockMember.java
 */

package com.connexience.server.model.workflow.notification;

import com.connexience.server.model.workflow.*;
import java.io.*;

/**
 * This class represents a member of a workflow invocation lock.
 * @author hugo
 */
public class WorkflowLockMember implements Serializable {
    /** Database ID */
    private long id;

    /** ID of the parent lock */
    private long lockId;

    /** ID of the invocation */
    private String invocationId;

    /** ID of the invocation folder */
    private String invocationFolderId;

    /** Invocation status */
    private int invocationStatus = WorkflowInvocationFolder.INVOCATION_WAITING;

    /** ID of the engine running this workflow */
    private String engineId;

    /** Start time of the workflow */
    private long startTime;

    public String getEngineId() {
        return engineId;
    }

    public void setEngineId(String engineId) {
        this.engineId = engineId;
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

    public int getInvocationStatus() {
        return invocationStatus;
    }

    public void setInvocationStatus(int invocationStatus) {
        this.invocationStatus = invocationStatus;
    }

    public long getLockId() {
        return lockId;
    }

    public void setLockId(long lockId) {
        this.lockId = lockId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getInvocationFolderId() {
        return invocationFolderId;
    }

    public void setInvocationFolderId(String invocationFolderId) {
        this.invocationFolderId = invocationFolderId;
    }
}