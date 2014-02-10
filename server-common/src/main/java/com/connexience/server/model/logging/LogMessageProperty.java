package com.connexience.server.model.logging;

import java.io.Serializable;

/**
 * Author: Simon
 * Date: Jun 30, 2009
 *
 * This class represents a name=value attribute pair for a log message
 */
public class LogMessageProperty implements Serializable
{

  private long id;

  private long logMessageId;

  private String name;

  private String value;

  public LogMessageProperty()
  {
  }

  public LogMessageProperty(long logMessageId, String name, String value)
  {
    this.logMessageId = logMessageId;
    this.name = name;
    this.value = value;
  }

  public long getId()
  {
    return id;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  public long getLogMessageId()
  {
    return logMessageId;
  }

  public void setLogMessageId(long logMessageId)
  {
    this.logMessageId = logMessageId;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }
}
