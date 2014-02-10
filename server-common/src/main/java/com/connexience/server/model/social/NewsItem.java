/*
 * NewsItem.java
 */

package com.connexience.server.model.social;

import com.connexience.server.model.ServerObject;
import com.connexience.server.model.security.User;

import java.util.*;
import java.io.*;

/**
 * This class represents a news item in the social networking system. These are
 * generated whenever people become friends, update their profile, post new information
 * etc. They are timestamped, contain details of the originating profile a label
 * and some details.
 *
 * @author hugo
 */
public class NewsItem extends ServerObject implements Serializable
{
  public static final String GRANTED_PERM = "Granted Permission";
  public static final String UPDATED = "Updated";
  public static final String NEW_FILE = "Created";
  public static final String JOIN_GROUP = "Group Membership";
  public static final String NEW_FRIENDS = "New Friendship";
  public static final String UNKNOWN = "Unknown";
  /**
   * Time that this event happened
   */
  private Date timestamp;

  /**
   * Label for this item
   */
  private String label;

  /**
   * Descriptive text for this item
   */
  private String description;

  /**
   * Type of News Item
   */
  private String itemType;

  /**
   * The id of the main item in this news item.  For instance, updating a blog post,
   * this would be the postId, creating a file, it would be the fileId
   */
  private String mainItemId;

  /**
   * Blank constructor
   */
  public NewsItem()
  {
  }

 
  /**
   * Construct with details in place
   */
  public NewsItem(String label, String description, String itemType)
  {
    this.label = label;
    this.description = description;
    this.itemType = itemType;
    this.timestamp = new Date();    
  }

  /**
   * Get the timestamp for this news item
   */
  public Date getTimestamp()
  {
    return timestamp;
  }

  /**
   * Set the timestamp for this news item
   */
  public void setTimestamp(Date timestamp)
  {
    this.timestamp = timestamp;
  }

  /**
   * Get the display label for this news item
   */
  public String getLabel()
  {
    return label;
  }

  /**
   * Set the label for this news item
   */
  public void setLabel(String label)
  {
    this.label = label;
  }

  /**
   * Get the description text for this news item
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Set the description text for this news item
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getItemType()
  {
    return itemType;
  }

  public void setItemType(String itemType)
  {
    this.itemType = itemType;
  }

  public String getMainItemId()
  {
    return mainItemId;
  }

  public void setMainItemId(String mainItemId)
  {
    this.mainItemId = mainItemId;
  }
}