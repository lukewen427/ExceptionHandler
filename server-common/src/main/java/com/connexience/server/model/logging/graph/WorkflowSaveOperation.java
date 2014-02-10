/*
 * WorkflowDataWriteOperation.java
 */

package com.connexience.server.model.logging.graph;

import java.util.Date;

/**
 * This operation is triggered whenever a user writes a piece of
 * data.
 *
 * @author hugo
 */
public class WorkflowSaveOperation extends GraphOperation
{

  /**
   * ID of the workflow
   */
  String workflowId;

  /**
   * ID of the version
   */
  String versionId;

  /**
   * Version number of the service
   */
  int versionNum;

  /**
   * Name of the workflow
   */
  String name;

  public WorkflowSaveOperation()
  {
  }

  public WorkflowSaveOperation(String workflowId, String versionId, int versionNum, String name, String userId, Date timestamp)
  {
    super();
    this.workflowId = workflowId;
    this.versionId = versionId;
    this.versionNum = versionNum;
    this.name = name;
    setUserId(userId);
    setTimestamp(timestamp);
  }

  public String getWorkflowId()
  {
    return workflowId;
  }

  public void setWorkflowId(String workflowId)
  {
    this.workflowId = workflowId;
  }

  public String getVersionId()
  {
    return versionId;
  }

  public void setVersionId(String versionId)
  {
    this.versionId = versionId;
  }

  public int getVersionNum()
  {
    return versionNum;
  }

  public void setVersionNum(int versionNum)
  {
    this.versionNum = versionNum;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }
}