/*
 * WorkflowDataServiceOperation.java
 */

package com.connexience.server.model.logging.graph;

import java.util.*;

/**
 * This operation occurs whenever a service is invoked in a workflow.
 *
 * @author hugo
 */
public class WorkflowDataServiceOperation extends WorkflowGraphOperation {
    /**
     * Serialized call properties. This is used to extract the
     * properties sent to the service when it was called
     */
    private byte[] propertiesData;

    /** ID of the service document */
    private String serviceId;

    /** Version ID of the service document */
    private String versionId;

    /** Version nunmber of the service document */
    private int versionNum;

    /** Dependency IDs */
    private String[] dependencyIds;

    /** Dependency version IDs */
    private String[] dependencyVersionIds;

    /** Dependency names */
    private String[] dependencyNames;

    /** Service end time */
    private Date endTimestamp;

    /** Name of the service document */
    private String serviceName;

    /** Context UUID of the block running this service */
    private String blockUUID;

    /** VersionId of the workflow that contains this service */
    private String workflowVersionId;

    /** Is the service idempotent */
    private boolean idempotent = true;

    /** Is the service deterministic */
    private boolean deterministic = true;

    /** The amount of data this service consumed */
    private long dataConsumedSize = 0;

    /** The amount of data thsi service produced */
    private long dataProducedSize = 0;

    public String[] getDependencyIds() {
        return dependencyIds;
    }

    public void setDependencyIds(String[] dependencyIds) {
        this.dependencyIds = dependencyIds;
    }

    public String[] getDependencyNames() {
        return dependencyNames;
    }

    public void setDependencyNames(String[] dependencyNames) {
        this.dependencyNames = dependencyNames;
    }

    public String[] getDependencyVersionIds() {
        return dependencyVersionIds;
    }

    public void setDependencyVersionIds(String[] dependencyVersionIds) {
        this.dependencyVersionIds = dependencyVersionIds;
    }

    public byte[] getPropertiesData() {
        return propertiesData;
    }

    public void setPropertiesData(byte[] propertiesData) {
        this.propertiesData = propertiesData;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public Date getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Date endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getBlockUUID() {
        return blockUUID;
    }

    public void setBlockUUID(String blockUUID) {
        this.blockUUID = blockUUID;
    }

    public int getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(int versionNum) {
        this.versionNum = versionNum;
    }

    public String getWorkflowVersionId() {
        return workflowVersionId;
    }

    public void setWorkflowVersionId(String workflowVersionId) {
        this.workflowVersionId = workflowVersionId;
    }

    public boolean isIdempotent() {
        return idempotent;
    }

    public void setIdempotent(boolean idempotent) {
        this.idempotent = idempotent;
    }

    public boolean isDeterministic() {
        return deterministic;
    }

    public void setDeterministic(boolean deterministic) {
        this.deterministic = deterministic;
    }

    public long getDataConsumedSize()
    {
      return dataConsumedSize;
    }

    public void setDataConsumedSize(long dataConsumedSize)
    {
      this.dataConsumedSize = dataConsumedSize;
    }

    public long getDataProducedSize()
    {
      return dataProducedSize;
    }

    public void setDataProducedSize(long dataProducedSize)
    {
      this.dataProducedSize = dataProducedSize;
    }
}
