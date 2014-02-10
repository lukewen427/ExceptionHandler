package com.connexience.server.ejb.admin;

import com.connexience.server.ConnexienceException;
import com.connexience.server.model.security.Ticket;

import javax.ejb.Remote;

/**
 * Author: Simon
 * Date: Jul 16, 2009
 */
@Remote
public interface AdminLoggingRemote
{
  public void processLogMessages(Ticket adminTicket) throws ConnexienceException;

}