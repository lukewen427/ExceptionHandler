package com.connexience.server.ejb.directory;

import javax.ejb.Remote;

import com.connexience.server.model.*;
import com.connexience.server.model.social.profile.GroupProfile;
import com.connexience.server.model.security.*;
import com.connexience.server.*;

import java.util.*;

/**
 * This is the business interface for GroupDirectory enterprise bean.
 */
@Remote
public interface GroupDirectoryRemote
{
  /**
   * Save a group
   */
  com.connexience.server.model.security.Group saveGroup(Ticket ticket, Group group) throws ConnexienceException;

   /**
   * Get a group by Id
   */
  com.connexience.server.model.security.Group getGroup(Ticket ticket, String groupId) throws ConnexienceException;

  /**
   * Add a user to a group
   */
  void addUserToGroup(Ticket ticket, String userId, String groupId) throws ConnexienceException;

  /**
   * Remove a user from a group
   */
  void removeUserFromGroup(Ticket ticket, String userId, String groupId) throws ConnexienceException;

  /**
   * List all of the users that are part of a specific group
   */
  java.util.List listGroupMembers(Ticket ticket, String groupId) throws ConnexienceException;

  /**
   * Search for groups
   */
  java.util.List searchForGroups(Ticket ticket, String searchText) throws ConnexienceException;

  /**
   * Remove a group
   */
  void removeGroup(Ticket ticket, String groupId) throws ConnexienceException;

  /*
  * Get the number of members of a group
  * */
  Long numberOfGroupMembers(Ticket ticket, String groupId) throws ConnexienceException;

  /*
  * Save the profile of a group
  * */
  public GroupProfile saveGroupProfile(Ticket ticket, GroupProfile groupProfile, String groupId) throws ConnexienceException;

  /** List groups - left for backwards compatibility with AxisWSDirectory */
  List listGroups(Ticket ticket) throws ConnexienceException;

  /**
   * List all of the users that are part of a specific group
   */
  List listGroupMembers(Ticket ticket, String groupId, int start, int numResults) throws ConnexienceException;

  /**
   * Get some basic statistics about a group. Includes number of shared files, events and members
   */
  public HashMap getGroupStatistics(Ticket ticket, String groupId) throws ConnexienceException;
}
