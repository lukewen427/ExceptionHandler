package com.connexience.server.model.messages;

import com.connexience.server.model.ServerObject;

import java.io.Serializable;
import java.util.Vector;
import java.util.List;
import java.util.Date;

/**
 * Author: Simon
 * Date: Jul 17, 2009
 * This class represents a text message sent from one person to one or many other people.  It may be in reply to another message
 */
public class TextMessage extends Message implements Serializable
{
  /**
   * The id for this thread of messages.  Will be equal to the message id for the first message in the thread*/
  private String threadId;

  /**
   * Has this message been read
   * */
  private boolean read;

  /**
   * Title of the message
   * */
  private String title;


  public TextMessage()
  {
    super();
    this.setName("Text Message");
    this.setTimestamp(new Date());
  }

  public String getThreadId()
  {
    return threadId;
  }

  public void setThreadId(String threadId)
  {
    this.threadId = threadId;
  }

  public boolean isRead()
  {
    return read;
  }

  public void setRead(boolean read)
  {
    this.read = read;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }
}
