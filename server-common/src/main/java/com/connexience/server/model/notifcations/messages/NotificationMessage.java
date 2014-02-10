package com.connexience.server.model.notifcations.messages;

import com.connexience.server.model.messages.Message;
import com.connexience.server.model.security.Ticket;
import com.connexience.server.ConnexienceException;

/**
 * Created by IntelliJ IDEA.
 * User: martyn
 * Date: 27-Nov-2009
 * Time: 10:42:07
 * To change this template use File | Settings | File Templates.
 */
public class NotificationMessage extends Message
{
    private boolean isRead;

    private String description;

    public boolean isIsRead()
    {
        return isRead;
    }

    public void setIsRead(boolean isRead)
    {
        this.isRead = isRead;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
