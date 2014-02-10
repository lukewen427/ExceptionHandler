package com.connexience.server.ejb.remove;

import javax.ejb.Remote;

import com.connexience.server.*;
import com.connexience.server.model.messages.Message;
import com.connexience.server.model.security.*;
import com.connexience.server.model.organisation.*;
import com.connexience.server.model.document.*;
import com.connexience.server.model.folder.*;
import com.connexience.server.model.workflow.*;
import com.connexience.server.model.properties.*;
import com.connexience.server.model.allowance.*;
import com.connexience.server.model.social.application.*;
import com.connexience.server.model.social.blog.BlogPost;
import com.connexience.server.model.social.blog.Blog;
import com.connexience.server.model.social.blog.Comment;
import com.connexience.server.model.social.Note;
import com.connexience.server.model.social.Event;
import com.connexience.server.model.social.Publication;
import com.connexience.server.model.social.Tag;
import com.connexience.server.model.ServerObject;
import org.hibernate.*;

import java.util.Properties;

/**
 * This is the business interface for ObjectRemoval enterprise bean.
 */
@Remote
public interface ObjectRemovalRemote
{

  /*
   * Utility method to remove a server object.
   *
   * Throws an exception if passed an object that there is not a method to remove in this bean
   * */
   public void remove(Ticket ticket, ServerObject so) throws ConnexienceException;

   /**
    * Remove a dynamic workflow service
    */
   public void remove(DynamicWorkflowService service) throws ConnexienceException;

  /**
   * Remove a document version
   */
  void remove(DocumentVersion version) throws ConnexienceException;

  /**
   * Remove an entire DocumentRecord
   */
  void remove(DocumentRecord doc) throws ConnexienceException;

  /**
   * Remove a group and all of the associated memberships and permissions
   */
  void remove(Group group) throws ConnexienceException;

  /**
   * Remove a document version with a session
   */
  void remove(DocumentVersion version, Session session) throws ConnexienceException;

  /**
   * Remove a Folder
   */
  void remove(Ticket ticket, Folder folder) throws ConnexienceException;

  /**
     * Remove a Folder
     */
  void removeWorkflowFolder(Ticket ticket, User user) throws ConnexienceException;
  /**
   * Remove a DocumentType
   */
  void remove(DocumentType docType) throws ConnexienceException;


  /**
   * Remove a properties group
   */
  void remove(PropertyGroup properties) throws ConnexienceException;

  /**
   * Remove a workflow service
   */
  void remove(BlogPost blogpost) throws ConnexienceException;

  /**
   * Remove a Blog
   */
  void remove(Blog blog) throws ConnexienceException;

  /**
   * Remove a Comment
   */
  void remove(Comment comment) throws ConnexienceException;

  /**
   * Remove a Note
   */
  void remove(Note note) throws ConnexienceException;

  /**
   * Remove a Event
   */
  void remove(Event event) throws ConnexienceException;

  /**
   * Remove an external application
   */
  void remove(ExternalApplication app) throws ConnexienceException;

  /*
  * Remove the XML MetaData Proxy and associated metadata
  * */
  public void remove(XMLMetaDataProxy mdproxy) throws ConnexienceException;

  /*
 * Remove a single tag from a server object
 * */
  void remove(String serverObjectId, Tag tag) throws ConnexienceException;

  /**
   * Remove all of the tags for an item
   */
  void removeTags(Session session, String serverObjectId);

  /**
   * Remove an external object
   */
  void remove(ExternalObject externalObj) throws ConnexienceException;

  /**
   * Remove a product
   */
  void remove(Product product) throws ConnexienceException;

  void remove(Message m) throws ConnexienceException;
  
  void remove(WorkflowFolderTrigger trigger) throws ConnexienceException;
}
