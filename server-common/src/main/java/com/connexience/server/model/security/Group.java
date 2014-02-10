/*
 * Group.java
 */

package com.connexience.server.model.security;

import com.connexience.server.model.folder.*;
import com.connexience.server.model.ServerObject;

/**
 * This class represents a group of users
 *
 * @author hugo
 */
public class Group extends ServerObject
{

  /**
  * Indicates whether this group is a special group or not.
  * The default users and admin groups are protected and will not
  * show in lists on the website
  * */
  private boolean protectedGroup = false;

  /**
   * ID of the profile for this group
   * */
  private String profileId;

  /*
  * Should the group admin (creator) have to explicitly allow people to join the group?
  * */
  private boolean adminApproveJoin = true;

  /*
  * Can the members of this group be listed by non-members?
  * */
  private boolean nonMembersList = true;

  /**
   * Folder for the DocumentRecordLinks for this group
   */
  private String dataFolder;

  /**
   * Folder for any events stored in this group
   */
  private String eventsFolder;

  /**
   * Creates a new instance of Group
   */
  public Group()
  {
    super();
  }

  public boolean isProtectedGroup()
  {
    return protectedGroup;
  }

  public void setProtectedGroup(boolean protectedGroup)
  {
    this.protectedGroup = protectedGroup;
  }

  public String getProfileId()
  {
    return profileId;
  }

  public void setProfileId(String profileId)
  {
    this.profileId = profileId;
  }

  public boolean isAdminApproveJoin()
  {
    return adminApproveJoin;
  }

  public void setAdminApproveJoin(boolean adminApproveJoin)
  {
    this.adminApproveJoin = adminApproveJoin;
  }

  public boolean isNonMembersList()
  {
    return nonMembersList;
  }

  public void setNonMembersList(boolean nonMembersList)
  {
    this.nonMembersList = nonMembersList;
  }

  public String getDataFolder()
  {
    return dataFolder;
  }

  public void setDataFolder(String dataFolder)
  {
    this.dataFolder = dataFolder;
  }

  public String getEventsFolder()
  {
    return eventsFolder;
  }

  public void setEventsFolder(String eventsFolder)
  {
    this.eventsFolder = eventsFolder;
  }
}
