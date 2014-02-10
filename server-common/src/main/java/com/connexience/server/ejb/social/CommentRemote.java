/*
 * CommentRemote.java
 */

package com.connexience.server.ejb.social;

import com.connexience.server.model.security.Ticket;
import com.connexience.server.model.social.blog.Comment;
import com.connexience.server.ConnexienceException;

import javax.ejb.Remote;
import java.util.List;

/**
 * This interface defines a bean that can manage comments attached to server objects.
 * @author hugo
 */
@Remote
public interface CommentRemote {
  /**
   * Create a comment for a server object
   */
  Comment createComment(Ticket ticket, String objectId, String text) throws ConnexienceException;

  /**
   * Get the comments attached to a server object
   */
  List getComments(Ticket ticket, String objectId) throws ConnexienceException;

  /**
   * Update the details of a comment - throws an exception if the comment does not exist
   */
  Comment updateComment(Ticket ticket, String commentId, String text) throws ConnexienceException;

  /**
   * Delete a comment
   */
  void deleteComment(Ticket ticket, String commentId) throws ConnexienceException;

  /**
   * Get a comment
   */
  Comment getComment(Ticket ticket, String commentId) throws ConnexienceException;
  
  /**
   * Create a comment on a server object
   */
  Comment createComment(Ticket ticket, String objectId, String text, String authorName) throws ConnexienceException;

  /**
   * Get the number of comments for an object
   */
  Long getNumberOfComments(Ticket ticket, String objectId);
}
