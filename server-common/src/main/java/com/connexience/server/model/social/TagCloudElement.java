package com.connexience.server.model.social;

/**
 * This is an interface to an element in a tag cloud.  Used by the TagCloud in Inkspot
 * User: nsjw7
 * Date: Aug 6, 2009
 * Time: 1:08:31 PM
 */
public interface TagCloudElement extends Comparable<TagCloudElement>
{
  String getFontSize();

  void setFontSize(String fontSize);

  String getTagText();

  long getWeight();
}
