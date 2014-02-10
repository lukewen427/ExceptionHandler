package com.connexience.server.ejb.social;

import com.connexience.server.model.messages.Message;
import com.connexience.server.model.messages.TextMessage;
import com.connexience.server.model.security.Ticket;
import com.connexience.server.model.notifcations.messages.NotificationMessage;
import com.connexience.server.ConnexienceException;

import javax.ejb.Remote;
import javax.mail.internet.InternetAddress;
import java.util.Collection;
import java.util.List;

/**
 * Author: Simon
 * Date: Jul 17, 2009
 */
@Remote
public interface MessageRemote
{
  /**
   * Add a message to a users sent messages folder
   * */
  TextMessage addSentTextMessageToFolder(Ticket ticket, String receiverIds, String threadId, String title, String text) throws ConnexienceException;

  /**
   * Get the number of unread messages
   */
  Long getNumberOfUnreadMessages(Ticket ticket) throws ConnexienceException;

  /**
   * Get the messages in a thread
   */
  List<Message> getMessageThread(Ticket ticket, String threadId, int start, int maxResults) throws ConnexienceException;

  /**
   * Create a message from this user to a list of other users
   */
  TextMessage createTextMessage(Ticket ticket, String thisRecipeint, String allRecipients, String threadId, String title, String text) throws ConnexienceException;

  /**
   * Mark a message as read
   * */
  void markThreadAsRead(Ticket ticket, String threadId) throws ConnexienceException;

  Long getNumberOfMessagesInThread(Ticket ticket, String threadId) throws ConnexienceException;

  Long getNumberOfTextMessgesInFolder(Ticket ticket, String folderId) throws ConnexienceException;

  Long getNumberOfUnreadTextMessgesInFolder(Ticket ticket, String folderId) throws ConnexienceException;

  /**
   * Get the contents of the folder
   */
  Collection<Message> getMessages(Ticket ticket, String userId, String folderId, int start, int maxResults) throws ConnexienceException;

  /**
   * Get the TextMessages in this folder
  */
  Collection<Message> getTextMessages(Ticket ticket, String userId, String folderId, int start, int maxResults) throws ConnexienceException;

  void addNotificationMessageToInbox(Ticket ticket, String userId, Message message) throws ConnexienceException;

  void markNotificationMessageRead(Ticket ticket, NotificationMessage message) throws ConnexienceException;

  /**
   * Get the number of Messages the user has.  Threads will only be counted once
   * @param ticket Ticket for the user
   * @param userId user id of the user
   * @param folderId folder to use as the container
   * @return number of messages
   * @throws com.connexience.server.ConnexienceException something went wrong...
   */
  int getNumberOfMessages(Ticket ticket, String userId, String folderId) throws ConnexienceException;
}
