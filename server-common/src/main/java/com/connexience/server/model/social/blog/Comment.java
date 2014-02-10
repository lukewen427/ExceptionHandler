package com.connexience.server.model.social.blog;

import com.connexience.server.model.ServerObject;

import java.util.Date;
import java.io.Serializable;

/**
 * Author: Simon
 * Date: 03-Jul-2008
 */
public class Comment extends ServerObject implements Serializable
{
// Id of the blog post that this comment is attached to
  private String objectId;

// The body of the comment
  private String text;

// database id of the comment
  private String id;

// the time that the comment was left
  private Date timestamp;

  //name of the author who left the comment.  This may be different from the username for public users
  private String authorName;

  // Whether or not the user should be notified if anyone comments the BlogPost this comment has commented on
  private boolean notification;

  public Comment()
  {
    this.timestamp = new Date();
  }

  public String getObjectType()
  {
    return "Comment";
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    this.text = text;
  }

  public String getPostId(){
      return objectId;
  }

  public void setPostId(String postId){
      this.objectId = postId;
  }
  
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }


  public Date getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp(Date timestamp)
  {
    this.timestamp = timestamp;
  }

  public String getAuthorName()
  {
    return authorName;
  }

  public void setAuthorName(String authorName)
  {
    this.authorName = authorName;
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
