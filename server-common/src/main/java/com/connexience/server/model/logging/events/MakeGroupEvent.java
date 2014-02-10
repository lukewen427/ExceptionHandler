package com.connexience.server.model.logging.events;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class MakeGroupEvent extends LogEvent
{
  private String objectId;
  private String objectName;

  public MakeGroupEvent()
  {
  }

  public MakeGroupEvent(Long timestamp, String principalId, String objectId, String prinicpalName, String objectName)
  {
    super(timestamp, principalId, prinicpalName);
    this.objectId = objectId;
    this.objectName = objectName;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public void setObjectId(String objectId)
  {
    this.objectId = objectId;
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
