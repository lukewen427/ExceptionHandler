package com.connexience.server.model.logging.events;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class WorkflowCompleteEvent extends LogEvent
{
  private String workflowId;

  private String workflowName;

  private String invocationId;

  private String state;

  public WorkflowCompleteEvent()
  {
  }

  public WorkflowCompleteEvent(Long timestamp, String principalId, String workflowId, String invocationId, String state, String principalName, String workflowName)
  {
    super(timestamp, principalId, principalName);
    this.workflowId = workflowId;
    this.invocationId = invocationId;
    this.state = state;
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

  public String getState()
  {
    return state;
  }

  public void setState(String state)
  {
    this.state = state;
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