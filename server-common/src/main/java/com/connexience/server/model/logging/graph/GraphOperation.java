package com.connexience.server.model.logging.graph;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: nsjw7
 * Date: 13/04/2011
 * Time: 09:39
 * To change this template use File | Settings | File Templates.
 */
public class GraphOperation implements Serializable
{
  /**
   * Database id
   */
  long id;
  /**
   * User ID of the user that is executing the workflow
   */
  private String userId;
  /**
   * Timestamp
   */
  private Date timestamp;

  public GraphOperation()
  {
  }

  public void setId(long id)
  {
    this.id = id;
  }

  public long getId()
  {
    return id;
  }

  public Date getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp(Date timestamp)
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
}
