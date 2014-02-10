/*
 * LogonDetails.java
 */

package com.connexience.server.model.security;

/**
 * This class represents a logon configuration for a User
 *
 * @author hugo
 */
public class LogonDetails
{
  /**
   * ID of logon object
   */
  private long id;

  /**
   * Logon name. This is used as the database id
   */
  private String logonName;

  /**
   * User ID
   */
  private String userId;

  /**
   * Password
   */
  private String hashedPassword;


  /**
   * Creates a new instance of LogonDetails
   */
  public LogonDetails()
  {
  }

  /**
   * Get the id of this logon object
   */
  public long getId()
  {
    return id;
  }

  /**
   * Set the id of this logon object
   */
  public void setId(long id)
  {
    this.id = id;
  }

  /**
   * Get the user logon name
   */
  public String getLogonName()
  {
    return logonName;
  }

  /**
   * Set the user logon name
   */
  public void setLogonName(String logonName)
  {
    this.logonName = logonName;
  }

  /**
   * Get the database id of the user
   */
  public String getUserId()
  {
    return userId;
  }

  /**
   * Set the database id of the user
   */
  public void setUserId(String userId)
  {
    this.userId = userId;
  }


  /**
   * Get the hashed password of this logon
   */
  public String getHashedPassword()
  {
    return hashedPassword;
  }

  /**
   * Set the hashed password of this logon
   */
  public void setHashedPassword(String hashedPassword)
  {
    this.hashedPassword = hashedPassword;
  }
}
