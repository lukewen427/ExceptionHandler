package com.connexience.server.model.logging.events;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class DataEvent extends LogEvent
{
  private String objectId;

  private String objectType;

  private String versionId;

  private String objectName; //name of the object so that it doesn't need to be looked up when displaying log

  public DataEvent()
  {
  }

  public DataEvent(Long timestamp, String principalId, String objectId, String objectType, String versionId, String principalName, String objectName)
  {
    super(timestamp, principalId, principalName);
    this.objectId = objectId;
    this.objectType = objectType;
    this.versionId = versionId;
    this.objectName = objectName;
  }

  public String getObjectType()
  {
    return objectType;
  }

  public void setObjectType(String objectType)
  {
    this.objectType = objectType;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public void setObjectId(String objectId)
  {
    this.objectId = objectId;
  }

  public String getVersionId()
  {
    return versionId;
  }

  public void setVersionId(String versionId)
  {
    this.versionId = versionId;
  }

  public String getObjectName()
  {
    return objectName;
  }

  public void setObjectName(String objectName)
  {
    this.objectName = objectName;
  }
}
