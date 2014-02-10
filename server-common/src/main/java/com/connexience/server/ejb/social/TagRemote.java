package com.connexience.server.ejb.social;

import com.connexience.server.model.social.Tag;
import com.connexience.server.model.security.Ticket;
import com.connexience.server.model.ServerObject;
import com.connexience.server.ConnexienceException;

import javax.ejb.Remote;
import java.util.Collection;

/**
 * Author: Simon
 * Date: Jun 3, 2009
 *
 * This bean deals with adding and removing tags from objects
 */
@Remote
public interface TagRemote
{
  /*
 * Add a com.connexience.server.social.tag to a server object
 * */
  Tag addTag(Ticket ticket, ServerObject so, String tag) throws ConnexienceException;

  /*
 * Get all of the tags for a server object
 * */
  Collection getTags(Ticket ticket, ServerObject so) throws ConnexienceException;

  /*
 * Get a com.connexience.server.social.tag from an id
 * */
  Tag getTag(Ticket ticket, String tagId) throws ConnexienceException;

  /*
 * Remove a com.connexience.server.social.tag from a server object
 * */
  void removeTag(Ticket ticket, ServerObject so, Tag tag) throws ConnexienceException;
}
