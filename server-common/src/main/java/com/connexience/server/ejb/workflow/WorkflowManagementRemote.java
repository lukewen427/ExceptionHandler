/*
 * WorkflowManagementRemote.java
 */

package com.connexience.server.ejb.workflow;

import com.connexience.server.model.workflow.*;
import com.connexience.server.model.security.*;
import com.connexience.server.model.document.*;
import com.connexience.server.*;
import com.connexience.server.model.workflow.control.WorkflowEngineRecord;

import javax.ejb.Remote;
import java.util.*;

/**
 * This interface defines the remote interface of the workflow management bean
 * @author nhgh
 */
@Remote
public interface WorkflowManagementRemote {
    /** List all workflows owned by a ticket */
    public List listWorkflows(Ticket ticket) throws ConnexienceException;
    
    /** Get a specific workflow document record */
    public WorkflowDocument getWorkflowDocument(Ticket ticket, String documentId) throws ConnexienceException;
    
    /** Save a specific workflow document record */
    public WorkflowDocument saveWorkflowDocument(Ticket ticket, WorkflowDocument document) throws ConnexienceException;
    
    /** Delete a specific workflow document record */
    public void deleteWorkflowDocument(Ticket ticket, String documentId) throws ConnexienceException;
    
    /** Upload some binary data for a workflow document record */
    public DocumentVersion uploadWorkflowDocumentData(Ticket ticket, String documentId, byte[] workflowDocumentData) throws ConnexienceException;
    
    /** Get the binary data for a workflow document record */
    public byte[] getLatestWorkflowDocumentData(Ticket ticket, String documentId) throws ConnexienceException;
    
    /** Get data for a specific workflow document version */
    public byte[] getWorkflowDocumentData(Ticket ticket, String documentId, String versionId) throws ConnexienceException;

    /** Create a WorkflowInvocationFolder for a workflow */
    public WorkflowInvocationFolder createInvocationFolder(Ticket ticket, String invocationId, String containerId, String workflowId) throws ConnexienceException;
    
    /** Get the folder associated with an invocation */
    public WorkflowInvocationFolder getInvocationFolder(Ticket ticket, String invocationId) throws ConnexienceException;

    /** Save changes to an invocation folder */
    public WorkflowInvocationFolder saveInvocationFolder(Ticket ticket, WorkflowInvocationFolder folder) throws ConnexienceException;

    /** Do a simple database update to set the progress status of a block */
    public void setInvocationStatus(Ticket ticket, String invocationId, String contextId, long bytesToStream, long bytesStreamed) throws ConnexienceException;
    
    /** Get the currently executing block */
    public String getCurrentBlockForInvocation(Ticket ticket, String invocationId) throws ConnexienceException;

    /** Get the status of an invocation */
    public int getInvocationStatus(Ticket ticket, String invocationId) throws ConnexienceException;

    /** Get all of the invocations associated with a workflow for a user */
    public List getInvocationFolders(Ticket ticket, String workflowId) throws ConnexienceException;

    /** List the available dynamic workflow services */
    public List listDynamicWorkflowServices(Ticket ticket) throws ConnexienceException;

    /** Save a new / updated dynamic workflow service */
    public DynamicWorkflowService saveDynamicWorkflowService(Ticket ticket, DynamicWorkflowService service) throws ConnexienceException;

    /** Get the service XML for a dynamic workflow service */
    public String getDynamicWorkflowServiceXML(Ticket ticket, String serviceId) throws ConnexienceException;

    /** Get the service XML for a specific version of a dynamic workflow service */
    public String getDynamicWorkflowServiceXML(Ticket ticket, String serviceId, String versionId) throws ConnexienceException;

    /** Shortcut for getting dynamic service xml and document version together This is used by the workflow engine and bypasses some checks */
    public Object[] getDynamicWorkflowServiceXmlAndVersion(Ticket ticket, String serviceId) throws ConnexienceException;
    
    /** Get the latest version of a dynamic workflow service by ID */
    public DynamicWorkflowService getDynamicWorkflowService(Ticket ticket, String serviceId) throws ConnexienceException;

    /** Update the service xml data for a service that has already been uploaded */
    public void updateServiceXml(Ticket ticket, String serviceId, String versionId) throws ConnexienceException;

    /** Update the service xml data for a service that has already been uploaded and change the category */
    public void updateServiceXml(Ticket ticket, String serviceId, String versionId, String newCategory) throws ConnexienceException;

    /** Parse data in the library xml data for a service that has already been uploaded */
    public void updateLibraryXml(Ticket ticket, String libraryId, String versionId) throws ConnexienceException;
    
    /** Save a dynamic workflow service library */
    public DynamicWorkflowLibrary saveDynamicWorkflowLibrary(Ticket ticket, DynamicWorkflowLibrary library) throws ConnexienceException;

    /** Get a dynamic workflow service library */
    public DynamicWorkflowLibrary getDynamicWorkflowLibrary(Ticket ticket, String id) throws ConnexienceException;

    /** Get a dynamic workflow service library by library name */
    public DynamicWorkflowLibrary getDynamicWorkflowLibraryByLibraryName(Ticket ticket, String libraryName) throws ConnexienceException;

    /** List all of the dynamic workflow libraries that a user has access to */
    public List listDynamicWorkflowLibraries(Ticket ticket) throws ConnexienceException;

    /** Set the dynamic workflow engine IP address for an invocation */
    public void setDynamicWorkflowEngineForInvocation(Ticket ticket, String invocationId, String engineId) throws ConnexienceException;

    /** List the workflow management engines. The optional queue argument returns all of the engines that are listening to a specific queue.
     * If this is null it returns all of the queues */
    public List listDynamicWorkflowEngines(Ticket ticket) throws ConnexienceException;

    /** Get a dynamic workflow engine by database Id */
    public WorkflowEngineRecord getDynamicWorkflowEngine(Ticket ticket, long id) throws ConnexienceException;
    
    /** Update the output data for a service invocation of a workflow */
    public void updateServiceLog(Ticket ticket, String invocationId, String contextId, String outputData, String statusText, String statusMessage) throws ConnexienceException;

    /** Update the message part of a service log */
    public void updateServiceLogMessage(Ticket ticket, String invocationId, String contextId, String statusText, String statusMessage) throws ConnexienceException;
    
    /** Terminate a workflow run */
    public WorkflowTerminationReport terminateInvocation(Ticket ticket, String invocationId) throws ConnexienceException;

    /** Get the service log for an block in an invocation */
    public WorkflowServiceLog getServiceLog(Ticket ticket, String invocationId, String contextId) throws ConnexienceException;

    /** Get all of the service logs for an invocation */
    public List getServiceLogs(Ticket ticket, String invocationId) throws ConnexienceException;
    
    /** Delete all of the service logs for an invocation */
    public void deleteServiceLogs(Ticket ticket, String invocationId) throws ConnexienceException;
    
    /** Get the help text for a workflow service */
    public BlockHelpHtml getDynamicServiceHelp(Ticket ticket, String serviceId, String versionId) throws ConnexienceException;

    /** List all of the users workflow invocations. Filter suceeded set to true to only show running / failed runs */
    public List getAllInvocationFolders(Ticket ticket, int filterStatus, boolean filterFinished) throws ConnexienceException;

    /** Get the serialized message for an invocation */
    public SerializedInvocationMessage getSerializedMessage(Ticket ticket, String invocationId) throws ConnexienceException;
    
    /** List all of the workflow servers that have been seen */
    public List listWorkflowEngineIds(Ticket ticket) throws ConnexienceException;
    
    /** Get the number of running workflows for a particular workflow */
    public int getNumberOfInvocations(Ticket ticket, String workflowId) throws ConnexienceException;
    
    /**
     * Get the workflow statistics for a partocular user that can be displayed on the front page
     * @param ticket Ticket for the user who is logged in
     * @return HashMap that has the status and number of workflows in that status
     * @throws com.connexience.server.ConnexienceException Something went wrong
     */
    HashMap<String, Long> getWorkflowStats(Ticket ticket) throws ConnexienceException;
    
    /** Get a trigger by id */
    public WorkflowFolderTrigger getWorkflowFolderTrigger(Ticket ticket, long triggerId) throws ConnexienceException;
    
    /** List all triggers for a folder */
    public List listFolderTriggers(Ticket ticket, String folderId) throws ConnexienceException;
    
    /** List the workflow folder triggers for a user */
    public List listUserTriggers(Ticket ticket, String userId) throws ConnexienceException;
    
    /** Save a workflow trigger */
    public WorkflowFolderTrigger saveWorkflowFolderTrigger(Ticket ticket, WorkflowFolderTrigger trigger) throws ConnexienceException;
    
    /** Delete a workflow trigger */
    public void deleteWorkflowFolderTrigger(Ticket ticket, long triggerId) throws ConnexienceException;
    
    /** Run triggers for a document that has just been saved to a folder */
    public void runTriggersForDocument(Ticket ticket, DocumentRecord document) throws ConnexienceException;
    
    /** Create a record for a workflow engine */
    public void createDyamicWorkflowEngineRecord(String engineId) throws ConnexienceException;
    
    /** Remove a record for a workflow engine */
    public void removeDyamicWorkflowEngineRecord(String engineId) throws ConnexienceException;

    public void replaceWorkflowServiceIds(Ticket ticket, WorkflowDocument workflow, HashMap<String, String> serviceIdsMap) throws ConnexienceException;

  public void updateWorkflowImage(Ticket ticket, String workflowId) throws ConnexienceException;

}