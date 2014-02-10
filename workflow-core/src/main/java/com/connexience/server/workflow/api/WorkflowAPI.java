/*
 * WorkflowAPI.java
 */

package com.connexience.server.workflow.api;

import com.connexience.server.api.*;
import com.connexience.server.model.workflow.control.WorkflowEngineStatusData;
import com.connexience.server.workflow.service.*;
import com.connexience.server.workflow.xmlstorage.WorkflowLockWrapper;
import java.util.*;

/**
 * This interface provides a workflow control API that the cloud workflow engine
 * can use to update the status of workflow runs. It is separate from the standard
 * API as it provides lower level functionality not relevant to external developers.
 * @author nhgh
 */
public interface WorkflowAPI {
    /** Save a workflow invocation folder back to the server */
    public IWorkflowInvocation saveWorkflowInvocation(IWorkflowInvocation invocation) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Set the current data streaming progress through a block */
    public void setCurrentBlockStreamingProgress(String invocationId, String contextId, long totalBytes, long bytesStreamed) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;
    
    /** Set the engine ID for an invocation ID */
    public void setInvocationEngineId(String invocationId, String engineId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Upload the output and error stream data for a service invocation */
    public void updateServiceLog(String invocationId, String contextId, String outputData, String statusText, String statusMessage) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Update just the message from a service log */
    public void updateServiceLogMessage(String invocationId, String contextId, String statusText, String statusMessage) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;
    
    /** Get a list of latest versions for a list of document IDs */
    public List<IDocumentVersion> getLatestVersions(List<String> documentIds) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get the service defintion for a service */
    public DataProcessorServiceDefinition getService(String serviceId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get the service definition for a specific version of a workflow service */
    public DataProcessorServiceDefinition getService(String serviceId, String versionId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Log the fact that a workflow has been taken off the queue */
    public void logWorkflowDequeued(String invocationId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Log the fact that a workflow has started executing */
    public void logWorkflowExecutionStarted(String invocationId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Log the fact that a workflow has completed */
    public void logWorkflowComplete(String invocationId, String status) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Create an empty invocation folder */
    public IWorkflowInvocation createInvocationFolder(String workflowId, String invocationId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get the time on the server */
    public Date getServerTime() throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Create a workflow lock object */
    public WorkflowLockWrapper createWorkflowLock(String invocationId, String contextId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Remove a workflow lock */
    public void removeWorkflowLock(long lockId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Set the status of a workflow lock */
    public void setWorkflowLockStatus(long lockId, String status) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;
    
    /** Execute a workflow and attach it to a lock */
    public IWorkflowInvocation executeWorkflow(IWorkflow workflow, IWorkflowParameterList parameters, long lockId, String folderName) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;
    
    /** Set the status of a workflow invocation */
    public void setWorkflowStatus(String invocationId, String workflowStatus) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;
    
    /** Set the current block of a workflow invocation */
    public void setCurrentBlock(String invocationId, String contextId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;
    
    /** Make the server send a workflow lock completion message if the specified lock is finished. This is used when engines are restarted */
    public void refreshLockStatus(long lockId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;
    
    /** Notify the server that the engine has started */
    public void notifyEngineStartup(String engineId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;
    
    /** Notify the server that an engine has shutdown */
    public void notifyEngineShutdown(String engineId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;
}