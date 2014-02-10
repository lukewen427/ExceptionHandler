package com.connexience.server.model.logging.events.annonymised;

import com.connexience.server.model.logging.events.DataEvent;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class WriteEventAnnonymised extends DataEventAnnonymised
{
  public WriteEventAnnonymised()
  {
  }

  public WriteEventAnnonymised(Long timestamp,  int principalId, int objectId, String objectType, int versionId)
  {
    super(timestamp, principalId, objectId, objectType, versionId);
  }
}