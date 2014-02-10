/*
 * GroupProfile.java
 */

package com.connexience.server.model.social.profile;

import com.connexience.server.model.ServerObject;

import java.io.Serializable;

/**
 * This class represents the profile of a group of users.
 *
 * It is not used at present but a placeholder for the future
 *
 * @author Simon
 */
public class GroupProfile implements Serializable
{
//Id of the profile in the database
  private String id;

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getObjectType()
  {
    return "Group Profile";
  }
}
