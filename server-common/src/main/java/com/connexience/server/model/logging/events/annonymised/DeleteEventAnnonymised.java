package com.connexience.server.model.logging.events.annonymised;

import com.connexience.server.model.logging.events.DataEvent;

/**
 * Author: Simon
 * Date: Mar 4, 2010
 */
public class DeleteEventAnnonymised extends DataEventAnnonymised
{
  private String objectName;

  public DeleteEventAnnonymised()
  {
  }

  public DeleteEventAnnonymised(Long timestamp, int principalId, int objectId, String objectType, int versionId, String objectName)
  {
    super(timestamp, principalId, objectId, objectType, versionId);
    this.objectName = objectName;
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