package com.connexience.server.model.logging.events;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class RegisterEvent extends LogEvent
{
  public RegisterEvent()
  {
  }

  public RegisterEvent(Long timestamp, String principalId, String principalName)
  {
    super(timestamp, principalId, principalName);
  }
}
