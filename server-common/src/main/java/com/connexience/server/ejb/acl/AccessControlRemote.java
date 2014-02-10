
package com.connexience.server.ejb.acl;

import com.connexience.server.*;
import com.connexience.server.model.*;
import com.connexience.server.model.security.*;

import javax.ejb.Remote;
import java.util.List;
import java.util.Collection;

import org.hibernate.Session;

/**
 * This is the business interface for AccessControl enterprise bean.
 */
@Remote
public interface AccessControlRemote {
    /**
     * Can a ticket add a new resource to the database
     */
    boolean canTicketAddResource(Ticket ticket, ServerObject object) throws ConnexienceException;

    /**
     * Can a ticket read a resource
     */
    boolean canTicketAccessResource(Ticket ticket, ServerObject object, String accessType) throws ConnexienceException;

    /**
     * Get all of the permissions for a specific object
     */
    com.connexience.server.model.security.PermissionList getObjectPermissions(Ticket ticket, String objectId) throws ConnexienceException;

    /**
     * Set the permissions for an object
     */
    void setObjectPermissions(Ticket ticket, String objectId, PermissionList permissions) throws ConnexienceException;

    /**
     * Grant a principal access to a resource. Access is granted if the ticket making
     * the call has write permission on the object in question
     */
    void grantAccess(Ticket ticket, String principalId, String resourceId, String accessType) throws ConnexienceException;

    /**
     * Revoke a permission for a specified principal
     */
    void revokePermission(Ticket ticket, String principalId, String resourceId, String accessType) throws ConnexienceException;
    
    /**
     * Remove all permissions for a specified principal
     */
    void revokeAllPermissions(Ticket ticket, String principalId, String resourceId) throws ConnexienceException;

    /**
     * Propagate security settings from a folder down to all the child folders
     */
    void propagatePermissions(Ticket ticket, String folderId) throws ConnexienceException;

    /**
     * Narrow a list of objects using the Access Control rules
     */
    java.util.List filterList(List objects, Ticket ticket, String permissionType) throws ConnexienceException;


  /*
 * Get a list of the groups that the current user is a member which do not have access to the obbect
 * */
  Collection getGroupsWithoutPermission(Ticket ticket, String serverId) throws ConnexienceException;

  /*
 * Get a list of the users who are connected to the current user and do not have access to the current object
 * */
  List getUsersWithoutPermission(Ticket ticket, String serverId) throws ConnexienceException;

  /**
   * Check to see if there is a specific permission for a ticket to access an object
   */
  PermissionList getObjectPermissionsForTicket( Ticket ticket, String objectId) throws ConnexienceException;

  /**
   * Grant universal access to a resource. Access is granted if the ticket making
   * the call has write permission on the object in question
   */
  void grantUniversalAccess(Ticket ticket, String userId, String resourceId, String accessType) throws ConnexienceException;

  /**
   * Remove all permissions
   */
  void revokeAllPermissions(Ticket ticket,  String resourceId) throws ConnexienceException;

  /**
   * Can a ticket add a new resource to the database
   * @param object
   */
  boolean canTicketAddResource(Session session, Ticket ticket, ServerObject object) throws ConnexienceException;
}
