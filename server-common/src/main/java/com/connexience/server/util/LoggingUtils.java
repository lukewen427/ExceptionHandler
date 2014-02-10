package com.connexience.server.util;

import com.connexience.server.ConnexienceException;
import com.connexience.server.ejb.util.EJBLocator;
import com.connexience.server.model.ServerObject;
import com.connexience.server.model.document.DocumentRecord;
import com.connexience.server.model.folder.Folder;
import com.connexience.server.model.logging.events.ObjectType;
import com.connexience.server.model.logging.events.PrincipalType;
import com.connexience.server.model.messages.Message;
import com.connexience.server.model.security.Group;
import com.connexience.server.model.security.Ticket;
import com.connexience.server.model.security.User;
import com.connexience.server.model.social.Note;
import com.connexience.server.model.social.blog.Blog;
import com.connexience.server.model.social.blog.BlogPost;
import com.connexience.server.model.social.blog.Comment;
import com.connexience.server.model.workflow.WorkflowDocument;

import javax.ejb.EJB;

/**
 * Author: Simon
 * Date: Feb 10, 2010
 */
public class LoggingUtils
{

  public static String getObjectType(Ticket ticket, ServerObject object) throws ConnexienceException
  {
    if (object instanceof WorkflowDocument)
    {
      return ObjectType.WORKFLOW;
    }
    else if (object instanceof Blog)
    {
      return ObjectType.BLOG;
    }
    else if (object instanceof BlogPost)
    {
      return ObjectType.BLOG_POST;
    }
    else if (object instanceof Note)
    {
      return ObjectType.NOTE;
    }
    else if (object instanceof Comment)
    {
      return ObjectType.COMMENT;
    }
    else if (object instanceof Message)
    {
      return ObjectType.MESSAGE;
    }
    else if (object instanceof DocumentRecord)
    {
      return ObjectType.DATA;
    }
    else if (object instanceof User)
    {
      String publicId = EJBLocator.lookupOrganisationDirectoryBean().getDefaultOrganisation(ticket).getDefaultUserId();
      if (object.getId().equals(publicId))
      {
        return PrincipalType.PUBLIC;
      }
      else
      {
        return PrincipalType.USER;
      }
    }
    else if (object instanceof Group)
    {
      return PrincipalType.GROUP;
    }
    else if (object instanceof Folder)
    {
      return ObjectType.FOLDER;
    }
    else
    {
      return ObjectType.UNKNOWN;
    }
  }

  public static String getObjectType(Ticket ticket, String id) throws ConnexienceException
  {
    return getObjectType(ticket, EJBLocator.lookupObjectDirectoryBean().getServerObject(ticket, id, ServerObject.class));
  }
}
