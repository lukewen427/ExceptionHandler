package com.connexience.server.model.logging.events;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class WorkflowStartEvent extends LogEvent
{
  private String workflowId;

  private String workflowName;

  private String invocationId;

  private String versionId;

  public WorkflowStartEvent()
  {
  }

  public WorkflowStartEvent(Long timestamp, String principalId, String workflowId, String invocationId, String versionId, String principalName, String workflowName)
  {
    super(timestamp, principalId, principalName);
    this.workflowId = workflowId;
    this.invocationId = invocationId;
    this.versionId = versionId;
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

  public String getVersionId()
  {
    return versionId;
  }

  public void setVersionId(String versionId)
  {
    this.versionId = versionId;
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
