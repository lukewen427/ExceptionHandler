package com.connexience.server.model.social.blog;

import com.connexience.server.model.ServerObject;

import java.io.Serializable;

/**
 * Author: Simon
 * Date: 01-Jul-2008
 */
public class Blog extends ServerObject implements Serializable
{

// Title of the blog
  private String title;

// one line description (or tagline)
  private String description;

  //unique shortname for the blog.  Used to build URLs for the RSS
  private String shortName;

  //A private URL for the blog that doesn't have access control applied
  private String privateURL;

  //the number of posts in this blog
  private Long numPosts;

  //The number of posts that the logged in user has not read in this blog
  private Long unreadCount;

  //The date of the last post
  private Long dateOfLastPost;

  public Blog()
  {
    super();
  }

  public String getObjectType()
  {
    return "Blog";
  }


  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getShortName()
  {
    return shortName;
  }

  public void setShortName(String shortName)
  {
    this.shortName = shortName;
  }

  public String getPrivateURL()
  {
    return privateURL;
  }

  public void setPrivateURL(String privateURL)
  {
    this.privateURL = privateURL;
  }

  public Long getNumPosts()
  {
    return numPosts;
  }

  public void setNumPosts(Long numPosts)
  {
    this.numPosts = numPosts;
  }

  public Long getUnreadCount()
  {
    return unreadCount;
  }

  public void setUnreadCount(Long unreadCount)
  {
    this.unreadCount = unreadCount;
  }

  public Long getDateOfLastPost()
  {
    return dateOfLastPost;
  }

  public void setDateOfLastPost(Long dateOfLastPost)
  {
    this.dateOfLastPost = dateOfLastPost;
  }

  @Override
  public String getDisplayName()
  {
    return getTitle();
  }
}
