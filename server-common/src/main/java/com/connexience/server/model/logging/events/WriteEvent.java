package com.connexience.server.model.logging.events;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class WriteEvent extends DataEvent
{
  public WriteEvent()
  {
  }

  public WriteEvent(Long timestamp, String principalId, String objectId, String objectType, String versionId, String principalName, String objectName)
  {
    super(timestamp, principalId, objectId, objectType, versionId, principalName, objectName);
  }
}
