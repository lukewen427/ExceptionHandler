package com.connexience.server.model.social.blog;

import com.connexience.server.model.ServerObject;

import java.io.Serializable;
import java.util.Date;

/**
 * User: nsjw7
 * Date: 30-Jun-2008
 * Time: 21:38:36
 */
public class BlogPost extends ServerObject implements Serializable
{
  //database id of this blogpost
  private String blogId;

  /**
   * Title of the blog post
   */
  private String title;

  /**
   * Post body (known as 'description' in RSS2.0
   */
  private String body;

  /**
   * Category of the post
   */
  private String category;

  /**
   * Time and date published
   */
  private Date timestamp;

  /**
   * Summary text that is extracted from the HTML and saved separately
   */
  private String summary;


  /**
   * Has the blog post been read by the currently logged in user
   * */
  private boolean read;

  //the number of comments on this post
  private Long numComments;

  /**
   * Whether or not the owner of this blog post should be notified when this BlogPost is comment on
   */
  private boolean notification;

  /**
   * Default Constructor
   */
  public BlogPost()
  {
  }

    /**
   * Parameterised Constructor
   */
  public BlogPost(String userId, String blogId, String title, String body, String category)
  {
    this.blogId = blogId;
    this.title = title;
    this.body = body;
    this.category = category;
    this.timestamp = new Date();
  }

  public String getObjectType()
  {
    return "Blog Post";
  }

  public String getBlogId()
  {
    return blogId;
  }

  public void setBlogId(String blogId)
  {
    this.blogId = blogId;
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

  public String getCategory()
  {
    return category;
  }

  public void setCategory(String category)
  {
    this.category = category;
  }

  public Date getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp(Date timestamp)
  {
    this.timestamp = timestamp;
  }

  public String getSummary()
  {
    return summary;
  }

  public void setSummary(String summary)
  {
    this.summary = summary;
  }

  public boolean isRead()
  {
    return read;
  }

  public void setRead(boolean read)
  {
    this.read = read;
  }

  public Long getNumComments()
  {
    return numComments;
  }

  public void setNumComments(Long numComments)
  {
    this.numComments = numComments;
  }

    public boolean isNotification()
    {
        return notification;
    }

    public void setNotification(boolean notification)
    {
        this.notification = notification;
    }
}
