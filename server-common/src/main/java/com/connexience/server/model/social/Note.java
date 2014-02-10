package com.connexience.server.model.social;

import com.connexience.server.model.ServerObject;

import java.io.Serializable;

/**
 * Author: Simon
 * Date: 15-Jul-2008
 * <p/>
 * Generic Note that might be associated with a blog post, publication, event
 */
public class Note extends ServerObject implements Serializable
{
  // Title of the note
  private String title;

  // Body of the note
  private String body;

  public Note()
  {
    super();
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getBody()
  {
    return body;
  }

  public void setBody(String body)
  {
    this.body = body;
  }

  public String getObjectType()
  {
    return "Note";
  }
}
