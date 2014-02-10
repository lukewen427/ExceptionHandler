/*
 * WorkflowLockWrapper.java
 */

package com.connexience.server.workflow.xmlstorage;

import org.pipeline.core.xmlstorage.*;
import java.io.*;
/**
 * This class provides a serializable form of a workflow lock.
 * @author hugo
 */
public class WorkflowLockWrapper implements Serializable, XmlStorable {
    public static String LOCK_FILLING = "filling";
    public static String LOCK_WAITING = "waiting";
    public static String LOCK_FINISHED = "finished";
    public static String LOCK_ERROR = "error";
    private long id;
    private String status;
    private String engineId;
    private String invocationId;
    private String userId;
    private String contextId;

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


    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("WorkflowLockWrapper");
        store.add("ID", id);
        store.add("InvocationID", invocationId);
        store.add("Status", status);
        store.add("UserID", userId);
        store.add("EngineID", engineId);
        store.add("ContextID", contextId);
        return store;
    }

    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        id = store.longValue("ID", 0);
        invocationId = store.stringValue("InvocationID", null);
        status = store.stringValue("Status", null);
        userId = store.stringValue("UserID", null);
        engineId = store.stringValue("EngineID", null);
        contextId = store.stringValue("ContextID", null);
    }
}