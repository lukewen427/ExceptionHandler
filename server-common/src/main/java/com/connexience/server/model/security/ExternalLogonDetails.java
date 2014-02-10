package com.connexience.server.model.security;

import java.io.Serializable;
import java.util.UUID;

/**
 * Author: Simon
 * Date: Mar 24, 2010
 */
public class ExternalLogonDetails implements Serializable
{
  /**
   * The id of this object in the database
   */
  private long id;

  /**
   * The user id that this external id is for
   */
  private String userId;

  /**
   * The id provided by the external site such as an OpenId
   */
  private String externalUserId;

  /**
   * A temporary id that is created and put in a form for the user to fill in their name
   * or confirm linking to an existing account.  Deleted when User account created or linked.
   */
  private String temporaryId;

  public ExternalLogonDetails()
  {
  }

  public ExternalLogonDetails(String userId, String externalUserId)
  {
    this.userId = userId;
    this.externalUserId = externalUserId;
    this.temporaryId = UUID.randomUUID().toString();
  }

  public long getId()
  {
    return id;
  }

  public void setId(long id)
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

  public String getExternalUserId()
  {
    return externalUserId;
  }

  public void setExternalUserId(String externalUserId)
  {
    this.externalUserId = externalUserId;
  }

  public String getTemporaryId()
  {
    return temporaryId;
  }

  public void setTemporaryId(String temporaryId)
  {
    this.temporaryId = temporaryId;
  }
}
