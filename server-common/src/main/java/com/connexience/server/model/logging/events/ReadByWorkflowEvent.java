package com.connexience.server.model.logging.events;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class ReadByWorkflowEvent extends DataEvent
{
  private String workflowId;

  private String workflowName;

  private String invocationId;

  public ReadByWorkflowEvent()
  {
  }

  public ReadByWorkflowEvent(Long timestamp, String principalId, String objectId, String objectType, String versionId, String workflowId, String invocationId, String principalName, String objectName, String workflowName)
  {
    super(timestamp, principalId, objectId, objectType, versionId, principalName, objectName);
    this.workflowId = workflowId;
    this.invocationId = invocationId;
    this.workflowName = workflowName;
  }

  public String getWorkflowId()
  {
    return workflowId;
  }

  public void setWorkflowId(String workflowId)
  {
    this.workflowId = workflowId;
  }

  public String getInvocationId()
  {
    return invocationId;
  }

  public void setInvocationId(String invocationId)
  {
    this.invocationId = invocationId;
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
