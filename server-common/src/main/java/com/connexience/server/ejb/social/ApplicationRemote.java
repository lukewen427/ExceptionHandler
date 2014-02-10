/*
 * ApplicationRemote.java
 */

package com.connexience.server.ejb.social;

import javax.ejb.Remote;

import com.connexience.server.*;
import com.connexience.server.model.security.*;
import com.connexience.server.model.social.application.*;

import java.util.*;

/**
 * This interface defines the behaviour of the exernal application management
 * bean.
 * @author nhgh
 */
@Remote
public interface ApplicationRemote {
    /** List the external applications */
    public List listApplications(Ticket ticket) throws ConnexienceException;

    /** List application descriptions. These are what is used in the GUI because
     * they don't contain key details */
    public List listApplicationDescriptions(Ticket ticket) throws ConnexienceException;

    /** Search application descriptions */
    public List searchApplicationDescriptions(Ticket ticket, String searchText) throws ConnexienceException;
    
    /** List all of the external applications owned by a specific user */
    public List listUserApplications(Ticket ticket, String userId) throws ConnexienceException;

    /** Save an external application */
    public ExternalApplication saveApplication(Ticket ticket, ExternalApplication application) throws ConnexienceException;

    /** Get an external application by ID */
    public ExternalApplication getApplication(Ticket ticket, String applicationId) throws ConnexienceException;

    /** Remove an external application */
    public void removeApplication(Ticket ticket, String applicationId) throws ConnexienceException;

    /** Check a signature against an applications private key */
    public boolean checkSignature(String applicationId, String data, String signature) throws ConnexienceException;

    /** Sign a string using an application private key */
    public String signString(Ticket ticket, String applicationId, String data) throws ConnexienceException;
    
    /** Get the description of an application */
    public ApplicationDescription getApplicationDescription(Ticket ticket, String applicationId) throws ConnexienceException;

    /** Subscribe to an application */
    public ApplicationSubscription subscribeToApplication(Ticket ticket, String applicationId) throws ConnexienceException;

    /** Get subscription details for an application */
    public ApplicationSubscription getSubscriptionDetails(Ticket ticket, String applicationId) throws ConnexienceException;

    /** Get the subscription details for a given user and application. This method is used by the
     * application servlets when they need to check permissions */
    public ApplicationSubscription getSubscriptionDetails(Ticket ticket, String applicationId, String userId) throws ConnexienceException;

    /** Get subscription details by subscription ID */
    public ApplicationSubscription getSubscriptionDetails(Ticket ticket, long subscriptionId) throws ConnexienceException;
    
    /** Is a user subscribed to a specific application */
    public boolean isSubscribedToApplication(Ticket ticket, String applicationId) throws ConnexienceException;

    /** Update subscription details for an application */
    public ApplicationSubscription saveSubscriptionDetails(Ticket ticket, ApplicationSubscription subscription) throws ConnexienceException;
    
    /** Unsubscribe from an application */
    public void unsbscribeFromApplication(Ticket ticket, String applicationId) throws ConnexienceException;


  /** List applications that are subscribed to by a specific user */
  List listApplicationsUserSubscribedTo(Ticket ticket, String userId) throws ConnexienceException;

  /**
   * List applications that are owned by a specific user or the user subscribes to
   *
   */
  List listUserApplicationsOwnedOrSubscribedTo(Ticket ticket, String userId, int start, int maxResults) throws ConnexienceException;

  /**
   * List applications that are owned by a specific user or the user subscribes to
   */
  Long numberOfApplicationsOwnedOrSubscribedTo(Ticket ticket, String userId) throws ConnexienceException;

  /**
   * Save / update an external application with a subscription to itself.  The subscription details the preferred access rights for the users
   */
  ExternalApplication saveApplication(Ticket ticket, ExternalApplication application, ApplicationSubscription subscription) throws ConnexienceException;

  /** Get all of the external objects owned by an application in a users folder */
  public List getExternalObjectsForApplication(Ticket ticket, String applicationId, String userId) throws ConnexienceException;

  /** Get an external object by ID */
  public ExternalObject getExternalObject(Ticket ticket, String applicationId, String objectId) throws ConnexienceException;
  
  /** Get an external object by ID */
  public ExternalObject getExternalObject(Ticket ticket, String objectId) throws ConnexienceException;

  /** Delete an external object */
  public void deleteExternalObject(Ticket ticket, String objectId) throws ConnexienceException;

  /** Save an external object */
  public ExternalObject saveExternalObject(Ticket ticket, ExternalObject obj) throws ConnexienceException;

  /** List the applications that can edit a mime type */
  public List listApplicationsSupportingMimeType(Ticket ticket, String mimeType) throws ConnexienceException;

  /** Can an application edit a mime type */
  public boolean applicationSupportsMimeType(Ticket ticket, String applicationId, String mimeType) throws ConnexienceException;

  List listApplicationSubscriptions(Ticket ticket, int start, int maxResults) throws ConnexienceException;

  List listApplicationDescriptions(Ticket ticket, int start, int maxResults) throws ConnexienceException;

  long numberApplicationDescriptions(Ticket ticket) throws ConnexienceException;
}