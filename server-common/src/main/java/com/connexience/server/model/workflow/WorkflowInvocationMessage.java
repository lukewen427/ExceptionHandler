/*
 * WorkflowInvocationMessage.java
 */

package com.connexience.server.model.workflow;

import com.connexience.server.model.security.*;
import java.io.*;

/**
 * This message is used to initialise a Workflow. It contains the workflow ID
 * and any parameters that are needed to start the execution.
 * @author nhgh
 */
public class WorkflowInvocationMessage implements Serializable {
    static final long serialVersionUID = -5182007711747004291L;
    
    /** ID of the workflow to start */
    private String workflowId;
    
    /** Version ID of the workflow document to retrieve */
    private String versionId;
    
    /** Should the latest file version be used */
    private boolean useLatest = true;

    /** ID of the external file to target */
    private String targetFileId = null;

    /** Ticket of the user that the workflow is being run for */
    private Ticket ticket = null;

    /** Invocation ID assigned to this workflow run */
    private String invocationId;

    /** ID of the results folder to send data to */
    private String resultsFolderId;

    /** Name to call the results folder */
    private String resultsFolderName;
    
    /** Name of the input block in the workflow */
    private String inputBlockName = null;

    /** XML Representation of parameter list */
    private byte[] parameterXmlData = null;

    /** Should this workflow be attached to a lock */
    private boolean lockMember = false;

    /** ID of the lock to attach this workflow to */
    private long lockId = 0;
    
    /** Should the invocation be deleted if successful */
    private boolean deletedOnSuccess = false;
    
    /** Only upload output data for failed blocks */
    private boolean onlyFailedOutputsUploaded = false;    

    public WorkflowInvocationMessage(Ticket ticket, String workflowId) {
        this.workflowId = workflowId;
        this.ticket = ticket;
        this.versionId = null;
        this.useLatest = true;
    }

    public WorkflowInvocationMessage() {
    }

    public WorkflowInvocationMessage(Ticket ticket, String workflowId, String versionId) {
        this.workflowId = workflowId;
        this.ticket = ticket;
        this.versionId = versionId;
        useLatest = false;
    }

    public String getResultsFolderId(){
        return resultsFolderId;
    }

    public void setResultsFolderId(String resultsFolderId){
        this.resultsFolderId = resultsFolderId;
    }
    
    public String getInvocationId(){
        return invocationId;
    }

    public void setInvocationId(String invocationId){
        this.invocationId = invocationId;
    }
    
    public Ticket getTicket(){
        return ticket;
    }

    public void setTicket(Ticket ticket){
        this.ticket = ticket;
    }
    
    public void setTargetFileId(String targetFileId){
        this.targetFileId = targetFileId;
    }

    public String getTargetFileId(){
        return targetFileId;
    }
    
    public boolean isUseLatest() {
        return useLatest;
    }

    public void setUseLatest(boolean useLatest) {
        this.useLatest = useLatest;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public void setLockMember(boolean lockMember) {
        this.lockMember = lockMember;
    }

    public boolean isLockMember() {
        return lockMember;
    }

    public void setLockId(long lockId) {
        this.lockId = lockId;
    }

    public long getLockId() {
        return lockId;
    }

    public String getInputBlockName() {
        return inputBlockName;
    }

    public void setInputBlockName(String inputBlockName) {
        this.inputBlockName = inputBlockName;
    }

    public byte[] getParameterXmlData() {
        return parameterXmlData;
    }

    public void setParameterXmlData(byte[] parameterXmlData) {
        this.parameterXmlData = parameterXmlData;
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

    public String getResultsFolderName() {
        return resultsFolderName;
    }

    public void setResultsFolderName(String resultsFolderName) {
        this.resultsFolderName = resultsFolderName;
    }
}