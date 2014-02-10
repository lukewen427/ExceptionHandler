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
public class ServiceSaveOperation extends GraphOperation
{

  /**
   * ID of the service
   */
  String serviceId;

  /**
   * Id of the version
   */
  String versionId;

  /**
   * Version number of the service
   */
  int versionNum;

  /**
   * Name of the service
   */
  String name;

  public ServiceSaveOperation()
  {
  }

  public ServiceSaveOperation(String serviceId, String versionId, int versionNum, String name, String userId, Date timestamp)
  {
    super();
    this.serviceId = serviceId;
    this.versionId = versionId;
    this.versionNum = versionNum;
    this.name = name;
    setUserId(userId);
    setTimestamp(timestamp);
  }

  public String getServiceId()
  {
    return serviceId;
  }

  public void setServiceId(String serviceId)
  {
    this.serviceId = serviceId;
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