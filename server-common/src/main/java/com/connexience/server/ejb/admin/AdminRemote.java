package com.connexience.server.ejb.admin;

import com.connexience.server.model.security.Ticket;
import com.connexience.server.model.security.User;
import com.connexience.server.ConnexienceException;
import com.connexience.server.util.WorkflowDTO;

import javax.ejb.Remote;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Author: Simon
 * Date: Jul 16, 2009
 */
@Remote
public interface AdminRemote
{
  void moveWorkflowFolders(Ticket adminTicket) throws ConnexienceException;

  void createInboxFolders(Ticket adminTicket) throws ConnexienceException;

  void moveNotes(Ticket adminTicket) throws ConnexienceException;

  void moveProfileText(Ticket adminTicket) throws ConnexienceException;

  List<User> mapUsernameToEmail(Ticket ticket) throws ConnexienceException;

  void sendEmailToAllUsers(Ticket ticket, String subject, String content, String contentType) throws ConnexienceException;

  List<User> getAllUsers(Ticket ticket) throws ConnexienceException;

  /**
   * Method to add workflow invocation to users watch proeprty
   */
  String addWorkflowFavourite(Ticket ticket, String workflowId) throws ConnexienceException;

  Collection<WorkflowDTO> getFavouriteWorkfowStats(Ticket ticket, Date start, Date end) throws ConnexienceException;

  /**
   * Method to monitor workflow invocations
   */
  WorkflowDTO getWorkflowStats(Ticket ticket, String workflowId, Date start, Date end) throws ConnexienceException;

  /**
  * Method to remove workflow invocation to users watch proeprty
  */
  void removeWorkflowFavourite(Ticket ticket, String workflowId) throws ConnexienceException;

  void addHashKeyForUsers(Ticket adminTicket) throws ConnexienceException;

  int fixUserHomeFolderOwnser(Ticket adminTicket) throws ConnexienceException;

    Long add1000LogMessages(Ticket adminTicket) throws ConnexienceException;
}