package com.connexience.server.model.logging.events.annonymised;

import com.connexience.server.model.logging.events.LogEvent;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class DataEventAnnonymised extends LogEventAnnonymised
{
  private int objectId;

  private String objectType;

  private int versionId;

  public DataEventAnnonymised()
  {
  }

  public DataEventAnnonymised(Long timestamp, int principalId, int objectId, String objectType, int versionId)
  {
    super(timestamp, principalId);
    this.objectId = objectId;
    this.objectType = objectType;
    this.versionId = versionId;
  }

  public String getObjectType()
  {
    return objectType;
  }

  public void setObjectType(String objectType)
  {
    this.objectType = objectType;
  }

  public int getObjectId()
  {
    return objectId;
  }

  public void setObjectId(int objectId)
  {
    this.objectId = objectId;
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