/*
 * User.java
 */

package com.connexience.server.model.security;

import com.connexience.server.model.*;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * This class represents a User within the system
 *
 * @author hugo
 */
public class User extends ServerObject
{

  /**
   * First name
   */
  private String firstName;

  /**
   * Surname
   */
  private String surname;

  /**
   * Default group for this user when they log on
   */
  private String defaultGroupId;

  /**
   * Default storage folder for this user
   */
  private String homeFolderId = "";

  /**
   * Default web folder for this User
   * */
  private String webFolderId = "";

  /**
   * Inbox for the user
   * */
  private String inboxFolderId;

  /**
   * Sent messages folder for the user
   * */
  private String sentMessagesFolderId;

  /**
   * External objects folder ID for the user. These are objects that external
   * applications have registered as objects that have their access control
   * managed by this system
   */
  private String externalObjectsFolderId;
  
  /*
  * The profile Id for this user.  Contains more information on the user
  * */
  private String profileId = "";

  /**
   * The folder containing this users workflow runs */
  private String workflowFolderId;

  /**
   * User key used by the API to make calls as this user
   */
  private String hashKey = null;

  // =========================================================================
  // Contact and various user details
  // =========================================================================

  /** Telephone number */

  /** Telephone ext */

  /** E-Mail addresss */


  /**
   * Creates a new instance of User
   */
  public User()
  {
    super();
  }

  /*
  * Get the profile Id for this user
  * */
  public String getProfileId()
  {
    return profileId;
  }
  /*
  * Set the profile Id for this user 
  * */
  public void setProfileId(String profileId)
  {
    this.profileId = profileId;
  }

  /**
   * Get the home folder id for this User
   */
  public String getHomeFolderId()
  {
    return homeFolderId;
  }

  /**
   * Set the home folder id for this User
   */
  public void setHomeFolderId(String homeFolderId)
  {
    this.homeFolderId = homeFolderId;
  }

  /*
  * Get the web folder for this User.
  * */
  public String getWebFolderId()
  {
    return webFolderId;
  }

  /*
  * Set the web folder for this user
  * */
  public void setWebFolderId(String webFolderId)
  {
    this.webFolderId = webFolderId;
  }

  /**
   * Get the first name of this user
   */
  public String getFirstName()
  {
    return firstName;
  }

  /**
   * Get the first name of this user
   */
  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  /**
   * Get the surname of this user
   */
  public String getSurname()
  {
    return surname;
  }

  /**
   * Set the surname of this user
   */
  public void setSurname(String surname)
  {
    this.surname = surname;
  }

  /**
   * Get the id of the default group for this user. This is the first group assigned during login
   */
  public String getDefaultGroupId()
  {
    return defaultGroupId;
  }

  /**
   * Get the id of the default group for this user. This is the first group assigned during login
   */
  public void setDefaultGroupId(String defaultGroupId)
  {
    this.defaultGroupId = defaultGroupId;
  }

  public String getWorkflowFolderId()
  {
    return workflowFolderId;
  }

  public void setWorkflowFolderId(String workflowFolderId)
  {
    this.workflowFolderId = workflowFolderId;
  }

  public String getInboxFolderId()
  {
    return inboxFolderId;
  }

  public void setInboxFolderId(String inboxFolderId)
  {
    this.inboxFolderId = inboxFolderId;
  }

  public String getSentMessagesFolderId()
  {
    return sentMessagesFolderId;
  }

  public void setSentMessagesFolderId(String sentMessagesFolderId)
  {
    this.sentMessagesFolderId = sentMessagesFolderId;
  }


  public String getDisplayName()
  {
    return StringEscapeUtils.escapeHtml(this.getFirstName() + " " + this.getSurname());
  }

    public String getExternalObjectsFolderId() {
        return externalObjectsFolderId;
    }

    public void setExternalObjectsFolderId(String externalObjectsFolderId) {
        this.externalObjectsFolderId = externalObjectsFolderId;
    }

    public String getHashKey() {
        return hashKey;
    }

    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }

    
}