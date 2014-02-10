/*
 * WorkflowDataTransferOperation.java
 */

package com.connexience.server.model.logging.graph;

/**
 * This class represents the transfer of a piece of data from one block (service)
 * in a workflow to another. It contains enough data to recreate the two operations
 * which in turn can be used to construct a "virtual workflow" to recreate the
 * steps taken in generating a piece of data.
 * @author hugo
 */
public class WorkflowDataTransferOperation extends com.connexience.server.model.logging.graph.WorkflowGraphOperation
{
    /** Source port name */
    private String sourcePortName;

    /** Source block UUID */
    private String sourceBlockUUID;

    /** Target port name */
    private String targetPortName;

    /** Target block UUID */
    private String targetBlockUUID;

    /** Data type name */
    private String dataType;

    /** MD5 hash of data if known */
    private String hashValue = null;
    
    /** Number of bytes transferred */
    private long dataSize;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getSourceBlockUUID() {
        return sourceBlockUUID;
    }

    public void setSourceBlockUUID(String sourceBlockUUID) {
        this.sourceBlockUUID = sourceBlockUUID;
    }

    public String getSourcePortName() {
        return sourcePortName;
    }

    public void setSourcePortName(String sourcePortName) {
        this.sourcePortName = sourcePortName;
    }

    public String getTargetBlockUUID() {
        return targetBlockUUID;
    }

    public void setTargetBlockUUID(String targetBlockUUID) {
        this.targetBlockUUID = targetBlockUUID;
    }

    public String getTargetPortName() {
        return targetPortName;
    }

    public void setTargetPortName(String targetPortName) {
        this.targetPortName = targetPortName;
    }

    public long getDataSize() {
        return dataSize;
    }

    public void setDataSize(long dataSize) {
        this.dataSize = dataSize;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    public String getHashValue() {
        return hashValue;
    }
}