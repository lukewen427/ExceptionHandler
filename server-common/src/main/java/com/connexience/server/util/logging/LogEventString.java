package com.connexience.server.util.logging;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: TempAdmin
 * Date: 31-Dec-2009
 * Time: 21:27:38
 * To change this template use File | Settings | File Templates.
 */
public class LogEventString implements Serializable, ILogEvent
{
  private String id = "";
  private long timestamp;
  private String userId = "";
  private String operation = "";
  private String param = "";
  private String param2 = "";

  public static final String WORKFLOW_ID_UNKOWN = "Workflow id is unknown";
  public static final String GROUP_ID_UNKOWN = "Group id is unknown";
  public static final String  INVOCATION_ID_UNKOWN =  "Invocation id is unkownn";
  
  public String toString()
  {
    return "LogEventString: (t=" + timestamp + ",u=" + userId + ",op=" + operation + ",param=" + param + ",param2=" + param2 + ")";
  }

  public LogEventString()
  {
  }

  public LogEventString(long timestamp, String userId, String operation)
  {
    this.timestamp = timestamp;
    this.userId = userId;
    this.operation = operation;
  }

  public LogEventString(long t, String u, String o, String p)
  {
    timestamp = t;
    userId = u;
    operation = o;
    param = p;
    param2 = "";
  }

  public LogEventString(long t, String u, String o, String p, String p2)
  {
    timestamp = t;
    userId = u;
    operation = o;
    param = p;
    param2 = p2;
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

  public String getUserId()
  {
    return userId;
  }

  public void setUserId(String userId)
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

  public String getParam()
  {
    return param;
  }

  public void setParam(String param)
  {
    this.param = param;
  }

  public String getParam2()
  {
    return param2;
  }

  public void setParam2(String param2)
  {
    this.param2 = param2;
  }
}
