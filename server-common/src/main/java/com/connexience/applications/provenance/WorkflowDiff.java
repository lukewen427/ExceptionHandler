package com.connexience.applications.provenance;

import java.io.Serializable;

/**
 * User: nsjw7
 * Date: 27/07/2012
 * Time: 10:29
 *
 * This class holds reference to the workflow which ran in the provenance graph
 *
 */
public class WorkflowDiff implements Serializable
{
  private String workflowId = "";
  private String versionId = "";
  private String name = "";

  public WorkflowDiff()
  {
  }

  public WorkflowDiff(String workflowId, String versionId, String name)
  {
    this.workflowId = workflowId;
    this.versionId = versionId;
    this.name = name;
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

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @Override
  public String toString()
  {
    return "WorkflowDiff{" +
        "workflowId='" + workflowId + '\'' +
        ", versionId='" + versionId + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
