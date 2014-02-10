package com.connexience.server.model.notifcations;

import com.connexience.server.model.security.Ticket;
import com.connexience.server.model.social.blog.Comment;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: martyn
 * Date: 25-Nov-2009
 * Time: 13:01:18
 * To change this template use File | Settings | File Templates.
 */
public class BlogPostCommentNotification extends Notification implements Serializable 
{
  public Comment comment;

  public BlogPostCommentNotification()
  {
  }

  public BlogPostCommentNotification(Ticket ticket, String userId, Comment comment)
  {
    setTicket(ticket);
    setUserId(userId);
    setComment(comment);
  }

  public Comment getComment()
  {
    return comment;
  }

  public void setComment(Comment comment)
  {
    this.comment = comment;
  }
}