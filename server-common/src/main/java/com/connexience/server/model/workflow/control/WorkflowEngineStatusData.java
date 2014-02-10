/*
 * WorkflowEngineStatusData.java
 */
package com.connexience.server.model.workflow.control;
import java.io.*;
import java.util.*;
/**
 * This class contains information regarding the status of a workflow engine.
 * @author hugo
 */
public class WorkflowEngineStatusData implements Serializable {
    static final long serialVersionUID = 5144727020994760291L;
    
    /** Size of the working disk */
    private long diskSize;
    
    /** Free space on the working disk */
    private long freeSpace;
    
    /** Workflow capacity */
    private int workflowCapacity;
    
    /** Number of running workflows */
    private int workflowCount;
    
    /** Is the queue connected */
    private boolean jmsConnected;
    
    /** Total workflows executed */
    private long totalWorkflowsStarted;
    
    /** Total workflows succeeded */
    private long totalWorkflowsSucceeded;
    
    /** Total workflows failed */
    private long totalWorkflowsFailed;
    
    /** Engine start date */
    private Date engineStartTime;

    /** Date of this status update */
    private Date statusDate = new Date();
    
    /** List of invocations */
    private ArrayList<WorkflowInvocationRecord> invocations = new ArrayList<WorkflowInvocationRecord>();
    
    public WorkflowEngineStatusData() {
    }

    public long getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(long diskSize) {
        this.diskSize = diskSize;
    }

    public Date getEngineStartTime() {
        return engineStartTime;
    }

    public void setEngineStartTime(Date engineStartTime) {
        this.engineStartTime = engineStartTime;
    }

    public long getFreeSpace() {
        return freeSpace;
    }

    public void setFreeSpace(long freeSpace) {
        this.freeSpace = freeSpace;
    }

    public boolean isJmsConnected() {
        return jmsConnected;
    }

    public void setJmsConnected(boolean jmsConnected) {
        this.jmsConnected = jmsConnected;
    }

    public long getTotalWorkflowsStarted() {
        return totalWorkflowsStarted;
    }

    public void setTotalWorkflowsStarted(long totalWorkflowsStarted) {
        this.totalWorkflowsStarted = totalWorkflowsStarted;
    }

    public int getWorkflowCapacity() {
        return workflowCapacity;
    }

    public void setWorkflowCapacity(int workflowCapacity) {
        this.workflowCapacity = workflowCapacity;
    }

    public int getWorkflowCount() {
        return workflowCount;
    }

    public void setWorkflowCount(int workflowCount) {
        this.workflowCount = workflowCount;
    }

    public void setTotalWorkflowsFailed(long totalWorkflowsFailed) {
        this.totalWorkflowsFailed = totalWorkflowsFailed;
    }

    public long getTotalWorkflowsFailed() {
        return totalWorkflowsFailed;
    }

    public void setTotalWorkflowsSucceeded(long totalWorkflowsSucceeded) {
        this.totalWorkflowsSucceeded = totalWorkflowsSucceeded;
    }

    public long getTotalWorkflowsSucceeded() {
        return totalWorkflowsSucceeded;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public ArrayList<WorkflowInvocationRecord> getInvocations() {
        return invocations;
    }

    public void setInvocations(ArrayList<WorkflowInvocationRecord> invocations) {
        this.invocations = invocations;
    }
    
}