package com.connexience.server.model.logging.events.annonymised;

import com.connexience.server.model.logging.events.DataEvent;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class ReadEventAnnonymised extends DataEventAnnonymised
{

  public ReadEventAnnonymised()
  {
  }

  public ReadEventAnnonymised(Long timestamp, int principalId, int objectId, String objectType, int versionId)
  {
    super(timestamp, principalId, objectId, objectType, versionId);
  }
}