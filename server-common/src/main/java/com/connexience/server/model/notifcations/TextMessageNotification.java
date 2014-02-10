package com.connexience.server.model.notifcations;

import com.connexience.server.model.messages.TextMessage;
import com.connexience.server.model.messages.Message;
import com.connexience.server.model.security.Ticket;

/**
 * Created by IntelliJ IDEA.
 * User: martyn
 * Date: 16-Nov-2009
 * Time: 16:14:43
 * To change this template use File | Settings | File Templates.
 */
public class TextMessageNotification extends Notification
{
  private TextMessage textMessage;

  public TextMessageNotification(Ticket ticket, String userId, TextMessage textMessage)
  {
    setTicket(ticket);
    setTextMessage(textMessage);
    setUserId(userId);
  }

  public void setTextMessage(TextMessage textMessage)
  {
    this.textMessage = textMessage;
  }

  public TextMessage getTextMessage()
  {
    return textMessage;
  }
}
