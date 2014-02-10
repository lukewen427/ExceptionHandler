/*
 * WorkflowInvocation.java
 */
package com.connexience.server.workflow.engine;

import com.connexience.server.workflow.blocks.processor.*;
import com.connexience.server.workflow.xmlstorage.*;
import com.connexience.server.workflow.service.*;
import com.connexience.server.workflow.api.*;
import com.connexience.server.workflow.engine.parameters.*;
import com.connexience.server.api.*;
import com.connexience.server.api.impl.*;
import com.connexience.server.api.util.*;
import com.connexience.server.model.security.Ticket;
import com.connexience.server.model.logging.events.WorkflowStatus;
import com.connexience.server.model.workflow.WorkflowInvocationMessage;
import com.connexience.server.model.workflow.WorkflowServiceLog;
import com.connexience.server.util.SerializationUtils;

import com.connexience.server.workflow.api.WorkflowAPIProvider;
import com.connexience.server.workflow.api.rpc.RPCClientApi;
import com.connexience.server.workflow.service.DataProcessorClientListener;
import org.pipeline.core.drawing.*;
import org.pipeline.core.drawing.spanning.*;
import org.pipeline.core.drawing.model.*;
import org.pipeline.core.xmlstorage.*;
import org.pipeline.core.xmlstorage.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.net.InetAddress;
import org.apache.log4j.*;

/**
 * This class manages the invocation of a Drawing object. It looks after the
 * persistence of data etc.
 * @author hugo
 */
public class WorkflowInvocation implements DrawingExecutionListener, Serializable, DataProcessorServiceFetcher, Runnable, XmlStorable {
    static Logger logger = Logger.getLogger(WorkflowInvocation.class);
    
    static {
        InkspotTypeRegistration.register();
    }
    /** Drawing being executed */
    private transient DrawingModel drawing = null;
    
    /** Drawing data */
    private XmlDataStore drawingData = null;
    
    /** Execution engine */
    private transient DrawingExecutionProcessor executor = null;
    
    /** Temporary data source */
    private InvocationDataSource dataSource;
    
    /** Listeners */
    private Vector<WorkflowInvocationListener> listeners = new Vector<WorkflowInvocationListener>();
    
    /** ID of this invocation */
    private String invocationId;
    
    /** ID of the workflow document */
    private String workflowId;
    
    /** ID of the workflow version */
    private String versionId;
    
    /** Invocation PID. This is used by the command line tools to provide an easy way
     * of manipulating invocations */
    private long pid = 0;
    
    /** Block Execution Reports */
    private Hashtable executionReports = new Hashtable();
    
    /** Parent workflow engine */
    private WorkflowEngine parent = null;
    
    /** Security ticket of calling user */
    private Ticket ticket = null;
    
    /** ID of the data file to process using the workflow */
    private String targetFileId = null;
    
    /** Name of the data target block */
    private String inputBlockName = null;
    
    /** Message plan for this invocation */
    private WorkflowInvocationMessagePlan messagePlan = new WorkflowInvocationMessagePlan();
    
    /** Currently running message item */
    private volatile WorkflowInvocationMessagePlan.MessageItem currentItem = null;
    
    /** List of invocation properties. These are used to store things like API clients as a workflow runs */
    private Hashtable<String, Object> invocationProperties = new Hashtable<String, Object>();
    
    /** API Link */
    private API apiLink;
    
    /** Workflow control api link */
    private WorkflowAPI controlApiLink;
    
    /** Has this invocation been killed */
    private boolean killFlag = false;

    /** Time this invocation was started */
    private Date startTime = null;

    /** Is this invocation running */
    private boolean running = false;
    
    /** Should the results data be stored in the workflow execution report. If this is false, the results
     * will be posted back directly to the database */
    private boolean reportUsedForCommandOutput = true;
    
    /** Parameter replacements */
    private XmlDataStore parameterReplacements = null;
    
    /** Workflow service EngineID */
    private String engineId = null;
    
    /** Cache of latest versions for a given document ID */
    private ConcurrentHashMap<String, String> latestVersionCache = new ConcurrentHashMap<String, String>();
    
    /** Cache of libraries by name */
    private ConcurrentHashMap<String, IDocument> libraryCache = new ConcurrentHashMap<String, IDocument>();
    
    /** Cache of services by ID */
    private ConcurrentHashMap<String, IDocument> serviceCache = new ConcurrentHashMap<String, IDocument>();
    
    /** Is this invocation waiting for a lock */
    private boolean waitingForLock = false;
    
    /** ID of the lock waiting for */
    private long lockId = 0;
    
    /** Should the invocation be deleted if successful */
    private boolean deletedOnSuccess = false;
    
    /** Only upload output data for failed blocks */
    private boolean onlyFailedOutputsUploaded = false;
    
    /** Service fetcher object */
    private DataProcessorServiceFetcher serviceFetcher = null;
    
    /** Was there an error during invocation */
    private boolean invocationFailed = false;
    
    /** Flag set when the workflow is being restarted after being suspended */
    private boolean resumeFlag = false;
    
    /** Name of the workflow */
    private String workflowName;
    
    /** Create an invocation by loading a drawing from storage */
    public WorkflowInvocation(XmlDataStore drawingData, Ticket ticket, WorkflowAPIProvider apiProvider, String invocationId) throws WorkflowInvocationException {
        drawing = new DefaultDrawingModel();
        this.drawingData = drawingData;
        this.ticket = ticket;
        this.invocationId = invocationId;
        serviceFetcher = this;
        try {

            ((DefaultDrawingModel) drawing).recreateObject(drawingData);
        } catch (XmlStorageException xmlse) {
            logger.error("Error parsing workflow data. InvocationID=" + invocationId, xmlse);
            throw new WorkflowInvocationException("Error reading drawing data");
        }
        executor = new DrawingExecutionProcessor(drawing);
        executor.addDrawingExecutionListener(this);

        try {
            apiLink = apiProvider.createApi(ticket);
        } catch (Exception e) {
            logger.error("Could not create API link. InvocationID=" + invocationId, e);
            throw new WorkflowInvocationException("Cannot create API link: " + e.getMessage());
        }

        try {
            controlApiLink = apiProvider.createWorkflowApi(ticket);
        } catch (Exception e) {
            logger.error("Could not create workflow control API link. InvocationID=" + invocationId, e);
            throw new WorkflowInvocationException("Cannot create workflow control API link: " + e.getMessage());
        }
    }

    /** Create a workflow invocation by loading state from a workflow invocation directory */
    public WorkflowInvocation(WorkflowAPIProvider apiProvider, File invocationDir) throws WorkflowInvocationException {
        // Load the workflow data file
        try {
            loadInvocationData(invocationDir);
        } catch (Exception e){
            throw new WorkflowInvocationException("Error loading invocation data: " + e.getMessage(), e);
        }
        
        try {
            apiLink = apiProvider.createApi(ticket);
        } catch (Exception e) {
            logger.error("Could not create API link. InvocationID=" + invocationId, e);
            throw new WorkflowInvocationException("Cannot create API link: " + e.getMessage());
        }

        try {
            controlApiLink = apiProvider.createWorkflowApi(ticket);
        } catch (Exception e) {
            logger.error("Could not create workflow control API link. InvocationID=" + invocationId, e);
            throw new WorkflowInvocationException("Cannot create workflow control API link: " + e.getMessage());
        }
                
        // Set up the drawing and all of the execution processors
        drawing = new DefaultDrawingModel();
        serviceFetcher = this;
        try {
            ((DefaultDrawingModel) drawing).recreateObject(drawingData);
        } catch (XmlStorageException xmlse) {
            logger.error("Error parsing workflow data. InvocationID=" + invocationId, xmlse);
            throw new WorkflowInvocationException("Error reading drawing data");
        }
        executor = new DrawingExecutionProcessor(drawing);
        executor.addDrawingExecutionListener(this);        
        
        // Set the resume flag
        resumeFlag = true;
    }
    
    public InvocationDataSource getDataSource(){
        return dataSource;
    }
    
    public boolean isInvocationFailed() {
        return invocationFailed;
    }

    public void setServiceFetcher(DataProcessorServiceFetcher serviceFetcher) {
        this.serviceFetcher = serviceFetcher;
    }

    public void setDeletedOnSuccess(boolean deletedOnSuccess) {
        this.deletedOnSuccess = deletedOnSuccess;
    }

    public boolean isDeletedOnSuccess() {
        return deletedOnSuccess;
    }

    public void setOnlyFailedOutputsUploaded(boolean onlyFailedOutputsUploaded) {
        this.onlyFailedOutputsUploaded = onlyFailedOutputsUploaded;
    }

    public boolean isOnlyFailedOutputsUploaded() {
        return onlyFailedOutputsUploaded;
    }

    /** This sets the ID of the service host containing the engine. If this
     * property is present, the engine will make a registration based upon
     * host ID as opposed to host IP. Otherwise an old style IP registration 
     * will be made.
     */
    public void setEngineId(String engineId) {
        this.engineId = engineId;
    }

    /** Get the workflow PID number */
    public long getPid() {
        return pid;
    }

    /** Set the workflow PID number */
    public void setPid(long pid) {
        this.pid = pid;
    }

    /** Get the ID of the workflow document */
    public String getWorkflowId() {
        return workflowId;
    }

    /** Set the ID of the workflow document */
    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    /** Set the ID of the workflow version */
    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    /** Set an invocation property */
    public void setInvocationProperty(String name, Object value) {
        invocationProperties.put(name, value);
    }

    /** Get an invocation property */
    public Object getInvocationProperty(String name) {
        return invocationProperties.get(name);
    }

    /** Clear an invocation property */
    public void clearInvocationProperty(String name) {
        invocationProperties.remove(name);
    }

    /** Set the name of the block that will be used to receive the data */
    public void setInputBlockName(String inputBlockName) {
        this.inputBlockName = inputBlockName;
    }

    /** Set the ID of the target file. This file will be passed to the specfied
     * input data block using the attribute name Source. It is the responsibility
     * of the workflow to check that the data type is correct */
    public void setTargetFileId(String targetFileId) {
        this.targetFileId = targetFileId;
    }

    /** Get the security ticket */
    public Ticket getTicket() {
        return ticket;
    }

    /** Get the drawing object */
    public DrawingModel getDrawing() {
        return drawing;
    }

    /** Get the drawing data */
    public XmlDataStore getDrawingData() {
        return drawingData;
    }

    /** Set the parent workflow engine */
    public void setParent(WorkflowEngine parent) {
        this.parent = parent;
        this.reportUsedForCommandOutput = parent.isReportUsedForCommandOutput();    // Set output properties
    }

    /** Get the invocation ID */
    public String getInvocationId() {
        return invocationId;
    }

    /** Set the invocation ID */
    public void setInvocationId(String invocationId) {
        this.invocationId = invocationId;
    }

    /** Add a listener */
    public void addWorkflowInvocationListener(WorkflowInvocationListener listener) {
        listeners.add(listener);
    }

    /** Remove a listener */
    public void removeWorkflowInvocationListener(WorkflowInvocationListener listener) {
        listeners.remove(listener);
    }

    /** Notify listeners that the execution has finished */
    private void notifyFinished() {
        Iterator<WorkflowInvocationListener> i = listeners.iterator();
        while (i.hasNext()) {
            i.next().executionFinished(this);
        }
    }

    /** Set the data source */
    public void setDataSource(InvocationDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /** Get the time that this invocation started */
    public Date getStartTime() {
        return startTime;
    }

    /** Is the XML workflow report used to contain the output stream from the service */
    public boolean isReportUsedForCommandOutput() {
        return reportUsedForCommandOutput;
    }

    /** Is this invocation currenly waiting for a lock */
    public boolean isWatitingForLock() {
        return waitingForLock;
    }

    /** Is the XML workflow report used to contain the output stream from the service */
    public void setReportUsedForCommandOutput(boolean reportUsedForCommandOutput) {
        this.reportUsedForCommandOutput = reportUsedForCommandOutput;
    }

    public void run(){
        logger.info("Starting workflow invocation thread. InvocationID=" + invocationId);
        // Set the invocation ID and data source in all of the blocks
        startTime = new Date();
        running = true;
        Enumeration blocks = drawing.blocks();
        Enumeration ports;
        PortModel port;
        
        // Save the invocation data
        saveInvocationData();

        parent.notifyInvocationStarted(this);
        
        BlockModel block;
        String serviceVersionId;
        IDocument serviceDoc;
        while (blocks.hasMoreElements()) {
            block = (BlockModel) blocks.nextElement();
            // Set the invocation ID
            if (block instanceof DataProcessorBlock) {
                DataProcessorBlock dpb = (DataProcessorBlock)block;     
                dpb.setInvocationId(invocationId);
                dpb.setGlobalDataStore(dataSource.getParent());
                dpb.setTicket(ticket);
                dpb.setMessagePlan(messagePlan);
                dpb.setServiceFetcher(serviceFetcher);
                dpb.setWorkflowId(workflowId);
                dpb.setWorkflowVersionId(versionId);
                
                // Get the latest version ID
                if(dpb.getUsesLatest()){
                    try {
                        serviceDoc = (IDocument)apiLink.createObject(IDocument.XML_NAME);
                        serviceDoc.setId(dpb.getServiceId());
                        serviceVersionId = getLatestDocumentVersionId(serviceDoc, apiLink);
                        if(serviceVersionId!=null){
                            dpb.setVersionId(serviceVersionId);
                            dpb.setUsesLatest(false);
                        }
                    } catch  (Exception e){
                        logger.error("Could not get latest version of service from cache: " + e.getMessage() + " InvocationID=" + invocationId);
                        running = false;
                        try {
                            controlApiLink.logWorkflowComplete(getInvocationId(), WorkflowStatus.FAILED);
                        } catch (Exception apie) {
                            logger.error("Error sending workflow status back to server. InvocationID=" + invocationId, apie);
                        }
                        finishExecution(true, "Could not download service code");
                        return;                        
                    }
                }
            }
        }

        // Set up the input data file if there is one and the drawing has
        // said that it can process an input data file
        if (targetFileId != null && inputBlockName != null) {
            block = getBlock(inputBlockName);
            if (block instanceof DefaultBlockModel) {
                DefaultBlockModel dbm = (DefaultBlockModel) block;
                if (dbm.getEditableProperties().propertyExists("Source")) {
                    try {
                        IDocument sourceRecord = apiLink.getDocument(targetFileId);
                        DocumentRecordWrapper docWrapper = new DocumentRecordWrapper();
                        docWrapper.setContainerId(sourceRecord.getContainerId());
                        docWrapper.setCreatorId(sourceRecord.getOwnerId());
                        docWrapper.setId(sourceRecord.getId());
                        docWrapper.setName(sourceRecord.getName());
                        dbm.getEditableProperties().add("Source", docWrapper);
                    } catch (Exception ce) {
                        running = false;
                        try {
                            controlApiLink.logWorkflowComplete(getInvocationId(), WorkflowStatus.FAILED);
                        } catch (Exception apie) {
                            logger.error("Error sending workflow status back to server. InvocationID=" + invocationId, apie);
                        }
                        logger.error("Error setting workflow input data details. InvocationID=" + invocationId, ce);
                        finishExecution(true, "Error setting workflow input data details: " + ce.getMessage());
                        return;
                    }

                } else {
                    try {
                        controlApiLink.logWorkflowComplete(getInvocationId(), WorkflowStatus.FAILED);
                    } catch (Exception apie) {
                        logger.error("Error sending workflow status back to server. InvocationID=" + invocationId, apie);
                    }
                    running = false;
                    finishExecution(true, "No suitable block property for input data on block: " + inputBlockName);
                    return;
                }
            }
        }

        // Save the drawing data into the invocation folder
        IWorkflowInvocation folder = null;
        IDocument workflowDoc = null;
        IDocument savedDoc = null;
        try {
            // Update the drawing data to reflect changes to the block data
            drawingData = ((DefaultDrawingModel) drawing).storeObject();

            folder = apiLink.getWorkflowInvocation(invocationId);

            // Set the workflow engine running this invocation
            controlApiLink.setInvocationEngineId(invocationId, engineId);

            // Set the invocation status
            folder.setStatus(IWorkflowInvocation.WORKFLOW_RUNNING);
            controlApiLink.saveWorkflowInvocation(folder);

        } catch (Exception e) {
            logger.error("Error starting and initialising the workflow data on the server. InvocationID=" + invocationId);
            try {
                controlApiLink.logWorkflowComplete(getInvocationId(), WorkflowStatus.FAILED);
            } catch (Exception apie) {
                logger.error("Error sending workflow status back to server. InvocationID=" + invocationId);
            }
            running = false;
            finishExecution(true, "Exception sending workflow information to server: " + e.getMessage());
            return;
        }
        
        // Run the drawing
        try {
            messagePlan.clear();

            // Builds a mesage plan. When this has finished messages will start being sent
            logger.info("Running workflow to build message plan. InvocationID=" + invocationId);
            executor.executeFromAllSourceBlocks();

        } catch (Exception e) {
            logger.error("Error creating workflow message plan. InvocationID=" + invocationId);
            try {
                controlApiLink.logWorkflowComplete(getInvocationId(), WorkflowStatus.FAILED);
            } catch (Exception apie) {
                logger.error("Error sending worklfow status back to server. InvocationID=" + invocationId);
            }
            finishExecution(true, "Error setting up drawing for execution: " + e.getMessage());
            return;
        }        
    }
    
    /** start executing the drawing */
    public void start() {
        new Thread(this).run();
    }

    /** Send the next message in the message plan. Returns true if a message was sent, or
     * false if there are no more messages left to send */
    private boolean sendNextMessage() {
        
        final WorkflowInvocationMessagePlan.MessageItem item = messagePlan.pop();
        if (item != null && killFlag == false) {
            // Send the message
            currentItem = item;
            saveInvocationState();
            
            // Add a listener to the client the process the message response
            item.getClient().addListener(new DataProcessorClientListener() {

                /** Server accepted the message */
                public void messageRecieved() {
                    item.setStatus(WorkflowInvocationMessagePlan.MESSAGE_RECEIVED);
                    item.getClient().removeListener(this);
                }

                /** Server would not process the message */
                public void messageRejected(String errorMessage) {
                    item.setStatus(WorkflowInvocationMessagePlan.MESSAGE_REJECTED);
                    item.getClient().removeListener(this);
                    currentItem = null;
                }
            });

            try {
                controlApiLink.setCurrentBlock(invocationId, item.getMessage().getContextId());
            } catch (Exception e){
                logger.error("Error sending current block ID. InvocationID=" + item.getMessage().getInvocationId(), e);
            }
            
            try {
                logger.info("Sending invocation message. InvocationID=" + item.getMessage().getInvocationId() + " BlockID=" + item.getMessage().getContextId());
                item.getClient().invoke(item.getMessage());
                item.setStatus(WorkflowInvocationMessagePlan.MESSAGE_SENT);
                
            } catch (Exception e) {
                logger.error("Error sending invocation message. InvocationID=" + item.getMessage().getInvocationId(), e);
                item.setStatus(WorkflowInvocationMessagePlan.MESSAGE_TRANSMISSION_ERROR);
                return false;
            }

            return true;

        } else {
            if (killFlag == true) {
                try {
                    controlApiLink.logWorkflowComplete(getInvocationId(), WorkflowStatus.KILLED);
                } catch (Exception apie) {
                    logger.error("Error sending workflow status back to server. InvocationID=" + item.getMessage().getInvocationId(), apie);
                }
            }
            return false;
        }
    }

    /** Processor service for a block has returned a completion message. This tells the
    block that it can return from its execute method */
    public void processorFinished(DataProcessorResponseMessage response) {
        logger.info("Workflow invocation received a data processor completion response message. InvocationID=" + response.getInvocationId() + " BlockID=" + response.getContextId());
        WorkflowInvocationMessagePlan.MessageItem message = messagePlan.getMessageForContextId(response.getContextId());
        boolean resent = false;
        
        // Did the service timeout 
        if(response.getStatus()==DataProcessorResponseMessage.SERVICE_TIMEOUT){
            logger.info("Service terminated due to timeout. InvocationID=" + response.getInvocationId() + " BlockID=" + response.getContextId());
            if(message.isOkToRetry()){
                if(message.getRemainingRetries()>0){
                    logger.info("Resending message to service. Retries left: " + message.getRemainingRetries() + " InvocationID=" + response.getInvocationId() + " BlockID=" + response.getContextId());
                    message.setRemainingRetries(message.getRemainingRetries() - 1);
                    messagePlan.pushBack(message);
                    resent = true;
                } else {
                    logger.info("Not resending message. Run out of retries. InvocationID=" + response.getInvocationId() + " BlockID=" + response.getContextId());
                    message.setStatusText("Service timed out and retry limit reached");
                    resent = false;
                }
            } else {
                logger.info("Service not allowed to retry. InvocationID=" + response.getInvocationId() + " BlockID=" + response.getContextId());
            }
        }
        
        if(!resent){
            // Set the correct status flag in the message plan
            message.setCommandOutput(response.getCommandOutput());
            if (response.getStatus() == DataProcessorResponseMessage.SERVICE_EXECUTION_OK) {
                message.setStatus(WorkflowInvocationMessagePlan.MESSAGE_PROCESSING_COMPLETED_OK);
                message.setStatusText("");
            } else {
                message.setStatus(response.getStatus());
                message.setStatusText(response.getStatusMessage());
                invocationFailed = true;
            }

            message.setCommandOutput(response.getCommandOutput());  // Copy output data

            // Send the output data back to the server via the API link
            if (reportUsedForCommandOutput == false) {
                boolean upload = true;

                if (onlyFailedOutputsUploaded && message.getStatus() == WorkflowInvocationMessagePlan.MESSAGE_PROCESSING_COMPLETED_OK) {
                    upload = false;
                }

                if (upload) {
                    try {
                        String statusText;
                        if(response.getStatus()==DataProcessorResponseMessage.SERVICE_EXECUTION_OK){
                            statusText = WorkflowServiceLog.SERVICE_EXECUTION_OK;
                        } else {
                            statusText = WorkflowServiceLog.SERVICE_EXECUTION_ERROR;
                        }

                        controlApiLink.updateServiceLog(response.getInvocationId(), response.getContextId(), response.getCommandOutput(), statusText, message.getStatusText());
                    } catch (Exception e) {
                        logger.error("Error sending block debugging data back to server. InvocationID=" + message.getMessage().getInvocationId(), e);
                        message.setCommandOutput("Error sending debugging data: " + e.getMessage());
                        message.setStatusText(message.getStatusText() + " (+ Error sending output)");
                    }
                } else {
                    try {
                        String statusText;
                        if(response.getStatus()==DataProcessorResponseMessage.SERVICE_EXECUTION_OK){
                            statusText = WorkflowServiceLog.SERVICE_EXECUTION_OK;
                        } else {
                            statusText = WorkflowServiceLog.SERVICE_EXECUTION_ERROR;
                        }

                        controlApiLink.updateServiceLog(response.getInvocationId(), response.getContextId(), "", statusText, message.getStatusText());
                    } catch (Exception e) {
                        logger.error("Error sending block debugging data back to server. InvocationID=" + message.getMessage().getInvocationId(), e);
                        message.setCommandOutput("Error sending debugging data: " + e.getMessage());
                        message.setStatusText(message.getStatusText() + " (+ Error sending output)");
                    }                
                }
            }
        }

        // Send the next message if possible. If there are no more left, then finish
        // up the execution process.
        currentItem = null;

        // Send next message or sleep waiting for the lock to be released
        if (response.isWaitingForLock() && resent==false) {
            logger.info("Workflow is waiting for a lock to be release. InvocationID=" + getInvocationId());
            lockId = response.getLockId();
            waitingForLock = true;
        } else {
            waitingForLock = false;
            lockId = 0;
            logger.info("Checking to see if there are more messages to be sent. InvocationID=" + getInvocationId());
            if (!sendNextMessage()) {
                logger.info("No more messages to be sent. Finishing the workflow invocation. InvocationID=" + getInvocationId());
                finishExecution(false, "");
            }
        }
    }

    /** Resume processing after a lock */
    public void resumeAfterLock(String contextId, long lockId, int failedWorkflowCount) {
        if (waitingForLock) {
            if (this.lockId == lockId) {
                waitingForLock = false;
                if(failedWorkflowCount==0){
                    if (!sendNextMessage()) {
                        finishExecution(false, "");
                    }
                } else {
                    try {
                        controlApiLink.updateServiceLogMessage(invocationId, contextId, "error", failedWorkflowCount + " child workflows contained execution errors");
                    } catch (Exception e){
                        logger.error("Error updating status message for block after lock returned failed invocations", e);
                    }
                    finishExecution(true, failedWorkflowCount + " child workflows contained execution errors");
                }
            }
        }
    }

    /** Get the currently executing item */
    public WorkflowInvocationMessagePlan.MessageItem getCurrentItem() {
        return currentItem;
    }
    
    /** Get the current block ID */
    public String getCurrentContextId(){
        return currentItem.getMessage().getContextId();
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }
    
    
    /** Do any of the execution reports contain an error */
    private boolean isExectionErrorPresent() {
        Enumeration reports = executionReports.elements();
        BlockExecutionReport report;
        while (reports.hasMoreElements()) {
            if (((BlockExecutionReport) reports.nextElement()).getExecutionStatus() != BlockExecutionReport.NO_ERRORS) {
                return true;
            }
        }
        return false;
    }

    /** Finish off the workflow execution */
    public synchronized void finishExecution(boolean error, String message) {
        logger.info("Finishing workflow invocation. Error=" + error + " InvocationID=" + invocationId);
        
        // Clean up the data store
        invocationFailed = error;
        dataSource.emptyDirectory();

        // Store the execution reports
        executionReports = executor.getExecutionReports();
        Enumeration reports = executionReports.elements();
        IWorkflowInvocation folder = null;
        BlockExecutionReport report;
        // Check exectution reports agains message plan and store any errors
        while (reports.hasMoreElements()) {
            report = (BlockExecutionReport) reports.nextElement();

            if (report != null) {
                // Try and set the output data in the report
                WorkflowInvocationMessagePlan.MessageItem item = messagePlan.getMessageForContextId(report.getBlockGuid());
                if (item != null && reportUsedForCommandOutput) {
                    report.setCommandOutputStored(true);
                    report.setCommandOutput(item.getCommandOutput());
                } else {
                    report.setCommandOutputStored(false);
                }

                // Set the status in the report
                if (item.getStatus() == WorkflowInvocationMessagePlan.MESSAGE_PROCESSING_COMPLETED_OK) {
                    report.setExecutionStatus(BlockExecutionReport.NO_ERRORS);

                } else if (item.getStatus() == WorkflowInvocationMessagePlan.MESSAGE_PROCESSING_COMPLETED_WITH_ERRORS) {
                    report.setExecutionStatus(BlockExecutionReport.INTERNAL_ERROR);
                    report.setAdditionalMessage(item.getStatusText());

                } else if (item.getStatus() == WorkflowInvocationMessagePlan.MESSAGE_REJECTED) {
                    report.setExecutionStatus(BlockExecutionReport.INTERNAL_ERROR);
                    report.setAdditionalMessage("Call message rejected: " + item.getStatusText());

                } else if (item.getStatus() == WorkflowInvocationMessagePlan.MESSAGE_TRANSMISSION_ERROR) {
                    report.setExecutionStatus(BlockExecutionReport.INTERNAL_ERROR);
                    report.setAdditionalMessage("Message transmission error: " + item.getStatusText());

                } else {
                    report.setExecutionStatus(BlockExecutionReport.INTERNAL_ERROR);
                    report.setAdditionalMessage("Unspecified error: " + item.getStatusText());

                }
            } else {
                System.out.println("*** WARNING: Missing block report ***");
            }
        }
               
        try {
            folder = apiLink.getWorkflowInvocation(invocationId);
        } catch (Exception e) {
            logger.error("Error getting workflow invocation folder. InvocationID=" + invocationId, e);
        }
        
        if(folder!=null){
            try {
                if(error){
                    // Some kind of setup error
                    logger.info("Invocation finished with an error: " + message + " InvocationID=" + invocationId);
                    folder.setStatus(IWorkflowInvocation.WORKFLOW_FINISHED_WITH_ERRORS);
                    folder.setMessage(message);
                    folder = controlApiLink.saveWorkflowInvocation(folder);
                    
                } else {
                    // Set the status
                    if (isExectionErrorPresent()) {
                        folder.setStatus(IWorkflowInvocation.WORKFLOW_FINISHED_WITH_ERRORS);
                        folder.setMessage("Error within workflow");
                    } else {
                        folder.setStatus(IWorkflowInvocation.WORKFLOW_FINISHED_OK);
                    }
                    folder = controlApiLink.saveWorkflowInvocation(folder);

                    if (isExectionErrorPresent()) {
                        try {
                            controlApiLink.logWorkflowComplete(getInvocationId(), WorkflowStatus.FAILED);
                        } catch (Exception apie) {
                            logger.error("Error sending workflow status back to server. InvocationID=" + invocationId, apie);
                        }
                    } else {
                        try {
                            controlApiLink.logWorkflowComplete(getInvocationId(), WorkflowStatus.SUCCESS);
                        } catch (Exception apie) {
                            logger.error("Error sending workflow status back to server. InvocationID=" + invocationId, apie);
                        }
                    }
                }

            } catch (Exception e) {
                logger.error("Error saving workflow status details. InvocationID=" + invocationId, e);
            }

        } else {
            logger.error("No invocation folder found on server. InvocationID=" + invocationId);
        }
        
        // Notify listeners
        running = false;
        notifyFinished();

        // Remove from the parent
        parent.invocationFinished(this);

        // Delete the invocation folder if needed
        if (deletedOnSuccess && folder != null) {
            if (!isExectionErrorPresent()) {
                try {
                    apiLink.deleteFolder(folder);
                } catch (Exception e) {
                    logger.error("Error deleting workflow invocation folder on server. InvocationID=" + invocationId, e);
                }
            }
        }
        // Close the API links
        parent.getAPIProvider().release(apiLink);
        parent.getAPIProvider().release(controlApiLink);
    }

    /** Is this invocation executing */
    public boolean isRunning() {
        return running;
    }

    /** Block has finished executing */
    public void blockExecutionFinished(BlockModel block) {
    }

    /** Block has started executing */
    public void blockExecutionStarted(BlockModel block) {
    }

    /** Drawing has finished. This means that the message plan is complete, so messages can start being sent */
    public void drawingExecutionFinished(DrawingModel drawing) {
        logger.info("Message plan creation finished. InvocationID=" + invocationId);
        if(isExectionErrorPresent()){
            logger.error("Error detected creating message plan. InvocationID=" + invocationId);
            finishExecution(true, "Error setting up drawing for execution");
        } else {
            logger.info("Starting to send service call messages. InvocationID=" + invocationId);

            try {
                controlApiLink.logWorkflowExecutionStarted(invocationId);
            } catch (Exception e) {
                logger.error("Error sending status to server", e);
            }

            // Should we resume execution from a previous run
            if(resumeFlag==true && invocationStateExists()){
                // Load the drawing state
                try {
                    XmlDataStore state = loadInvocationState();
                    waitingForLock = state.booleanValue("WaitingForLock", false);
                    lockId = state.longValue("LockID", 0);
                    invocationFailed = state.booleanValue("InvocationFailed", false);
                    killFlag = state.booleanValue("KillFlag", false);
                    if(state.booleanValue("HasCurrentItem", false)){
                        String contextId = state.stringValue("CurrentContextID", null);
                        if(contextId!=null){
                            // Pop messages off the queue until we reach the current message
                            messagePlan.moveToContextId(contextId);
                        }
                    }
                    
                } catch (Exception e){
                    logger.error("Error loading drawing state for resume. InvocationID=" + invocationId);
                    finishExecution(true, "Error loading invocation state");
                }
            }
            
            // Check lock status
            if(resumeFlag==false){
                if(!sendNextMessage()) {
                    finishExecution(false, "");
                }               
                
            } else {
                if(!waitingForLock){
                    // Not waiting for lock, run as normal
                    if (!sendNextMessage()) {
                        finishExecution(false, "");
                    }
                } else {
                    // Ask the server to send a lock completed message if the lock has finished
                    logger.info("Resumed invocation is waititng for a lock. Requesting update from server. InvocationID=" + invocationId);
                    try {
                        controlApiLink.refreshLockStatus(lockId);
                    } catch (Exception e){
                        logger.error("Error requesting lock status update from server. InvocationID=" + invocationId, e);
                    }
                }
            }
        }
    }

    /** Drawing execution has begun */
    public void drawingExecutionStarted(DrawingModel drawing) {
    }

    /** Get all of the execution reports */
    public Hashtable getExecutionReports() {
        return executionReports;
    }

    /** Get the exection report for a block */
    public BlockExecutionReport getExecutionReport(String blockId) {
        if (executionReports.containsKey(blockId)) {
            return (BlockExecutionReport) executionReports.get(blockId);
        } else {
            return null;
        }
    }

    /** Get a block by name */
    public BlockModel getBlock(String name) {
        Enumeration blocks = drawing.blocks();
        BlockModel block;
        while (blocks.hasMoreElements()) {
            block = (BlockModel) blocks.nextElement();
            if (block.getName().equals(name)) {
                return block;
            }
        }
        return null;
    }

    /** Get the latest version of a service definition */
    public DataProcessorServiceDefinition getServiceDefinition(String serviceId) throws DataProcessorException {
        try {
            return controlApiLink.getService(serviceId);
        } catch (Exception e) {
            logger.error("Error getting service definition data. ServiceID=" + serviceId + " InvocationID=" + invocationId);
            throw new DataProcessorException("Error getting service defintion: " + e.getMessage(), e);
        }
    }

    /** Get a specific version of a service definition */
    public DataProcessorServiceDefinition getServiceDefinition(String serviceId, String versionId) throws DataProcessorException {
        try {
            return controlApiLink.getService(serviceId, versionId);
        } catch (Exception e) {
            logger.error("Error getting service definition data. ServiceID=" + serviceId + " v " + versionId + " InvocationID=" + invocationId);
            throw new DataProcessorException("Error getting service defintion: " + e.getMessage(), e);
        }
    }

    /** Kill this workflow invocation */
    public void kill() {
        logger.info("Killing workflow. InvocationID=" + invocationId);
        killFlag = true;
        WorkflowInvocationMessagePlan.MessageItem itemToKill = currentItem;
        if (itemToKill != null) {
            DataProcessorClient client = itemToKill.getClient();
            DataProcessorCallMessage message = itemToKill.getMessage();
            try {
                client.terminate(message);
            } catch (Exception e) {
                logger.info("Error sending workflow termination message. InvocationID=" + invocationId, e);
            }
        }
        logger.info("Forcing workflow finish routine. InvocationID=" + invocationId);
        finishExecution(true, "Workflow Killed");
        
    }
    
    /** Suspend this invocation and stop running */
    public void suspend(){
        // Set status to suspended
        
        // Kill the current block if there is one
        
        // Push the current message back onto the queue
        
        // Flush the latest version and library caches
        
        
    }

    /** Save the state of the invocation to the invocation directory */
    public void saveInvocationState() {
        try {
            File stateFile = new File(dataSource.getStorageDir(), "_invocationState.xml");
            XmlDataStore state = new XmlDataStore("InvocationState");
            WorkflowInvocationMessagePlan.MessageItem ci = currentItem;
            
            // Save some essential data to allow resuming
            state.add("Running", running);
            state.add("WaitingForLock", waitingForLock);
            state.add("LockID", lockId);
            state.add("InvocationFailed", invocationFailed);
            state.add("Killed", killFlag);
            
            if(ci!=null){
                state.add("HasCurrentItem", true);
                state.add("CurrentContextID", ci.getContextId());
            } else {
                state.add("HasCurrentItem", false);
                state.add("CurrentContextID", "");
            }

            XmlFileIO writer = new XmlFileIO(state);
            writer.writeFile(stateFile);
        } catch (Exception e){
            logger.error("Error saving invocation state: " + e.getMessage());
        }
    }
    
    /** Load the invocation state */
    public XmlDataStore loadInvocationState() throws Exception {
        File stateFile = new File(dataSource.getStorageDir(), "_invocationState.xml");
        XmlFileIO reader = new XmlFileIO(stateFile);
        return reader.readFile();
    }
    
    /** Does the invocation state file exist */
    public boolean invocationStateExists() {
        File stateFile = new File(dataSource.getStorageDir(), "_invocationState.xml");
        return stateFile.exists();
    }
    
    /** Save all of the invocation data */
    public void saveInvocationData() {
        try {
            File dataFile = new File(dataSource.getStorageDir(), "_invocationData.xml");
            XmlFileIO writer = new XmlFileIO(storeObject());
            writer.writeFile(dataFile);
        } catch (Exception e){
            logger.error("Error saving invocation data: " + e.getMessage());
        }
    }
    
    /** Load invocation data from a directory */
    public void loadInvocationData(File sourceDir) throws Exception {
        File dataFile = new File(sourceDir, "_invocationData.xml");
        if(dataFile.exists()){
            XmlFileIO reader = new XmlFileIO(dataFile);
            recreateObject(reader.readFile());
        } else {
            throw new Exception("Cannot locate invocation data file");
        }
    }
    
    /** Parse a set of replacement properties as a byte[] array representation of an IWorkflowParameterList object */
    public void parseReplacementParameterXmlData(byte[] parameterData) throws WorkflowInvocationException {
        try {
            ByteArrayInputStream inStream = new ByteArrayInputStream(parameterData);
            List<IObject> objects = ObjectBuilder.parseInputStream(inStream);
            if (objects.size() == 1 && objects.get(0) instanceof IWorkflowParameterList) {
                IWorkflowParameterList parameterList = (IWorkflowParameterList) objects.get(0);
                IWorkflowParameter parameter;

                parameterReplacements = new XmlDataStore("ReplacementParameters");
                XmlDataStore blockParameters;
                for (int i = 0; i < parameterList.size(); i++) {
                    parameter = parameterList.getParameter(i);
                    if (parameterReplacements.containsName(parameter.getBlockName())) {
                        blockParameters = parameterReplacements.xmlDataStoreValue(parameter.getBlockName());
                    } else {
                        blockParameters = new XmlDataStore(parameter.getBlockName());
                        parameterReplacements.add(parameter.getBlockName(), blockParameters);
                    }
                    blockParameters.add(parameter.getName(), parameter.getValue());
                }

            } else {
                parameterReplacements = null;
                throw new Exception("Data does not contain a parameter list");
            }
        } catch (Exception e) {
            parameterReplacements = null;
            throw new WorkflowInvocationException("Error parsing parameter data: " + e.getMessage(), e);
        }
    }

    /** Replace the drawing parameters with the replacement parameter set */
    public void replaceWorkflowParameters() throws WorkflowInvocationException {
        try {
            if (drawing != null && parameterReplacements != null) {
                DrawingParameterReplacer replacer = new DrawingParameterReplacer(drawing, apiLink);
                replacer.replaceParameters(parameterReplacements);
            }
        } catch (Exception e) {
            throw new WorkflowInvocationException("Error replacing workflow parameters: " + e.getMessage(), e);
        }
    }

    /** Clear the parameter replacement object */
    public void clearReplacementParameters() {
        parameterReplacements = null;
    }

    /** Get a service by id. This method checks the cache to see if it is there first */
    public IDocument getServiceById(String serviceId, API api) throws Exception {
        if (serviceCache.containsKey(serviceId)) {
            return serviceCache.get(serviceId);
        } else {
            // Need to fetch from server
            IDocument serviceDoc = api.getDocument(serviceId);
            if (serviceDoc != null) {
                serviceCache.put(serviceId, serviceDoc);
                return serviceDoc;
            } else {
                return null;
            }
        }
    }

    /** Get a workflow library by name */
    public IDocument getLibraryByName(String libraryName, API api) throws Exception {
        if (libraryCache.containsKey(libraryName)) {
            return libraryCache.get(libraryName);
        } else {
            // Need to fetch from server
            IDocument libraryDoc = apiLink.getDynamicWorkflowLibraryByName(libraryName);
            if (libraryDoc != null) {
                libraryCache.put(libraryName, libraryDoc);
                return libraryDoc;
            } else {
                return null;
            }
        }
    }

    /** Get the latest version of a document */
    public String getLatestDocumentVersionId(IDocument doc, API api) throws Exception {
        if (latestVersionCache.containsKey(doc.getId())) {
            return latestVersionCache.get(doc.getId());
        } else {
            // Need to fetch latest document from the server
            String latestVersionId = api.getLatestVersionId(doc);
            if(latestVersionId!=null){
                latestVersionCache.put(doc.getId(), latestVersionId);
                return latestVersionId;
            } else {
                return null;
            }
        }
    }

    @Override
    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("WorkflowInvocation");
        store.add("DrawingData", drawingData);
        store.add("DeletedOnSuccess", deletedOnSuccess);
        store.add("EngineID", engineId);
        store.add("InputBlockName", inputBlockName);
        store.add("InvocationFailed", invocationFailed);
        store.add("InvocationID", invocationId);
        store.add("KillFlag", killFlag);
        store.add("LockID", lockId);
        store.add("OnlyFailedOutputsUploaded", onlyFailedOutputsUploaded);
        store.add("ReportUsedForCommandOutput", reportUsedForCommandOutput);
        store.add("Running", running);
        store.add("StartTime", startTime);
        store.add("TargetFileID", targetFileId);
        try {
            store.add("Ticket", SerializationUtils.serialize(ticket));
        } catch (IOException ioe){
            logger.error("Error saving ticket data: " + ioe.getMessage());
        }
        store.add("VersionID", versionId);
        store.add("WaitingForLock", waitingForLock);
        store.add("WorkflowID", workflowId);
        return store;
    }

    @Override
    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        drawingData = store.xmlDataStoreValue("DrawingData");
        deletedOnSuccess = store.booleanValue("DeletedOnSuccess", false);
        engineId = store.stringValue("EngineID", null);
        inputBlockName = store.stringValue("InputBlockName", null);
        invocationFailed = store.booleanValue("InvocationFailed", false);
        invocationId = store.stringValue("InvocationID", null);
        killFlag = store.booleanValue("KillFlag", false);
        lockId = store.longValue("LockID", 0);
        onlyFailedOutputsUploaded = store.booleanValue("OnlyFailedOutputsUploaded", false);
        reportUsedForCommandOutput = store.booleanValue("ReportUsedForCommandOutput", true);
        running = store.booleanValue("Running", false);
        startTime = store.dateValue("StartTime", new Date());
        targetFileId = store.stringValue("TargetFileID", null);
        if(store.containsName("Ticket")){
            try {
                ticket = (Ticket)SerializationUtils.deserialize(store.byteArrayValue("Ticket"));
            } catch (Exception e){
                logger.error("Error loading ticket data");
                throw new XmlStorageException("Error loading ticket data");
            }
        } else {
            ticket = null;
        }
        versionId = store.stringValue("VersinoID", null);
        waitingForLock = store.booleanValue("WaitingForLock", false);
        workflowId = store.stringValue("WorkflowID", null);
    }
}