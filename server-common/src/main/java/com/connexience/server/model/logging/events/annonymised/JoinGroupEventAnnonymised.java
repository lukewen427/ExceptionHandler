package com.connexience.server.model.logging.events.annonymised;

import com.connexience.server.model.logging.events.LogEvent;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class JoinGroupEventAnnonymised extends LogEventAnnonymised
{
  private int objectId;

  public JoinGroupEventAnnonymised()
  {
  }

  public JoinGroupEventAnnonymised(Long timestamp, int principalId, int objectId)
  {
    super(timestamp, principalId);
    this.objectId = objectId;
  }

  public int getObjectId()
  {
    return objectId;
  }

  public void setObjectId(int objectId)
  {
    this.objectId = objectId;
  }
}