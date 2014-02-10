/*
 * NewsRemote.java
 */

package com.connexience.server.ejb.social;

import com.connexience.server.ConnexienceException;
import com.connexience.server.model.social.*;
import com.connexience.server.model.security.*;
import com.connexience.server.model.ServerObject;

import javax.ejb.Remote;
import java.util.*;

import org.hibernate.Session;

/**
 * This interface defines the behaviour of the NewsItem management bean that
 * is responsible for handling the creation and display of news items in
 * a User's profile.
 *
 * @author hugo
 */
@Remote
public interface NewsRemote
{
  /**
   * Post a new news item
   */
  public void postItem(Ticket ticket, NewsItem item) throws ConnexienceException;

  /**
   * List news items for a specific user profile
   */
  public List listItems(Ticket ticket, int maxCount) throws ConnexienceException;

  public void postUpdates(Ticket ticket, ServerObject so) throws ConnexienceException;

  public String constructLink(ServerObject so);

  /*
 * Method to post updates to users news feeds
 * */
  void postCreatedMessages(Ticket ticket, ServerObject so) throws ConnexienceException;

  /**
   * Get a vector containing the first and last date of news items with numOffset between them.  Allows the timeline to be
   * configured to a better scale
   * @param ticket Users ticket
   * @param numOffset Number of news items to be displayed
   * @return Vector containing dates for the first (newest) and last (oldest) news items
   * @throws com.connexience.server.ConnexienceException something went wrong!
   */
  Collection<Date> getNewsDateOffset(Ticket ticket, int numOffset) throws ConnexienceException;

  /**
   * Get news items relating to a profile
  */
  List<ServerObject> listGroupItems(Ticket ticket, String groupId, int maxCount) throws ConnexienceException;
}