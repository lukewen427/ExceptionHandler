package com.connexience.server.model.notifcations.messages;

import com.connexience.server.model.messages.Message;
import com.connexience.server.model.security.Ticket;
import com.connexience.server.ConnexienceException;

/**
 * Created by IntelliJ IDEA.
 * User: martyn
 * Date: 18-Nov-2009
 * Time: 14:50:01
 * To change this template use File | Settings | File Templates.
 */
public class BlogPostNotificationMessage extends NotificationMessage
{
    public BlogPostNotificationMessage()
    {
    }

    public String getNotifcationMessage(Ticket ticket) throws ConnexienceException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
