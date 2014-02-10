/*
 * ConnexienceException.java
 */

package com.connexience.server;

import com.connexience.server.ejb.util.EJBLocator;
import com.connexience.server.model.ServerObject;
import com.connexience.server.model.security.Ticket;
import com.connexience.server.model.security.User;

/**
 * This is the base exception class for all ejbs
 *
 * @author hugo
 */
public class ConnexienceException extends java.lang.Exception
{
  /**
   * Unsupported operation message
   */
  public static final String UNSUPPORTED_OPERATION_MESSAGE = "Unsupported operation";

  /**
   * Access denied message
   */
  public static final String ACCESS_DENIED_MESSAGE = "Access denied";

  /**
   * No user logged in message
   */
  public static final String NO_USER_LOGGED_IN_MESSAGE = "No user logged in";

  /**
   * No folder has been specified
   */
  public static final String NO_FOLDER_SPECIFIED_MESSAGE = "No folder has been specified";

  /**
   * No such object message
   */
  public static final String OBJECT_NOT_FOUND_MESSAGE = "Could not locate specified object";

  /**
   * Incorrect organisation message
   */
  public static final String INCORRECT_ORGANIATION_MESSAGE = "Incorrect organisation";

  /**
   * No organisation has been specified
   */
  public static final String NO_ORGANISATION_SPECIFIED_MESSAGE = "No organisation has been specified";

  /**
   * Invalid signature message
   */
  public static final String INVALID_CERTIFICATE_MESSAGE = "Incorrect certificate";

  /**
   * No data store specified message
   */
  public static final String NO_DATA_STORE_MESSGAGE = "No data store available";

  /**
   * Cannot find partnership message
   */
  public static final String CANNOT_FIND_PARTNERSHIP_MESSAGE = "Cannot find requested Partnership";

  /**
   * No partners hold object message
   */
  public static final String CANNOT_LOCATE_PARTNER_OBJECT_MESSAGE = "Cannot locate requested Object in any Partnership";

  /**
   * User must be admin
   */
  public static final String ADMIN_ONLY = "User must be admin to perform this operation";

  /**
   * Creates a new instance of <code>ConnexienceException</code> without detail message.
   */
  public ConnexienceException()
  {
  }


  /**
   * Constructs an instance of <code>ConnexienceException</code> with the specified detail message.
   *
   * @param msg the detail message.
   */
  public ConnexienceException(String msg)
  {
    super(msg);
  }

  public ConnexienceException(Throwable t)
  {
    super(t);
  }

  public ConnexienceException(String message, Throwable t)
  {
    super(message, t);
  }

  public static String formatMessage(Ticket ticket, ServerObject object)
  {
    try
    {
      User user = EJBLocator.lookupUserDirectoryBean().getUser(ticket, ticket.getUserId());
      return "User: " + user.getDisplayName() + " denied access to: " + object.getName();
    }
    catch (ConnexienceException e)
    {
      return "Error getting user: " + ticket.getUserId() + " when trying to create error message";
    }
  }

  public static String formatMessage(User user, ServerObject object)
  {
      return "User: " + user.getDisplayName() + " denied access to: " + object.getName();
  }

}
