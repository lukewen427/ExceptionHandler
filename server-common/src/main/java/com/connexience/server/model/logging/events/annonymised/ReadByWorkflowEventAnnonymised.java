package com.connexience.server.model.logging.events.annonymised;

import com.connexience.server.model.logging.events.DataEvent;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class ReadByWorkflowEventAnnonymised extends DataEventAnnonymised
{
  private int workflowId;

  private int invocationId;

  public ReadByWorkflowEventAnnonymised()
  {
  }

  public ReadByWorkflowEventAnnonymised(Long timestamp,  int principalId, int objectId, String objectType, int versionId, int workflowId, int invocationId)
  {
    super(timestamp, principalId, objectId, objectType, versionId);
    this.workflowId = workflowId;
    this.invocationId = invocationId;
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
}