package com.connexience.server.model.logging.events.annonymised;

import com.connexience.server.model.logging.events.LogEvent;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class RegisterEventAnnonymised extends LogEventAnnonymised
{
  public RegisterEventAnnonymised()
  {
  }

  public RegisterEventAnnonymised(Long timestamp, int principalId)
  {
    super(timestamp, principalId);
  }
}