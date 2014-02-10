/*
 * ApplicationDescription.java
 */

package com.connexience.server.model.social.application;

import java.io.*;

/**
 * This class provides a description of an application that is returned
 * when a user searches for an application.
 *
 * @author nhgh
 */
public class ApplicationDescription implements Serializable
{
  /**
   * Application ID
   */
  private String id;

  /**
   * Application name
   */
  private String name;

  /**
   * Application description
   */
  private String description;

  /**
   * Application owner / creator
   */
  private String creatorId;

  /**
   * URL of the application
   */
  private String url;

  /**
   * Communication URL of the application
   */
  private String communicationUrl;

  /**
   * Document editor url
   */
  private String documentEditUrl;

  /**
   * 50x50 pixel Icon URL
   */
  private String iconURL;

  /**
   * MIME types
   */
  private String mimeTypeList;

  /**
    * Should this application be served from inside e-SC in an iFrame
    * */
   private boolean showInIFrame;

  /**
   * URL of the application documentation
   * */
  private String documentationURL;



  public ApplicationDescription()
  {
  }

  public ApplicationDescription(ExternalApplication app)
  {
    this.id = app.getId();
    this.name = app.getName();
    this.description = app.getDescription();
    this.creatorId = app.getCreatorId();
    this.url = app.getApplicationUrl();
    this.communicationUrl = app.getCommunicationUrl();
    this.documentEditUrl = app.getDocumentEditUrl();
    this.mimeTypeList = app.getMimeTypeList();
    this.iconURL = app.getIconURL();
    this.showInIFrame = app.isShowInIFrame();
    this.documentationURL = app.getDocumentationURL();
  }

  /**
   * Application ID
   *
   * @return the id
   */
  public String getId()
  {
    return id;
  }

  /**
   * Application ID
   *
   * @param id the id to set
   */
  public void setId(String id)
  {
    this.id = id;
  }

  /**
   * Application name
   *
   * @return the name
   */
  public String getName()
  {
    return name;
  }

  /**
   * Application name
   *
   * @param name the name to set
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Application description
   *
   * @return the description
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Application description
   *
   * @param description the description to set
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Application owner / creator
   *
   * @return the creator
   */
  public String getCreatorId()
  {
    return creatorId;
  }

  /**
   * Application owner / creator
   *
   * @param creatorId the creator to set
   */
  public void setCreator(String creatorId)
  {
    this.creatorId = creatorId;
  }

  /**
   * @return the url
   */
  public String getUrl()
  {
    return url;
  }

  /**
   * @param url the url to set
   */
  public void setUrl(String url)
  {
    this.url = url;
  }

  /**
   * @return the communicationUrl
   */
  public String getCommunicationUrl()
  {
    return communicationUrl;
  }

  /**
   * @return the documentEditUrl
   */
  public String getDocumentEditUrl()
  {
    return documentEditUrl;
  }

  /**
   * @return the mimeTypeList
   */
  public String getMimeTypeList()
  {
    return mimeTypeList;
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

  public String getDocumentationURL()
  {
    return documentationURL;
  }
}