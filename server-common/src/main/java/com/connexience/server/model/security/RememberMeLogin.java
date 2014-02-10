package com.connexience.server.model.security;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: Simon
 * Date: Mar 18, 2010
 *
 * Represents a saved login from a users machine.  Maps user Id to a unique id stored in a cookie.
 */
public class RememberMeLogin implements Serializable
{
  private String id;

  private String userId;

  private String cookieId;

  private Date expiryDate;

  public RememberMeLogin()
  {
  }

  public RememberMeLogin(String userId, String cookieId, Date expiryDate)
  {
    this.userId = userId;
    this.cookieId = cookieId;
    this.expiryDate = expiryDate;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId(String userId)
  {
    this.userId = userId;
  }

  public String getCookieId()
  {
    return cookieId;
  }

  public void setCookieId(String cookieId)
  {
    this.cookieId = cookieId;
  }

  public Date getExpiryDate()
  {
    return expiryDate;
  }

  public void setExpiryDate(Date expiryDate)
  {
    this.expiryDate = expiryDate;
  }
}
