/*
 * TicketThreadStore.java
 */

package com.connexience.server.ws;

import com.connexience.server.model.security.Ticket;

/**
 * This class provides a single thread local variable that is used to store
 * logon tickets during EJB access.
 *
 * @author nhgh
 */
public class TicketThreadStore
{
  /**
   * Thread local object holding the current Ticket object if there
   * is one for security enforcement
   */
  private static final ThreadLocal TICKET_STORE = new ThreadLocal();

  /**
   * Creates a new instance of TicketThreadStore
   */
  public TicketThreadStore()
  {
  }

  /**
   * Set the current ticket
   */
  public static final void setTicket(Ticket ticket)
  {
    TICKET_STORE.set(ticket);
  }

  /**
   * Get the current ticket
   */
  public static final Ticket getTicket()
  {
    if (TICKET_STORE.get() != null)
    {
      return (Ticket) TICKET_STORE.get();
    }
    else
    {
      return null;
    }
  }
}
