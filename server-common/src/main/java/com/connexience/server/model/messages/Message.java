package com.connexience.server.model.messages;

import com.connexience.server.model.ServerObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: Martyn
 * Date: Jul 17, 2009
 */
public class Message extends ServerObject implements Serializable
{
  /**
   * Time that this request was made
   */
  private Date timestamp;

  /**
   * Text of the message
   */
  private String message;

  /**
   * The id of the sender
   * */
  private String senderId;

  /**
   * The id of the recipient
   * */
  private String recipientId;

  public Message()
  {
    super();
    this.timestamp = new Date();
  }

  /**
   * Get the creation time of this request
   */
  public Date getTimestamp()
  {
    return timestamp;
  }

  /**
   * Set the creation time of this request
   */
  public void setTimestamp(Date timestamp)
  {
    this.timestamp = timestamp;
  }

  /**
   * Get the message that will be displayed to the target user
   */
  public String getMessage()
  {
    return message;
  }

  /**
   * Set the message that will be displayed to the target user
   */
  public void setMessage(String message)
  {
    this.message = message;
  }

  public String getSenderId()
  {
    return senderId;
  }

  public void setSenderId(String senderId)
  {
    this.senderId = senderId;
  }

  public String getRecipientId()
  {
    return recipientId;
  }

  public void setRecipientId(String recipientId)
  {
    this.recipientId = recipientId;
  }
}
