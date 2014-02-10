package com.connexience.server.model.logging.events.annonymised;

import com.connexience.server.model.logging.events.LogEvent;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class LoginEventAnnonymised extends LogEventAnnonymised
{

  public LoginEventAnnonymised()
  {
  }

  public LoginEventAnnonymised(Long timestamp, int principalId)
  {
    super(timestamp, principalId);
  }
}