package com.connexience.server.model.social;

import com.connexience.server.model.ServerObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: Simon
 * Date: 15-Jul-2008
 *
 * Link betwen two objects in the social networking system
 *
 */
public class Link extends ServerObject implements Serializable
{
  private String sourceObjectId;
  private String sinkObjectId;
  private Date timestamp;

  public Link()
  {
    timestamp = new Date();
  }

  public String getSourceObjectId()
  {
    return sourceObjectId;
  }

  public void setSourceObjectId(String sourceObjectId)
  {
    this.sourceObjectId = sourceObjectId;
  }

  public String getSinkObjectId()
  {
    return sinkObjectId;
  }

  public void setSinkObjectId(String sinkObjectId)
  {
    this.sinkObjectId = sinkObjectId;
  }


  public Date getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp(Date timestamp)
  {
    this.timestamp = timestamp;
  }
}
