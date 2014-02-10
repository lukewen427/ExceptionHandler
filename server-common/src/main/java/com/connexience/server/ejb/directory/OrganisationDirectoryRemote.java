package com.connexience.server.ejb.directory;

import com.connexience.server.ConnexienceException;
import com.connexience.server.ejb.*;

import javax.ejb.Remote;

import com.connexience.server.model.organisation.*;
import com.connexience.server.model.security.*;

import java.util.List;

/**
 * This is the business interface for OrganisationDirectorybean enterprise bean.
 */
@Remote
public interface OrganisationDirectoryRemote
{
  /**
   * List all of the organisations
   */
  java.util.List listOrganisations(Ticket ticket) throws ConnexienceException;

  /**
   * Save an organisation to the database
   */
  Organisation saveOrganisation(Ticket ticket, Organisation org) throws ConnexienceException;

  /**
   * Get an organisation by id
   */
  com.connexience.server.model.organisation.Organisation getOrganisation(Ticket ticket, String organisationId) throws ConnexienceException;

  /**
   * List all of the groups in an organisation
   */
  public List listOrganisationGroups(Ticket ticket, int start, int maxResults) throws ConnexienceException;

  /**
   * Get an organisation by name
   */
  com.connexience.server.model.organisation.Organisation getOrganisationByName(Ticket ticket, String organisationId) throws ConnexienceException;

  /**
   * Search for organisations
   */
  java.util.List searchOrganisations(Ticket ticket, String searchText) throws ConnexienceException;

  /*
  * List all of the users within an organisation, limited by the start parameter and maximum number of results to return
  **/
  public List listOrganisationUsers(Ticket ticket, int start, int maxResults) throws ConnexienceException;

  /*
  * Get the number of users within an organisation
  **/
  public int numberOfOrganisationUsers(Ticket ticket) throws ConnexienceException;

  /*
  * Get the number of groups within an organisation
  **/
  public int numberOfOrganisationGroups(Ticket ticket) throws ConnexienceException;

  /**
   * List all of the groups in an organisation that are not protected
  */
  List listOrganisationNonProtectedGroups(Ticket ticket, int start, int maxResults) throws ConnexienceException;

  /**
   * get the number of groups in an organisation
*/
  int numberOfOrganisationNonProtectedGroups(Ticket ticket) throws ConnexienceException;

  /** List all of the groups in an organisation */
  List listOrganisationGroups(Ticket ticket, String organisationId) throws ConnexienceException;

  /** Get the default organisation */
  public Organisation getDefaultOrganisation(Ticket ticket) throws ConnexienceException;
  
  public Organisation setupNewOrganisation(String rootUser, String password, String name, String storageDir, String adminGroup, String userGroup, String adminFirstname, String adminLastname, String adminUsername, String adminPassword) throws ConnexienceException;
}
