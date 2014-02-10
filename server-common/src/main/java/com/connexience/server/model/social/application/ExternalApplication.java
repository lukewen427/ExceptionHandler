/*
 * ExternalApplication.java
 */

package com.connexience.server.model.social.application;

import com.connexience.server.model.*;
import com.connexience.server.util.*;

import java.io.Serializable;

/**
 * This class represents an external application that is recognised by the
 * social networking code. It contains an API key that is a long randomly
 * generated string that is used to sign messages.
 *
 * @author nhgh
 */
public class ExternalApplication extends ServerObject implements Serializable
{
  /**
   * API SECRET Key
   */
  private String secretKey;

  /**
   * Base URL of the server that provides this application
   */
  private String applicationUrl;

  /**
   * URL that is used to retrieve the summary data from the application and
   * process registration events
   */
  private String communicationUrl;

  /**
   * URL that is used for document editing
   */
  private String documentEditUrl;

  /**
   * URL of the 50x50 icon for the application
   */
  private String iconURL;

  /**
   * List of MIME types that this application can edit
   */
  private String mimeTypeList;

  /**
   * Should this application be served from inside e-SC in an iFrame
   * */
  private boolean showInIFrame = true;

  /**
   * URL of the documentation of the application
   * */
  private String documentationURL;

  /**
   * Is this application allowed to register users?
   */
  private Boolean isAllowedToRegisterUsers = false;


  /**
   * Get the secred API Key
   */
  public String getSecretKey()
  {
    return secretKey;
  }

  /**
   * Set the secret API Key
   */
  public void setSecretKey(String secretKey)
  {
    this.secretKey = secretKey;
  }

  /**
   * Get the URL used when the application is used to edit a document
   */
  public String getDocumentEditUrl()
  {
    return documentEditUrl;
  }

  /**
   * Set the URL used when the application is used to edit a document
   */
  public void setDocumentEditUrl(String documentEditUrl)
  {
    this.documentEditUrl = documentEditUrl;
  }

  /**
   * Get a list of mime types that this application can edit
   */
  public String getMimeTypeList()
  {
    return mimeTypeList;
  }

  /**
   * Set the list of mime types that this application can edit
   */
  public void setMimeTypeList(String mimeTypeList)
  {
    this.mimeTypeList = mimeTypeList;
  }


  /**
   * Get the base application URL
   */
  public String getApplicationUrl()
  {
    return applicationUrl;
  }

  /**
   * Set the base application URL
   */
  public void setApplicationUrl(String applicationUrl)
  {
    this.applicationUrl = applicationUrl;
  }

  /**
   * Get the URL that is used to communicate with the application
   */
  public String getCommunicationUrl()
  {
    return communicationUrl;
  }

  /**
   * Set the URL that is used to communicate with the application
   */
  public void setCommunicationUrl(String communicationUrl)
  {
    this.communicationUrl = communicationUrl;
  }

  /**
   * Generate a new API secret key using the Secure Random GUID utility class
   */
  public void generateNewSecretKey()
  {
    secretKey = new RandomGUID().valueAfterMD5;
  }

  /**
   * Get a description of this application
   */
  public ApplicationDescription getApplicationDescription()
  {
    return new ApplicationDescription(this);
  }

  public String getIconURL()
  {
    return iconURL;
  }

  public void setIconURL(String iconURL)
  {
    this.iconURL = iconURL;
  }

  public boolean isShowInIFrame()
  {
    return showInIFrame;
  }

  public void setShowInIFrame(boolean showInIFrame)
  {
    this.showInIFrame = showInIFrame;
  }

  public String getDocumentationURL()
  {
    return documentationURL;
  }

  public void setDocumentationURL(String documentationURL)
  {
    this.documentationURL = documentationURL;
  }

  public Boolean getIsAllowedToRegisterUsers()
  {
    return isAllowedToRegisterUsers;
  }

  public void setIsAllowedToRegisterUsers(Boolean allowedToRegisterUsers)
  {
    isAllowedToRegisterUsers = allowedToRegisterUsers;
  }
}