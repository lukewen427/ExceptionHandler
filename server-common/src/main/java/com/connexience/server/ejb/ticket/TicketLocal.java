/*
 * TicketLocal.java
 *
 */

package com.connexience.server.ejb.ticket;
import com.connexience.server.model.security.*;
import com.connexience.server.*;

import java.util.*;
import javax.ejb.Local;

/**
 * local interface for ticket bean
 * @author hugo
 */
@Local
public interface TicketLocal {
    /**
     * Create a ticket for a username and password
     */
    com.connexience.server.model.security.TicketDataContainer acquireTicket(String username, String password) throws ConnexienceException;

    /**
     * Create a ticket with specified groups
     */
    com.connexience.server.model.security.TicketDataContainer acquireTicket(String username, String password, List groupIds) throws ConnexienceException;

    /**
     * Is a ticket valid
     */
    boolean isTicketValid(TicketData ticketData) throws ConnexienceException;

    /**
     * Release a ticket
     */
    void releaseTicket(TicketData ticketData) throws ConnexienceException;

    /**
     * List the groups that a ticket has registered for
     */
    java.util.List listTicketPrincpials(TicketData ticketData) throws ConnexienceException;

    /**
     * Add a group to a ticket
     */
    void acquireGroup(TicketData ticketData, String groupId) throws ConnexienceException;

    /**
     * Release a group from a ticket
     */
    void releaseGroup(TicketData ticketData, String groupId) throws ConnexienceException;

    /**
     * List the principals that a ticket has registered for
     */
    java.util.List listTicketPrincipals(String ticketId) throws ConnexienceException;

    /**
     * Does a ticket have a group associated with it
     */
    boolean ticketHasGroup(Ticket ticket, String groupId) throws ConnexienceException;
    
    /** Get the actual ticket */
    public Ticket getTicket(String ticketId) throws ConnexienceException;    

    /**
     * Switch organisations associated with a ticket. This only works with a Super Ticket
     */
    com.connexience.server.model.security.TicketDataContainer switchOrganisations(TicketData ticketData, String organisationId) throws ConnexienceException;

    /**
     * List the groups that a ticket is currently associated with
     */
    java.util.List listTicketGroups(Ticket ticket) throws ConnexienceException;

    /**
     * List the group ids associated with a ticket as a String[] array
     */
    java.lang.String[] listTicketGroupIds(Ticket ticket) throws ConnexienceException;

    /**
     * List the IDs of all of the ticket principals
     */
    java.lang.String[] listTicketPrincipalIds(Ticket ticket) throws ConnexienceException;

    /**
     * Create a ticket for a username. This is used by the web pages, as the user has already
     * logged on using their browser
     */
    com.connexience.server.model.security.WebTicket createWebTicket(String username) throws ConnexienceException;
    
    /**
     * Create a ticket for a user given a database id. This is used by the social networking
     * webservice.
     */
    com.connexience.server.model.security.WebTicket createWebTicketForDatabaseId(String id) throws ConnexienceException;
    
    /** Clone a ticket and pass back a new TicketDataContainer object. This is used
     * when compute jobs are created */
    com.connexience.server.model.security.TicketDataContainer cloneTicket(Ticket ticket) throws ConnexienceException;

  /**
   * Allow an admin user to change users.  Returns a ticket with the other users credentials.
 */
  WebTicket switchUsers(Ticket ticket, String otherUserId) throws ConnexienceException;

  /**
   * Set a value that allows a user to login automatically
   */
  String addRememberMe(Ticket ticket) throws ConnexienceException;

  /**
  * Set a value that allows a user to login automatically
  */
  RememberMeLogin checkRememberMe(String cookieId) throws ConnexienceException;

  /**
     * Set a value that allows a user to login automatically
     */
  void deleteRememberMe(Ticket ticket, String cookieId) throws ConnexienceException;

  ExternalLogonDetails getExternalLogon( String externalUserId) throws ConnexienceException;

  ExternalLogonDetails addExternalLogon(String userId, String externalUserId) throws ConnexienceException;
}