package com.connexience.server.model.logging.events;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class LoginEvent extends LogEvent
{

  public LoginEvent()
  {
  }

  public LoginEvent(Long timestamp, String principalId, String principalName)
  {
    super(timestamp, principalId, principalName);
  }
  }
