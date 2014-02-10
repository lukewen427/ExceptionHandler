package com.connexience.server.model.notifcations;

import com.connexience.server.model.security.Ticket;

/**
 * Created by IntelliJ IDEA.
 * User: martyn
 * Date: 18-Nov-2009
 * Time: 14:15:51
 * To change this template use File | Settings | File Templates.
 */
public class BlogPostNotification extends Notification
{
    
    public BlogPostNotification(Ticket ticket, String userId)
    {
      setTicket(ticket);
        this.userId = userId;
    }
}
