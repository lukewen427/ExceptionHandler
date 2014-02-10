package com.connexience.server.model.logging.events;

/**
 * Author: Simon
 * Date: Mar 4, 2010
 */
public class DeleteEvent extends DataEvent
{

  public DeleteEvent()
  {
  }

  public DeleteEvent(Long timestamp, String principalId, String objectId, String objectType, String versionId, String objectName, String principalName)
  {
    super(timestamp, principalId, objectId, objectType, versionId, principalName, objectName);
  }
}
