package com.connexience.server.model.logging.events.annonymised;

import com.connexience.server.model.logging.events.LogEvent;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class WorkflowStartEventAnnonymised extends LogEventAnnonymised
{
  private int workflowId;

  private int invocationId;

  private int versionId;

  public WorkflowStartEventAnnonymised()
  {
  }

  public WorkflowStartEventAnnonymised(Long timestamp, int principalId, int workflowId, int invocationId, int versionId)
  {
    super(timestamp, principalId);
    this.workflowId = workflowId;
    this.invocationId = invocationId;
    this.versionId = versionId;
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

  public int getVersionId()
  {
    return versionId;
  }

  public void setVersionId(int versionId)
  {
    this.versionId = versionId;
  }
}