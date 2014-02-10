package com.connexience.server.model.social;

import java.io.Serializable;

/**
 * Author: Simon
 * Date: Jun 3, 2009
 *
 * This class represents a tag that can be added to a server object
 */
public class Tag implements Serializable
{

  /*
  * The id of the com.connexience.server.social.tag
  * */
  private String id;

  /*
  * The text of the com.connexience.server.social.tag
  * */
  private String tagText;

  public Tag()
  {
  }

  public Tag(String tag)
  {
    this.tagText = tag;
  }

  public String getTagText()
  {
    return tagText;
  }

  public void setTagText(String tagText)
  {
    this.tagText = tagText;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }
}
