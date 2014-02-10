/*
 * LogMessage.java
 */

package com.connexience.server.model.logging;

import com.connexience.server.*;
import com.connexience.server.model.*;
import com.connexience.server.model.security.*;

import java.io.*;
import java.util.*;

/**
 * This class represents a log message that can be stored in the database
 *
 * @author nhgh
 */
public class LogMessage implements Serializable
{
  // Message types

  /**
   * Security violation
   */
  public static final int SECURITY_VIOLATION = 0;

  /**
   * Object added
   */
  public static final int OBJECT_ADDED = 1;

  /**
   * Object removed
   */
  public static final int OBJECT_REMOVED = 2;

  /**
   * Object modified
   */
  public static final int OBJECT_MODIFIED = 3;

  /**
   * Object downloaded
   */
  public static final int OBJECT_DOWNLOADED = 4;

  /**
   * New Version of document created
   */
  public static final int NEW_VERSION = 5;

  /**
   * User logged in
   */
  public static final int LOGIN = 6;


  /**
   * Database id of message
   */
  private long id;

  /**
   * Organisation ID that this message originated from
   */
  private String organisationId;

  /**
   * ID of the object that this message refers to
   */
  private String objectId;

  /**
   * ID of the principal that this message was caused by
   */
  private String principalId;

  /**
   * Message type
   */
  private int type;

  /**
   * Message date
   */
  private Date messageDate;

  /**
   * Message text
   */
  private String messageText;


  /**
   * Creates a new instance of LogMessage
   */
  public LogMessage()
  {
  }

  /**
   * Creates a log message with standard text
   */
  public LogMessage(Ticket ticket, ServerObject object, int type)
  {
    if (ticket != null)
    {
      this.principalId = ticket.getUserId();
      this.organisationId = ticket.getOrganisationId();
    }
    else
    {
      this.principalId = "";
      this.organisationId = "";
    }

    this.objectId = object.getId();
    this.messageDate = new Date();
    this.type = type;

    switch (type)
    {
      case SECURITY_VIOLATION:
        messageText = ConnexienceException.ACCESS_DENIED_MESSAGE;
        break;
      case OBJECT_ADDED:
        messageText = "Object created";
        break;
      case OBJECT_REMOVED:
        messageText = "Object removed";
        break;
      case OBJECT_MODIFIED:
        messageText = "Object modified";
        break;
      case OBJECT_DOWNLOADED:
        messageText = "Object downloaded";
        break;
      case NEW_VERSION:
        messageText = "New version created";
        break;
      case LOGIN:
        messageText = "User Logged In";
        break;
      default:
        messageText = "Unknown";
    }
  }

  /**
   * Creates a log message with a ticket and server object
   */
  public LogMessage(Ticket ticket, ServerObject object, int type, String messageText)
  {
    if (ticket != null)
    {
      this.principalId = ticket.getUserId();
      this.organisationId = ticket.getOrganisationId();
    }
    else
    {
      this.principalId = "";
      this.organisationId = "";
    }

    this.objectId = object.getId();
    this.messageDate = new Date();
    this.type = type;
    this.messageText = messageText;
  }

  /**
   * Get the database id of this message
   */
  public long getId()
  {
    return id;
  }

  /**
   * Set the database id of this message
   */
  public void setId(long id)
  {
    this.id = id;
  }

  /**
   * Get the organisation id that this message belongs to
   */
  public String getOrganisationId()
  {
    return organisationId;
  }

  /**
   * Set the organisation id that this message belongs to
   */
  public void setOrganisationId(String organisationId)
  {
    this.organisationId = organisationId;
  }

  /**
   * Get the id of the object that this message refers to
   */
  public String getObjectId()
  {
    return objectId;
  }

  /**
   * Set the id of the object that this message refers to
   */
  public void setObjectId(String objectId)
  {
    this.objectId = objectId;
  }

  /**
   * Get the ID of the user / group that this message refers to
   */
  public String getPrincipalId()
  {
    return principalId;
  }

  /**
   * Set the ID of the user / group that this message refers to
   */
  public void setPrincipalId(String principalId)
  {
    this.principalId = principalId;
  }

  /**
   * Get the message type
   */
  public int getType()
  {
    return type;
  }

  /**
   * Set the message type
   */
  public void setType(int type)
  {
    this.type = type;
  }

  /**
   * Get the date for this message
   */
  public Date getMessageDate()
  {
    return messageDate;
  }

  /**
   * Set the date for this message
   */
  public void setMessageDate(Date messageDate)
  {
    this.messageDate = new java.util.Date(messageDate.getTime());
  }

  /**
   * Get the text of this message
   */
  public String getMessageText()
  {
    return messageText;
  }

  /**
   * Set the text of this message
   */
  public void setMessageText(String messageText)
  {
    this.messageText = messageText;
  }
}