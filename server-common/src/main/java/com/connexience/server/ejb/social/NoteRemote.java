package com.connexience.server.ejb.social;

import com.connexience.server.model.social.Note;
import com.connexience.server.model.security.Ticket;
import com.connexience.server.ConnexienceException;

import javax.ejb.Remote;
import java.util.Collection;

/**
 * Author: Simon
 * Date: 16-Jul-2008
 */
@Remote
public interface NoteRemote
{
  /**
   *  Creates a note with the caller's id set as the owner
   */
  Note createNote(Ticket ticket, String title, String body) throws ConnexienceException;

  /**
   * Update an event with new details
   * */
  Note updateNote(Ticket ticket, String noteId, String title, String body) throws ConnexienceException;

  void deleteNote(Ticket ticket, String noteId) throws ConnexienceException;

  /**
   * Get the notes that a user owns
   * */
  Collection getNotes(Ticket ticket, String userId) throws ConnexienceException;

  /**
   * Get a note from its id
   * */
  Note getNote(Ticket ticket, String noteId) throws ConnexienceException;
}
