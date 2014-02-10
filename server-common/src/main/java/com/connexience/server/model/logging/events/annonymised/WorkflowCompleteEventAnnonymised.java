package com.connexience.server.model.logging.events.annonymised;

import com.connexience.server.model.logging.events.LogEvent;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class WorkflowCompleteEventAnnonymised extends LogEventAnnonymised
{
  private int workflowId;

  private int invocationId;

  private String state;

  public WorkflowCompleteEventAnnonymised()
  {
  }

  public WorkflowCompleteEventAnnonymised(Long timestamp, int principalId, int workflowId, int invocationId, String state)
  {
    super(timestamp, principalId);
    this.workflowId = workflowId;
    this.invocationId = invocationId;
    this.state = state;
  }

  public int getWorkflowId()
  {
    return workflowId;
  }

  public void setWorkflowId(int workflowId)
  {
    this.workflowId = workflowId;
  }

  public int getInvocationId()
  {
    return invocationId;
  }

  public void setInvocationId(int invocationId)
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
}