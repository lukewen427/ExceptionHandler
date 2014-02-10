/*
 * WorkflowInvocationFolderWrapper.java
 */

package com.connexience.server.workflow.xmlstorage;
import com.connexience.server.api.*;
import com.connexience.server.api.impl.*;
import org.pipeline.core.xmlstorage.*;
import java.io.*;

/**
 * This class wraps an invocation folder in a form suitable for passing over
 * Drawing RPC.
 * @author nhgh
 */
public class WorkflowInvocationFolderWrapper extends InkspotSecuredObject implements XmlStorable, Serializable, IWorkflowInvocation {
    /** Workflow is waiting */
    public static final int INVOCATION_WAITING = 0;

    /** Workflow is executing */
    public static final int INVOCATION_RUNNING = 1;

    /** Workflow has finished with no errors */
    public static final int INVOCATION_FINISHED_OK = 2;

    /** Workflow has finished but has errors */
    public static final int INVOCATION_FINISHED_WITH_ERRORS = 3;

    /** Workflow is waiting for a debugger on the current blocl */
    public static final int INVOCATION_WAITING_FOR_DEBUGGER = 4;
    
    /** Document ID */
    private String id;
    
    /** Document Name */
    private String name;
    
    /** DocumentDescription */
    private String description;
    
    /** Parent ID */
    private String containerId;
    
    /** OrganisationID */
    private String organisationId;
    
    /** Workflow ID for the folder */
    public String workflowId;
    
    /** Invocation ID for the folder */
    public String invocationId;

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
    
    /** Status message */
    private String message = "";
    
    /** ID of the engine running this workflow */
    private String engineId;
    
    public String getCurrentBlockId() {
        return currentBlockId;
    }

    public void setCurrentBlockId(String currentBlockId) {
        this.currentBlockId = currentBlockId;
    }

    public int getInvocationStatus() {
        return invocationStatus;
    }

    public void setInvocationStatus(int invocationStatus) {
        this.invocationStatus = invocationStatus;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }

    public String getInvocationId() {
        return invocationId;
    }

    public void setInvocationId(String invocationId) {
        this.invocationId = invocationId;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getStatus() {
        switch(invocationStatus){
            case INVOCATION_FINISHED_OK:
                return IWorkflowInvocation.WORKFLOW_FINISHED_OK;

            case INVOCATION_FINISHED_WITH_ERRORS:
                return IWorkflowInvocation.WORKFLOW_FINISHED_WITH_ERRORS;

            case INVOCATION_RUNNING:
                return IWorkflowInvocation.WORKFLOW_RUNNING;

            case INVOCATION_WAITING:
                return IWorkflowInvocation.WORKFLOW_WAITING;

            case INVOCATION_WAITING_FOR_DEBUGGER:
                return IWorkflowInvocation.WORKFLOW_WAITING_FOR_DEBUGGER;
                
            default:
                return IWorkflowInvocation.WORKFLOW_STATUS_UNKNOWN;
        }
    }

    public void setStatus(String status) {
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

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    
    
    @Override
    public String toString() {
        return name;
    }

    public String getEngineId() {
        return engineId;
    }

    public void setEngineId(String engineId) {
        this.engineId = engineId;
    }
        
    /** Store to XML data */
    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("Folder");
        store.add("ID", id);
        store.add("Name", name);
        store.add("Description", description);
        store.add("ContainerID", containerId);
        store.add("OrganisationID", organisationId);
        store.add("WorkflowID", workflowId);
        store.add("InvocationID", invocationId);
        store.add("InvocationStatus", invocationStatus);
        store.add("CurrentBlockID", currentBlockId);
        store.add("StreamingProgressKnown", streamingProgressKnown);
        store.add("TotalBytesToStream", totalBytesToStream);
        store.add("BytesStreamed", bytesStreamed);
        store.add("Message", message);
        store.add("EngineID", engineId);
        return store;
    }

    /** Recreate from XML data */
    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        id = store.stringValue("ID", null);
        name = store.stringValue("Name", "");
        description = store.stringValue("Description", "");
        containerId = store.stringValue("ContainerID", null);
        organisationId = store.stringValue("OrganisationID", null);
        workflowId = store.stringValue("WorkflowID", null);
        invocationId = store.stringValue("InvocationID", null);
        invocationStatus = store.intValue("InvocationStatus", INVOCATION_WAITING);
        currentBlockId = store.stringValue("CurrentBlockID", null);
        streamingProgressKnown = store.booleanValue("StreamingProgressKnown", false);
        totalBytesToStream = store.longValue("TotalBytesToStream", 0);
        bytesStreamed = store.longValue("BytesStreamed", 0);
        message = store.stringValue("Message", "");
        engineId = store.stringValue("EngineID", null);
    }    
}
