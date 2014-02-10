package com.connexience.server.util.logging;

/**
 * Author: Simon
 * Date: Jan 14, 2010
 */
public interface ILogEvent
{
  long getTimestamp();

  void setTimestamp(long timestamp);

  String getOperation();

  void setOperation(String operation);
}
