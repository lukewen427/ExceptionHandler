/*
 * WorkflowEngine.java
 */
package com.connexience.server.workflow.engine;

import com.connexience.server.ConnexienceException;

import com.connexience.server.model.logging.events.WorkflowStatus;
import com.connexience.server.workflow.service.*;
import com.connexience.server.model.security.*;
import com.connexience.server.workflow.api.*;
import com.connexience.server.workflow.util.ZipUtils;
import org.pipeline.core.xmlstorage.*;
import org.pipeline.core.drawing.*;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import org.apache.log4j.*;

/**
 * This class provides a singleton workflow management and execution engine. It
 * contains all of the WorkflowInvocation objects and manages status updates and
 * co-ordinates the status update messages. It uses JMS as the mechanism for
 * obtaining status updates.
 *
 * @author hugo
 */
public class WorkflowEngine {
    static Logger logger = Logger.getLogger(WorkflowEngine.class);
    
    /**
     * Currently executing workflow invocations
     */
    private ConcurrentHashMap<String, WorkflowInvocation> invocations = new ConcurrentHashMap<String, WorkflowInvocation>();
    
    /**
     * Global data source
     */
    private GlobalDataSource globalData = new GlobalDataSource(System.getProperty("user.home") + File.separator + "temp");
    /**
     * Desired transfer format for services
     */
    private String dataTransferType = DataProcessorDataSource.FILE_DATA_SOURCE;
    /**
     * API Provider that is used to create new API objects
     */
    private WorkflowAPIProvider apiProvider;
    /**
     * Counter to provide PID numbers for new invocations
     */
    private long pidCounter = 0;
    /**
     * Should the results data be stored in the workflow execution report. If this is false, the results
     * will be posted back directly to the database
     */
    private boolean reportUsedForCommandOutput = true;

    /** Listeners */
    private ArrayList<WorkflowEngineListener> listeners = new ArrayList<WorkflowEngineListener>();

    /**
     * Construct with a data source
     */
    public WorkflowEngine(GlobalDataSource globalData, WorkflowAPIProvider apiProvider) {
        this.globalData = globalData;
        this.apiProvider = apiProvider;
        logger.info("Created workflow engine in: " + globalData.getBaseDirectory());
    }

    /**
     * Construct with a default data source
     */
    public WorkflowEngine(WorkflowAPIProvider apiProvider) {
        this.apiProvider = apiProvider;
        logger.info("Created workflow engine in default location");
    }

    /** Add a listener */
    public void addWorkflowEngineListener(WorkflowEngineListener listener){
        listeners.add(listener);
    }

    /** Remove a listener */
    public void removeWorkflowEngineListener(WorkflowEngineListener listner){
        listeners.remove(listner);
    }

    /** Notify listeners that an invocation has started */
    public void notifyInvocationStarted(WorkflowInvocation invocation){
        for(int i=0;i<listeners.size();i++){
            listeners.get(i).invocationStarted(invocation);
        }
    }

    /** Notify listeners that an invocation has finised */
    private void notifyInvocationFinished(WorkflowInvocation invocation){
        for(int i=0;i<listeners.size();i++){
            listeners.get(i).invocationFinished(invocation);
        }
    }

    /**
     * Get the next sequential PID
     */
    public synchronized long getNextPid() {
        pidCounter++;
        return pidCounter;
    }

    /**
     * Provide an Enumeration of the running invocations
     */
    public Enumeration listInvocations() {
        return invocations.elements();
    }

    /**
     * Get the API Provider for this engine
     */
    public WorkflowAPIProvider getAPIProvider() {
        return apiProvider;
    }

    /**
     * Get the data transfer type. This specifies how services are expected
     * to get hold of and set transfer data
     */
    public String getDataTransferType() {
        return dataTransferType;
    }

    /**
     * Set the data transfer type. This specifies how services are expected
     * to get hold of and set transfer data
     */
    public void setDataTransferType(String dataTransferType) {
        this.dataTransferType = dataTransferType;
    }

    /**
     * Is the XML workflow report used to contain the output stream from the service
     */
    public boolean isReportUsedForCommandOutput() {
        return reportUsedForCommandOutput;
    }

    /**
     * Is the XML workflow report used to contain the output stream from the service
     */
    public void setReportUsedForCommandOutput(boolean reportUsedForCommandOutput) {
        this.reportUsedForCommandOutput = reportUsedForCommandOutput;
    }

    /**
     * Get a WorkflowInvocation by ID
     */
    public WorkflowInvocation getInvocation(String invocationId) {
        if (invocations.containsKey(invocationId)) {
            return invocations.get(invocationId);
        } else {
            return null;
        }
    }

    /**
     * Return the number of invocations currently in the engine
     */
    public int getInvocationCount(){
        return invocations.size();
    }

    /**
     * Get the number of active invocations currently in the engine. This
     * is defined as the number of invocations that are not waiting on
     * a workflow lock/
     */
    public int getActiveInvocationCount(){
        WorkflowInvocation invocation;
        Enumeration<WorkflowInvocation> invs = invocations.elements();
        int count = 0;
        while(invs.hasMoreElements()){
            invocation = invs.nextElement();
            if(!invocation.isWatitingForLock()){
                count++;
            }
        }
        return count;
    }

    /**
     * Get a reference to the global data store
     */
    public GlobalDataSource getDataSource() {
        return globalData;
    }

    /**
     * Create a WorkflowInvocation from a directory
     */
    public WorkflowInvocation createWorkflowInvocation(File directory) throws WorkflowInvocationException {
        WorkflowInvocation invocation = new WorkflowInvocation(apiProvider, directory);
        invocation.setDataSource(globalData.createInvocationDataSource(invocation.getInvocationId()));
        invocation.setParent(this);
        logger.info("Created workflow invocation from invocation directory. InvocationID=" + invocation.getInvocationId());
        return invocation;
    }
    
    /**
     * Create a WorkflowInvocation ready to start
     */
    public WorkflowInvocation createWorkflowInvocation(XmlDataStore drawingData, Ticket ticket) throws WorkflowInvocationException {
        // Load the drawing
        String invocationId = new RandomGUID().toString();
        WorkflowInvocation invocation = new WorkflowInvocation(drawingData, ticket, apiProvider, invocationId);
        invocation.setDataSource(globalData.createInvocationDataSource(invocation.getInvocationId()));
        invocation.setParent(this);
        logger.info("Creating workflow invocation object and assigning an invocation id. InvocationID=" + invocation.getInvocationId());
        return invocation;
    }

    /**
     * Create a workflow invocation with a pre-existing invocation ID
     */
    public WorkflowInvocation createWorkflowInvocation(XmlDataStore drawingData, Ticket ticket, String invocationId) throws WorkflowInvocationException {
        WorkflowInvocation invocation = new WorkflowInvocation(drawingData, ticket, apiProvider, invocationId);
        invocation.setInvocationId(invocationId);
        invocation.setDataSource(globalData.createInvocationDataSource(invocationId));
        invocation.setParent(this);
        logger.info("Created workflow invocation object. InvocationID=" + invocation.getInvocationId());
        return invocation;
    }

    /**
     * Start a workflow invocation
     */
    public void startInvocation(WorkflowInvocation invocation) throws WorkflowInvocationException {
        invocation.setPid(getNextPid());
        invocations.put(invocation.getInvocationId(), invocation);
        invocation.start();
    }

    /**
     * Find a workflow invocation by pid. This is not an efficient search, but is
     * only used from the command line tools
     */
    public WorkflowInvocation findInvocationByPid(long pid) {
        Enumeration<WorkflowInvocation> i = invocations.elements();
        WorkflowInvocation invocation;

        while (i.hasMoreElements()) {
            invocation = i.nextElement();
            if (invocation.getPid() == pid) {
                return invocation;
            }
        }
        return null;
    }

    /**
     * Process a response message
     */
    public void processResponseMessage(DataProcessorResponseMessage response) {
        String invocationId = response.getInvocationId();    // Invocation to update
        
        // Notify the correct invocation of the response
        if (invocations.containsKey(invocationId)) {
            logger.info("Workflow engine received service completion message. InvocationID=" + response.getInvocationId() + " BlockID=" + response.getContextId());
            invocations.get(invocationId).processorFinished(response);
        } else {
            logger.error("Received response message for non-existant workflow. InvocationID=" + response.getInvocationId());
        }
    }

    /**
     * An invocation has terminated
     */
    public void invocationFinished(WorkflowInvocation invocation) {
        logger.info("Workflow invocation finished. InvocationID=" + invocation.getInvocationId());
        invocations.remove(invocation.getInvocationId());

        //get the status of the workflow
        String status = WorkflowStatus.SUCCESS;
        Hashtable reports = invocation.getExecutionReports();
        for (Object key : reports.keySet()) {
            BlockExecutionReport report = (BlockExecutionReport) reports.get(key);
            if (report.getExecutionStatus() != BlockExecutionReport.NO_ERRORS) {
                status = WorkflowStatus.FAILED;
            }
        }
        notifyInvocationFinished(invocation);
    }

    /**
     * Kill a specific invocation
     */
    public void killInvocation(String invocationId) {
        logger.info("Killing invocation. InvocationID=" + invocationId);
        WorkflowInvocation invocation = getInvocation(invocationId);
        if (invocation != null) {
            invocation.kill();
        }
    }

    /**
     * Kill all the invocations
     */
    public void killAll() {
        logger.info("Killing all workflow invocations");
        ArrayList<WorkflowInvocation> killList = new ArrayList<WorkflowInvocation>();
        Enumeration<WorkflowInvocation> i = invocations.elements();
        while (i.hasMoreElements()) {
            killList.add(i.nextElement());
        }

        for (WorkflowInvocation invocation : killList) {
            invocation.kill();
        }
    }
    
    /**
     * Restart all of the workflows by reading the invocation directories
     */
    public void restartWorkflows(){
        File baseDirectory = new File(globalData.getBaseDirectory());
        if(baseDirectory.exists()){
            logger.info("Searching for unfinished invocations");
            File[] children = baseDirectory.listFiles();
            ArrayList<WorkflowInvocation> restoredInvocations = new ArrayList<WorkflowInvocation>();
            WorkflowInvocation invocation;
            for(int i=0;i<children.length;i++){
                if(children[i].isDirectory()){
                    logger.info("Attempting to recreate workflow from: " + children[i].getPath());
                    try {
                        invocation = createWorkflowInvocation(children[i]);
                        restoredInvocations.add(invocation);
                    } catch (Exception e){
                        logger.error("Error recreating workflow from: " + children[i].getPath() + ": " + e.getMessage());
                        try {
                            ZipUtils.removeDirectory(children[i]);
                        } catch (Exception ex){
                            logger.error("Error removing corrupt invocation folder: " + children[i].getPath(), ex);
                        }
                    }
                }
            }
            
            // Try and start the invocations
            for(WorkflowInvocation i : restoredInvocations){
                try {
                    logger.info("Attempting to restart workflow invocation. InvocationID=" + i.getInvocationId());
                    startInvocation(i);
                } catch (Exception e){
                    logger.error("Error restarting invocation. InvocationID=" + i.getInvocationId());
                }
            }
        }
    }
}
