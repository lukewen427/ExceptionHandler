package com.connexience.server.model.social;

import java.io.Serializable;

/**
 * The visual representation of a tag cloud element - tag text and magnitude etc.
 * User: nsjw7
 * Date: Aug 6, 2009
 * Time: 1:03:45 PM
 */
public class TagCloudElementImpl implements TagCloudElement, Serializable
{
  private String tagText;

  private String fontSize;

  private long weight;

  public TagCloudElementImpl()
  {
  }

  public TagCloudElementImpl(String tagText, long weight)
  {
    this.weight = weight;
    this.tagText = tagText;
  }

  public String getFontSize()
  {
    return fontSize;
  }

  public void setFontSize(String fontSize)
  {
    this.fontSize = fontSize;
  }

  public String getTagText()
  {
    return tagText;
  }

  public void setTagText(String tagText)
  {
    this.tagText = tagText;
  }

  public long getWeight()
  {
    return weight;
  }

  public void setWeight(long weight)
  {
    this.weight = weight;
  }

  public int compareTo(TagCloudElement o)
  {
    return this.tagText.toLowerCase().compareTo(o.getTagText().toLowerCase());
  }
}
