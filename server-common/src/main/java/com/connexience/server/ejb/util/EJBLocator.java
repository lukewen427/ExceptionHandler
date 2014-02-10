/*
 * EJBLocator.java
 */

package com.connexience.server.ejb.util;

import com.connexience.server.*;
import com.connexience.server.ejb.directory.*;
import com.connexience.server.ejb.provenance.ProvenanceRemote;
import com.connexience.server.ejb.ticket.*;
import com.connexience.server.ejb.certificate.*;
import com.connexience.server.ejb.acl.*;
import com.connexience.server.ejb.storage.*;
import com.connexience.server.ejb.remove.*;
import com.connexience.server.ejb.logging.*;
import com.connexience.server.ejb.properties.*;
import com.connexience.server.ejb.allowance.*;
import com.connexience.server.ejb.notifications.NotificationsRemote;
import com.connexience.server.ejb.smtp.SMTPRemote;
import com.connexience.server.ejb.account.*;
import com.connexience.server.ejb.admin.AdminLoggingRemote;
import com.connexience.server.ejb.admin.AdminRemote;
import com.connexience.server.ejb.service.*;
import com.connexience.server.ejb.social.*;

import javax.naming.*;

/**
 * This class looks up EJB remote interfaces for the beans contained in this
 * project.
 *
 * @author hugo
 */
public abstract class EJBLocator
{
  private static AccessControlRemote acRemote = null;
  private static StorageRemote storageRemote = null;
  private static AdminLoggingRemote adminLoggingRemote = null;
  private static AdminRemote adminRemote = null;
  private static AccountRemote accountRemote = null;
  private static UserDirectoryRemote userDirectoryRemote = null;
  private static OrganisationDirectoryRemote organisationRemote = null;
  private static TicketRemote ticketRemote = null;
  private static CertificateAccessRemote certificateRemote = null;
  private static GroupDirectoryRemote groupDirectoryRemote = null;
  private static ObjectInfoRemote objectInfoRemote = null;
  private static ObjectRemovalRemote objectRemovalRemote = null;
  private static PropertiesRemote propertiesRemote = null;
  private static ObjectDirectoryRemote objectDirectoryRemote = null;
  private static MetaDataRemote metadataRemote = null;
  private static SearchRemote searchRemote = null;
  private static NotificationsRemote notificationsRemote = null;
  private static SMTPRemote smtpRemote = null;
  private static LogEventRemote logEventRemote = null;
  private static ProductsRemote productsRemote = null;
  private static ServiceRemote serviceRemote = null;
  private static RequestRemote requestRemote = null;
  private static NewsRemote newsRemote = null;
  private static LinkRemote linkRemote = null;
  private static BlogRemote blogRemote = null;
  private static NoteRemote noteRemote = null;
  private static ApplicationRemote appRemote = null;
  private static TagRemote tagRemote = null;
  private static MessageRemote messageRemote = null;
  private static EventRemote eventRemote = null;
  private static CommentRemote commentRemote = null;
  private static AllowanceRemote allowanceRemote = null;
  private static ProvenanceRemote provRemote = null;


  /**
   * Get hold of the document storage bean
   */
  public static StorageRemote lookupStorageBean() throws ConnexienceException
  {
    try
    {
      if (storageRemote == null)
      {
        Context c = new InitialContext();
        storageRemote = (StorageRemote) c.lookup("java:global/ejb/StorageBean");
      }
      return storageRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate storage bean: " + ne.getMessage());
    }
  }

  public static AdminRemote lookupAdminBean() throws ConnexienceException
  {
    try
    {
      if (adminRemote == null)
      {
        Context c = new InitialContext();
        adminRemote = (AdminRemote) c.lookup("java:global/ejb/AdminBean");
      }
      return adminRemote;
    }
    catch (NamingException ne)
    {
      ne.printStackTrace();
      throw new ConnexienceException("Cannot locate admin bean: " + ne.getMessage());
    }
  }

  public static AdminLoggingRemote lookupAdminLoggingBean() throws ConnexienceException
  {
    try
    {
      if (adminLoggingRemote == null)
      {
        Context c = new InitialContext();
        adminLoggingRemote = (AdminLoggingRemote) c.lookup("java:global/ejb/AdminLoggingBean");
      }
      return adminLoggingRemote;
    }
    catch (NamingException ne)
    {
      ne.printStackTrace();
      throw new ConnexienceException("Cannot locate admin logging bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of the account bean
   */
  public static AccountRemote lookupAccountBean() throws ConnexienceException
  {
    try
    {
      if (accountRemote == null)
      {
        Context c = new InitialContext();
        accountRemote = (AccountRemote) c.lookup("java:global/ejb/AccountBean");
      }
      return accountRemote;
    }
    catch (NamingException ne)
    {
      ne.printStackTrace();
      throw new ConnexienceException("Cannot locate account bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of the access control bean
   */
  public static AccessControlRemote lookupAccessControlBean() throws ConnexienceException
  {
    try
    {
      if (acRemote == null)
      {
        Context c = new InitialContext();
        acRemote = (AccessControlRemote) c.lookup("java:global/ejb/AccessControlBean");
      }
      return acRemote;
    }
    catch (NamingException ne)
    {
      ne.printStackTrace();
      throw new ConnexienceException("Cannot locate access control bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of the access control bean
   */
  public static UserDirectoryRemote lookupUserDirectoryBean() throws ConnexienceException
  {
    try
    {
      if (userDirectoryRemote == null)
      {
        Context c = new InitialContext();
        userDirectoryRemote = (UserDirectoryRemote) c.lookup("java:global/ejb/UserDirectoryBean");

      }
      return userDirectoryRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate user directory: " + ne.getMessage());
    }
  }

  /**
   * Get hold of the organisation directory bean
   */
  public static OrganisationDirectoryRemote lookupOrganisationDirectoryBean() throws ConnexienceException
  {
    try
    {
      if (organisationRemote == null)
      {
        Context c = new InitialContext();
        organisationRemote = (OrganisationDirectoryRemote) c.lookup("java:global/ejb/OrganisationDirectoryBean");
      }
      return organisationRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate user directory: " + ne.getMessage());
    }
  }

  /**
   * Get hold of ticket bean
   */
  public static TicketRemote lookupTicketBean() throws ConnexienceException
  {
    try
    {
      if (ticketRemote == null)
      {
        Context c = new InitialContext();
        ticketRemote = (TicketRemote) c.lookup("java:global/ejb/TicketBean");
      }
      return ticketRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate ticket issuer: " + ne.getMessage());
    }
  }

  /**
   * Get hold of certificate bean
   */
  public static CertificateAccessRemote lookupCertificateBean() throws ConnexienceException
  {
    try
    {
      if (certificateRemote == null)
      {
        Context c = new InitialContext();
        certificateRemote = (CertificateAccessRemote) c.lookup("java:global/ejb/CertificateAccessBean");
      }
      return certificateRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate certificate access: " + ne.getMessage());
    }
  }

  /**
   * Get hold of group directory bean
   */
  public static GroupDirectoryRemote lookupGroupDirectoryBean() throws ConnexienceException
  {
    try
    {
      if (groupDirectoryRemote == null)
      {
        Context c = new InitialContext();
        groupDirectoryRemote = (GroupDirectoryRemote) c.lookup("java:global/ejb/GroupDirectoryBean");
      }
      return groupDirectoryRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate group directory: " + ne.getMessage());
    }
  }

  /**
   * Get hold of object information bean
   */
  public static ObjectInfoRemote lookupObjectInfoBean() throws ConnexienceException
  {
    try
    {
      if (objectInfoRemote == null)
      {
        Context c = new InitialContext();
        objectInfoRemote = (ObjectInfoRemote) c.lookup("java:global/ejb/ObjectInfoBean");
      }
      return objectInfoRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate object info bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of an object removal bean
   */
  public static ObjectRemovalRemote lookupObjectRemovalBean() throws ConnexienceException
  {
    try
    {
      if (objectRemovalRemote == null)
      {
        Context c = new InitialContext();
        objectRemovalRemote = (ObjectRemovalRemote) c.lookup("java:global/ejb/ObjectRemovalBean");
      }
      return objectRemovalRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate object removal bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of the properties bean
   */
  public static PropertiesRemote lookupPropertiesBean() throws ConnexienceException
  {
    try
    {
      if (propertiesRemote == null)
      {
        Context c = new InitialContext();
        propertiesRemote = (PropertiesRemote) c.lookup("java:global/ejb/PropertiesBean");
      }
      return propertiesRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate cluster bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of the properties bean
   */
  public static ObjectDirectoryRemote lookupObjectDirectoryBean() throws ConnexienceException
  {
    try
    {
      if (objectDirectoryRemote == null)
      {
        Context c = new InitialContext();
        objectDirectoryRemote = (ObjectDirectoryRemote) c.lookup("java:global/ejb/ObjectDirectoryBean");
      }
      return objectDirectoryRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate object directory bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of the metadata bean
   */
  public static MetaDataRemote lookupMetaDataBean() throws ConnexienceException
  {
    try
    {
      if (metadataRemote == null)
      {
        Context c = new InitialContext();
        metadataRemote = (MetaDataRemote) c.lookup("java:global/ejb/MetaDataBean");
      }
      return metadataRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate meta data bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of the search bean
   */
  public static SearchRemote lookupSearchBean() throws ConnexienceException
  {
    try
    {
      if (searchRemote == null)
      {
        Context c = new InitialContext();
        searchRemote = (SearchRemote) c.lookup("java:global/ejb/SearchBean");
      }
      return searchRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate search bean: " + ne.getMessage());
    }
  }

  public static NotificationsRemote lookupNotificationsBean() throws ConnexienceException
  {
    try
    {
      if (notificationsRemote == null)
      {
        Context c = new InitialContext();
        notificationsRemote = (NotificationsRemote) c.lookup("java:global/ejb/NotificationsBean");
      }
      return notificationsRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate notifications bean: " + ne.getMessage());
    }
  }

  public static SMTPRemote lookupSMTPBean() throws ConnexienceException
  {
    try
    {
      if (smtpRemote == null)
      {
        Context c = new InitialContext();
        smtpRemote = (SMTPRemote) c.lookup("java:global/ejb/SMTPBean");
      }
      return smtpRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate notifications bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of a logevent bean
   */
  public static LogEventRemote lookupLogEventBean() throws ConnexienceException
  {
    try
    {
      if (logEventRemote == null)
      {
        Context c = new InitialContext();
        logEventRemote = (LogEventRemote) c.lookup("java:global/ejb/LogEventBean");
      }
      return logEventRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate log event bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of an allowance bean
   */
  public static AllowanceRemote lookupAllowanceBean() throws ConnexienceException
  {
    try
    {
      if (allowanceRemote == null)
      {
        Context c = new InitialContext();
        allowanceRemote = (AllowanceRemote) c.lookup("java:global/ejb/AllowanceBean");
      }
      return allowanceRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate allowance bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of a product bean
   */
  public static ProductsRemote lookupProductsBean() throws ConnexienceException
  {
    try
    {
      if (productsRemote == null)
      {
        Context c = new InitialContext();
        productsRemote = (ProductsRemote) c.lookup("java:global/ejb/ProductsBean");
      }
      return productsRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate products bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of a service bean
   */
  public static ServiceRemote lookupServiceBean() throws ConnexienceException
  {
    try
    {
      if (serviceRemote == null)
      {
        Context c = new InitialContext();
        serviceRemote = (ServiceRemote) c.lookup("java:global/ejb/ServiceBean");
      }
      return serviceRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate service bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of the social networking request bean
   */
  public static RequestRemote lookupRequestBean() throws ConnexienceException
  {
    try
    {
      if (requestRemote == null)
      {
        Context c = new InitialContext();
        requestRemote = (RequestRemote) c.lookup("java:global/ejb/RequestBean");
      }
      return requestRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate social network request bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of the social networking news bean
   */
  public static NewsRemote lookupNewsBean() throws ConnexienceException
  {
    try
    {
      if (newsRemote == null)
      {
        Context c = new InitialContext();
        newsRemote = (NewsRemote) c.lookup("java:global/ejb/NewsBean");
      }
      return newsRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate social network news bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of the link management bean
   */
  public static LinkRemote lookupLinkBean() throws ConnexienceException
  {
    try
    {
      if (linkRemote == null)
      {
        Context c = new InitialContext();
        linkRemote = (LinkRemote) c.lookup("java:global/ejb/LinkBean");
      }
      return linkRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate social network link bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of the blog bean
   */
  public static BlogRemote lookupBlogBean() throws ConnexienceException
  {
    try
    {
      if (blogRemote == null)
      {
        Context c = new InitialContext();
        blogRemote = (BlogRemote) c.lookup("java:global/ejb/BlogBean");
      }
      return blogRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate social network blog bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of the note bean
   */
  public static NoteRemote lookupNoteBean() throws ConnexienceException
  {
    try
    {
      if (noteRemote == null)
      {
        Context c = new InitialContext();
        noteRemote = (NoteRemote) c.lookup("java:global/ejb/NoteBean");
      }
      return noteRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate social network note bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of an Application bean
   */
  public static ApplicationRemote lookupApplicationBean() throws ConnexienceException
  {
    try
    {
      if (appRemote == null)
      {
        Context c = new InitialContext();
        appRemote = (ApplicationRemote) c.lookup("java:global/ejb/ApplicationBean");
      }
      return appRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate publication bean: ", ne);
    }
  }

  /**
   * Get hold of an Application bean
   */
  public static TagRemote lookupTagBean() throws ConnexienceException
  {
    try
    {
      if (tagRemote == null)
      {
        Context c = new InitialContext();
        tagRemote = (TagRemote) c.lookup("java:global/ejb/TagBean");
      }
      return tagRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate com.connexience.server.social.tag bean: ", ne);
    }
  }

  /**
   * Get hold of an Application bean
   */
  public static MessageRemote lookupMessageBean() throws ConnexienceException
  {
    try
    {
      if (messageRemote == null)
      {
        Context c = new InitialContext();
        messageRemote = (MessageRemote) c.lookup("java:global/ejb/MessageBean");
      }
      return messageRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate message bean: ", ne);
    }
  }

  /**
   * Get hold of an Application bean
   */
  public static EventRemote lookupEventBean() throws ConnexienceException
  {
    try
    {
      if (eventRemote == null)
      {
        Context c = new InitialContext();
        eventRemote = (EventRemote) c.lookup("java:global/ejb/EventBean");
      }
      return eventRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate event bean: ", ne);
    }
  }

  /**
   * Get hold of an Application bean
   */
  public static CommentRemote lookupCommentBean() throws ConnexienceException
  {
    try
    {
      if (commentRemote == null)
      {
        Context c = new InitialContext();
        commentRemote = (CommentRemote) c.lookup("java:global/ejb/CommentBean");
      }
      return commentRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate comment bean: ", ne);
    }
  }

  /**
   * Get hold of a Provenance bean
   */
  public static ProvenanceRemote lookupProvenanceBean() throws ConnexienceException
  {
    try
    {
      if (provRemote == null)
      {
        Context c = new InitialContext();
        provRemote = (ProvenanceRemote) c.lookup("java:global/ejb/ProvenanceBean");
      }
      return provRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate Provenance bean: ", ne);
    }
  }
}