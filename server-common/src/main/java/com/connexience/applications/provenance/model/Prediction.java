package com.connexience.applications.provenance.model;

import java.util.Date;

public class Prediction
{
  private long id;

  private String workflowId;
  
  private Date startTime;
  
  private Date endTime;

  public Prediction()
  {
  }

  public long getId()
  {
    return id;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  public String getWorkflowId()
  {
    return workflowId;
  }

  public void setWorkflowId(String workflowId)
  {
    this.workflowId = workflowId;
  }

  public Date getStartTime()
  {
    return startTime;
  }

  public void setStartTime(Date startTime)
  {
    this.startTime = startTime;
  }

  public Date getEndTime()
  {
    return endTime;
  }

  public void setEndTime(Date endTime)
  {
    this.endTime = endTime;
  }
}
