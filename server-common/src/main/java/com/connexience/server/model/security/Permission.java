/*
 * Permission.java
 */

package com.connexience.server.model.security;

import java.io.Serializable;

/**
 * This class represents a permission for a server object
 *
 * @author hugo
 */
public class Permission implements Serializable
{
  /**
   * Read permission
   */
  public static final String READ_PERMISSION = "read";

  /**
   * Write permission
   */
  public static final String WRITE_PERMISSION = "write";

  /**
   * Administrator permission
   */
  public static final String ADMINISTRATOR_PERMISSION = "admin";

  /**
   * Owner permission
   */
  public static final String OWNER_PERMISSION = "owner";

  /**
   * Owner permission
   */
  public static final String ADD_PERMISSION = "add";

  /**
   * Owner permission
   */
  public static final String EXECUTE_PERMISSION = "execute";


  /**
   * Database id
   */
  private long id;

  /**
   * ID of the secured object
   */
  private String targetObjectId;

  /**
   * ID of principal
   */
  private String principalId;

  /* Whether or not this permission is universal.  If so, the principalId is not checked */
  private boolean universal;

  /**
   * Permission type
   */
  private String type;

  /**
   * Creates a new instance of Permission
   */
  public Permission()
  {
  }

  /**
   * Get the ID of this permission object
   */
  public long getId()
  {
    return id;
  }

  /**
   * Set the ID of this permission object
   */
  public void setId(long id)
  {
    this.id = id;
  }

  /**
   * Get the id of the secured object
   */
  public String getTargetObjectId()
  {
    return targetObjectId;
  }

  /**
   * Set the id of the secured object
   */
  public void setTargetObjectId(String targetObjectId)
  {
    this.targetObjectId = targetObjectId;
  }

  /**
   * Get the id of the principal that this permission refers to
   */
  public String getPrincipalId()
  {
    return principalId;
  }

  /**
   * Set the id of the principal that this permission relates to
   */
  public void setPrincipalId(String principalId)
  {
    this.principalId = principalId;
  }

  /**
   * Get the permission type. Read / write / owner etc
   */
  public String getType()
  {
    return type;
  }

  /**
   * Get the permission type. Read / write / owner etc
   */
  public void setType(String type)
  {
    this.type = type;
  }

  public boolean isUniversal()
  {
    return universal;
  }

  public void setUniversal(boolean universal)
  {
    this.universal = universal;
  }

  /**
   * Override the equals method
   */
  public boolean equals(Object obj)
  {
    if (obj instanceof Permission)
    {
      Permission p = (Permission) obj;
      if ((p.getPrincipalId().equals(getPrincipalId()) && p.getTargetObjectId().equals(getTargetObjectId()) && p.getType().equals(getType()))
          || (p.isUniversal() && p.getTargetObjectId().equals(getTargetObjectId()) && p.getType().equals(getType())))
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else
    {
      return false;
    }
  }

}