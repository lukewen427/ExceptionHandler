/*
 * WorkflowLock.java
 */

package com.connexience.server.model.workflow.notification;

import java.io.*;

/**
 * This class provides a representation of a workflow lock that can wait
 * for the completion of multiple workflows. The lock contains details of
 * the workflow and invocation runing, the engine containing the workflow
 * and various status parameters for the child worklflows.
 * @author hugo
 */
public class WorkflowLock implements Serializable {
    public static String LOCK_FILLING = "filling";
    public static String LOCK_WAITING = "waiting";
    public static String LOCK_FINISHED = "finished";
    public static String LOCK_ERROR = "error";

    /** Database ID */
    private long id;

    /** Comments for this lock */
    private String comments = "";

    /** Invocation ID for the running workflow */
    private String invocationId;

    /** Folder id of the invocation */
    private String invocationFolderId;

    /** Context ID of the waiting block */
    private String contextId;

    /** ID of the engine running the top level workflow that is waiting for this lock */
    private String engineId;

    /** Status of this lock */
    private String status = LOCK_FILLING;

    /** Has the lock finished message been delivered */
    private boolean notificationDelivered = false;

    /** Number of attempts made to deliver notification */
    private int notificationAttempts = 0;

    /** Number of seconds that should be left between delivery attempts */
    private int minimumAttemptInterval = 10;

    /** Time of the last delivery attempt */
    private long lastDeliveryAttemptTime = 0;

    /** User the created this lock */
    private String userId;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

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

    public long getLastDeliveryAttemptTime() {
        return lastDeliveryAttemptTime;
    }

    public void setLastDeliveryAttemptTime(long lastDeliveryAttemptTime) {
        this.lastDeliveryAttemptTime = lastDeliveryAttemptTime;
    }

    public int getMinimumAttemptInterval() {
        return minimumAttemptInterval;
    }

    public void setMinimumAttemptInterval(int minimumAttemptInterval) {
        this.minimumAttemptInterval = minimumAttemptInterval;
    }

    public int getNotificationAttempts() {
        return notificationAttempts;
    }

    public void setNotificationAttempts(int notificationAttempts) {
        this.notificationAttempts = notificationAttempts;
    }

    public boolean isNotificationDelivered() {
        return notificationDelivered;
    }

    public void setNotificationDelivered(boolean notificationDelivered) {
        this.notificationDelivered = notificationDelivered;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public String getInvocationFolderId() {
        return invocationFolderId;
    }

    public void setInvocationFolderId(String invocationFolderId) {
        this.invocationFolderId = invocationFolderId;
    }
}