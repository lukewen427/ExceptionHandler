/*
 * GroupMembership.java
 */

package com.connexience.server.model.security;
import com.connexience.server.model.*;
import java.io.*;

/**
 * This object represents a Users membership within a group
 * @author hugo
 */
public class GroupMembership implements Membership, Serializable {
    /** Membership id */
    private long id;
    
    /** Group id */
    private String groupId;
    
    /** User id */
    private String userId;
    
    /** Creates a new instance of GroupMembership */
    public GroupMembership() {
    }

    /** Get this object id */
    public long getId() {
        return id;
    }

    /** Set this object id */
    public void setId(long id) {
        this.id = id;
    }

    /** Get the id of the group that this membership refers to */
    public String getGroupId() {
        return groupId;
    }

    /** Set the id of the group that this membership refers to */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /** Get the id of the user that this membership refers to */
    public String getUserId() {
        return userId;
    }

    /** Set the id of the user that this membership refers to */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /** Get the object that the principal is a member of */
    public String getMemberContainerId() {
        return groupId;
    }

    /**
     * Get the principal
     */
    public String getPrincipalId() {
        return userId;
    }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof GroupMembership)) return false;

    GroupMembership that = (GroupMembership) o;

    if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null) return false;
    if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = groupId != null ? groupId.hashCode() : 0;
    result = 31 * result + (userId != null ? userId.hashCode() : 0);
    return result;
  }
}