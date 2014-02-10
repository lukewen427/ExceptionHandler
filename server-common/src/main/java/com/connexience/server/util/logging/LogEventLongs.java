package com.connexience.server.util.logging;

import java.io.Serializable;

/**
 * Author: Simon
 * Date: Jan 14, 2010
 */
public class LogEventLongs implements Serializable, ILogEvent
{
  private String id;
  private long timestamp;
  private long userId;
  private String operation;
  private long param ;
  private long param2;

  public LogEventLongs()
  {
  }

  public LogEventLongs(long timestamp, long userId, String operation)
  {
    this.timestamp = timestamp;
    this.userId = userId;
    this.operation = operation;
  }

  public LogEventLongs(long timestamp, long userId, String operation, long param)
  {

    this.timestamp = timestamp;
    this.userId = userId;
    this.operation = operation;
    this.param = param;
  }

  public LogEventLongs(long timestamp, long userId, String operation, long param, long param2)
  {
    this.timestamp = timestamp;
    this.userId = userId;
    this.operation = operation;
    this.param = param;
    this.param2 = param2;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public long getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp(long timestamp)
  {
    this.timestamp = timestamp;
  }

  public long getUserId()
  {
    return userId;
  }

  public void setUserId(long userId)
  {
    this.userId = userId;
  }

  public String getOperation()
  {
    return operation;
  }

  public void setOperation(String operation)
  {
    this.operation = operation;
  }

  public long getParam()
  {
    return param;
  }

  public void setParam(long param)
  {
    this.param = param;
  }

  public long getParam2()
  {
    return param2;
  }

  public void setParam2(long param2)
  {
    this.param2 = param2;
  }
}
