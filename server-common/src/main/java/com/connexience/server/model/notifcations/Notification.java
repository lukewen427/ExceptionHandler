package com.connexience.server.model.notifcations;

import com.connexience.server.model.messages.Message;
import com.connexience.server.model.security.Ticket;

import javax.jms.ObjectMessage;
import javax.jms.JMSException;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: martyn
 * Date: 16-Nov-2009
 * Time: 15:52:24
 * To change this template use File | Settings | File Templates.
 */
public abstract class Notification implements Serializable
{
  protected String userId;

  protected Ticket ticket;

  protected Notification()
  {
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId(String userId)
  {
    this.userId = userId;
  }

  public Ticket getTicket()
  {
    return ticket;
  }

  public void setTicket(Ticket ticket)
  {
    this.ticket = ticket;
  }
}
