/*
 * ReadByServiceEvent.java
 */

package com.connexience.server.model.logging.events;

/**
 * This event logs the fact that a piece of data has been read by a workflow service
 * @author hugo
 */
public class ReadByServiceEvent extends DataEvent {
    /** Service ID */
    private String serviceId;

    /** Service Version ID */
    private String serviceVersionId;

    /** Service Version number */
    private int serviceVersionNumber;

    /** Invocation ID */
    private String invocationId;

    /** Workflow ID */
    private String workflowId;

    /** Workflow Name */
    private String workflowName;

    /** Name of the service document record */
    private String serviceName;

  public ReadByServiceEvent()
  {
  }

  public ReadByServiceEvent(Long timestamp, String principalId, String objectId, String objectType, String versionId, String principalName, String objectName) {
        super(timestamp, principalId, objectId, objectType, versionId, principalName, objectName);
    }

    public String getInvocationId() {
        return invocationId;
    }

    public void setInvocationId(String invocationId) {
        this.invocationId = invocationId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceVersionId() {
        return serviceVersionId;
    }

    public void setServiceVersionId(String serviceVersionId) {
        this.serviceVersionId = serviceVersionId;
    }

    public int getServiceVersionNumber() {
        return serviceVersionNumber;
    }

    public void setServiceVersionNumber(int serviceVersionNumber) {
        this.serviceVersionNumber = serviceVersionNumber;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getWorkflowName()
    {
      return workflowName;
    }

    public void setWorkflowName(String workflowName)
    {
      this.workflowName = workflowName;
    }
}
