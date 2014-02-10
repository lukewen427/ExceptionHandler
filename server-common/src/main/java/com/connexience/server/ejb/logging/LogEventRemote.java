package com.connexience.server.ejb.logging;

import com.connexience.server.ConnexienceException;
import com.connexience.server.model.logging.events.LogEvent;
import com.connexience.server.model.logging.events.LoginEvent;
import com.connexience.server.model.logging.graph.*;
import com.connexience.server.model.security.Ticket;

import javax.ejb.Local;
import javax.ejb.Remote;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Author: Simon
 * Date: Feb 9, 2010
 */
@Remote
public interface LogEventRemote
{
  void logEvent(Ticket ticket, LogEvent logEvent);

  void logEvents(Collection<LogEvent> logEvents);

  HashMap<String, BigInteger> getObjectStats(Ticket ticket, String id, Date startDate, Date endDate) throws ConnexienceException;

  List<LogEvent> getObjectLog(Ticket ticket, String id, int maxResults, int offset) throws ConnexienceException;

  /**
   * Get a vector containing the first and last date of the object with numOffset between them.  Allows the timeline to be
   * configured to a better scale
   *
   * @param ticket         Users ticket
   * @param serverObjectId Id of the object whose history is being displayed
   * @param numOffset      Number of news items to be displayed
   * @return Vector containing dates for the first (newest) and last (oldest) news items
   * @throws com.connexience.server.ConnexienceException something went wrong!
   */
  Collection<Date> getObjectLogDateOffset(Ticket ticket, String serverObjectId, int numOffset) throws ConnexienceException;


    long getLastLoginEvent(Ticket ticket, String id) throws ConnexienceException;
}
