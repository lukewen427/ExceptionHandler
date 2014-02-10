/*
 * ServerObject.java
 */

package com.connexience.server.model;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * This is the base class for objects stored within the
 * server.
 *
 * @author hugo
 */
public class ServerObject implements Serializable, Comparable<ServerObject>
{
  /**
   * Database ID
   */
  private String id;

  /**
   * Id of the object that contains this one if any
   */
  private String containerId;

  /**
   * Organisation that this object belongs to
   */
  private String organisationId;

  /**
   * Object name
   */
  private String name;

  /**
   * Organisation description
   */
  private String description;

  /**
   * ID of the user that created the object
   */
  private String creatorId;

  /**
   * Date the object was created
   */
  private long timeInMillis;

  private String objectType;

  /**
   * Creates a new instance of ServerObject
   */
  public ServerObject()
  {
    timeInMillis = new Date().getTime();
  }

  /** Populate an object with fields from this one */
  public void populateCopy(ServerObject copy){
      copy.setContainerId(containerId);
      copy.setCreatorId(creatorId);
      copy.setDescription(description);
      copy.setName(name);
      copy.setObjectType(objectType);
      copy.setOrganisationId(organisationId);
  }
  /**
   * toString displays object name
   */
  public String toString()
  {
    return name;
  }

  /**
   * Get the object unique identifier
   */
  public String getId()
  {
    return id;
  }

  /**
   * Set the object unique idendifier
   */
  public void setId(String id)
  {
    this.id = id;
  }

  /**
   * Get the object name
   */
  public String getName()
  {
    return name;
  }

  /**
   * Set the object name
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Get the id of the container object
   */
  public String getContainerId()
  {
    return containerId;
  }

  /**
   * Set the id of the container object
   */
  public void setContainerId(String containerId)
  {
    this.containerId = containerId;
  }

  /**
   * Get the id of the organisation containing this object
   */
  public String getOrganisationId()
  {
    return organisationId;
  }

  /**
   * Set the id of the organisation containing this object
   */
  public void setOrganisationId(String organisationId)
  {
    this.organisationId = organisationId;
  }

  /**
   * Set the description of this object
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Get the description of this object
   */
  public String getDescription()
  {
    return description;
  }


  /**
   * Get the ID of the user that created this object
   */
  public String getCreatorId()
  {
    return creatorId;
  }

  /**
   * Set the ID of the user that created this object
   */
  public void setCreatorId(String creatorId)
  {
    this.creatorId = creatorId;
  }

  public long getTimeInMillis()
  {
    return timeInMillis;
  }

  public void setTimeInMillis(long timeInMillis)
  {
    this.timeInMillis = timeInMillis;
  }

  public java.util.Date getCreationDate()
  {
    return new java.util.Date(getTimeInMillis());
  }

  public void setCreationTime(java.util.Date dateCreated)
  {
    setTimeInMillis(dateCreated.getTime());
  }

  public int compareTo(ServerObject o)
  {
    return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
  }

  public String getObjectType()
  {
    return objectType;
  }

  public void setObjectType(String objectType)
  {
    this.objectType = objectType;
  }

  /**
   * Override the equals method to check Id
   */
  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof ServerObject)) return false;

    ServerObject that = (ServerObject) o;

    if (!id.equals(that.id)) return false;

    return true;
  }

  @Override
  public int hashCode()
  {
    return id.hashCode();
  }

  public String getDisplayName()
  {
    String n = StringEscapeUtils.escapeHtml(this.getName());
    return n;
  }
}