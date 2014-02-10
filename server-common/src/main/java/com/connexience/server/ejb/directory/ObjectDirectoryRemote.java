package com.connexience.server.ejb.directory;

import com.connexience.server.ConnexienceException;
import com.connexience.server.model.security.Ticket;
import com.connexience.server.model.ServerObject;
import com.connexience.server.model.image.ImageData;

import javax.ejb.Remote;
import java.util.List;

/**
 * This Interface can be used to query the system and retrieve sets of objects that satisfy certain parameters
 * For instance, all the objects a user has access to.  All the objects that are shared with a user etc.
 * <p/>
 * Author: Simon
 * Date: Jan 29, 2009
 */
@Remote
public interface ObjectDirectoryRemote
{
//  ServerObject getServerObject(Ticket ticket, String id) throws ConnexienceException;

  ServerObject getServerObject(Ticket ticket, String id, Class type) throws ConnexienceException;

  public List getOwnedObjects(Ticket ticket, String userId, Class type, int start, int maxResults) throws ConnexienceException;

  List getAllContainedObjectsUserHasAccessTo(Ticket ticket, String containerId, Class type, int start, int maxResults) throws ConnexienceException;
  
  List getAllObjectsUserHasAccessTo(Ticket ticket, String userId, Class type, int start, int maxResults) throws ConnexienceException;

  List getAllObjectsUserHasAccessTo(Ticket ticket, String userId, Class type, String accessType, int start, int maxResults) throws ConnexienceException;

  long getNumberOfObjectsUserHasAccessTo(Ticket ticket, String userId, Class type) throws ConnexienceException;

  long getNumberOfObjectsUserHasAccessTo(Ticket ticket, String userId, Class type, String accessType) throws ConnexienceException;

  long getNumberOfObjects(String type) throws ConnexienceException;
  
  /**
  * Get the server objects that a user has had shared with them.
  */
 public List getSharedObjectsUserHasAccessTo(Ticket ticket, String userId, Class type,int start, int maxResults) throws ConnexienceException;


  /**
   * Set an image for a server object
   * */
  ImageData setImageForServerObject(Ticket ticket, String serverObjectId, byte[] data, String type) throws ConnexienceException;

  /**
   * Get an image associated with a server object.  Will return the first image found even if there are more than one
   * */
  ImageData getImageForServerObject(Ticket ticket, String serverObjectId) throws ConnexienceException;

  /**
   * Get an image with a particular id
   * */
  ImageData getImageFromId(Ticket ticket, String imageId) throws ConnexienceException;

  /**
   * Get an image for a serverobject that has a particular type
   * */
  ImageData getImageForServerObject(Ticket ticket, String serverObjectId, String type) throws ConnexienceException;

  /**
   * Get the number of server objects of a particular type that are owned by a user.
    */
  long getNumberOfOwnedObjects(Ticket ticket, String userId, Class type) throws ConnexienceException;

  /**
   * Get the objects that a user has access to whose container has a certain id.
   */
  List getContainedObjectsUserHasAccessTo(Ticket ticket, String containerId, Class type, int start, int maxResults) throws ConnexienceException;

  /**
   * Get the server objects that a user has explicitly shared with them.  Does not include objects whose container is shared
   */
  List getExplicitlySharedObjectsUserHasAccessTo(Ticket ticket, String userId, Class type, int start, int maxResults) throws ConnexienceException;

  /**
   * Get the server objects that have been explicitly shared with a user by anybody
   */
  List getExplicitlySharedObjectsUserHasAccesTo(Ticket ticket, Class type, int start, int maxResults) throws ConnexienceException;
  
  /**
   * Get all the server objects of a particular type that are owned by a user.
  */
  List getOwnedObjectsOrderByTimeDesc(Ticket ticket, String userId, Class type, int start, int maxResults) throws ConnexienceException;
  
  /** Change the ID of an object */
  int changeObjectId(Ticket ticket, String originalId, String targetId) throws ConnexienceException;

    long getNumberOfPublicObjects(Ticket ticket, String userId) throws ConnexienceException;

    long getNumberOfSharedFiles(Ticket ticket, String userId) throws ConnexienceException;
}
