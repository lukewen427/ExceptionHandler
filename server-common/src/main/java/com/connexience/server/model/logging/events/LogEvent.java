package com.connexience.server.model.logging.events;


import java.io.Serializable;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class LogEvent implements Serializable
{

  private String id;

  private Long timestamp;

  private String principalId;

  private String principalName;

  /***
   * Non-persistent field that can be used to store text for this log message to be displayed.  Populated by LogEventBean
   */
  private String text;

  public LogEvent()
  {
  }

  public LogEvent(Long timestamp, String principalId, String principalName)
  {
    this.timestamp = timestamp;
    this.principalId = principalId;
    this.principalName = principalName;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public Long getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp(Long timestamp)
  {
    this.timestamp = timestamp;
  }

  public String getPrincipalId()
  {
    return principalId;
  }

  public void setPrincipalId(String principalId)
  {
    this.principalId = principalId;
  }

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    this.text = text;
  }

  public String getPrincipalName()
  {
    return principalName;
  }

  public void setPrincipalName(String principalName)
  {
    this.principalName = principalName;
  }
}
