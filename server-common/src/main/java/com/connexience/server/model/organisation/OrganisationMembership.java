/*
 * OrganisationMembership.java
 */

package com.connexience.server.model.organisation;
import com.connexience.server.model.*;

import java.io.*;
/**
 * This class represents a membership of an organisation
 * @author hugo
 */
public class OrganisationMembership implements Membership, Serializable {
    /** Object is a user */
    public static final int USER_MEMBERSHIP = 0;
    
    /** Object is a group */
    public static final int GROUP_MEMBERSHIP = 1;
    
    /** Membership id */
    private long id;
    
    /** Organisation id */
    private String organisationId;
    
    /** Id of principal belonging to organisation */
    private String principalId;
    
    /** Object type */
    private int objectType;
    
    /** Creates a new instance of OrganisationMembership */
    public OrganisationMembership() {
    }

    /** Get the membership id */
    public long getId() {
        return id;
    }
    
    /** Set the membership id */
    public void setId(long id) {
        this.id = id;
    }

    /** Get the organisation id */
    public String getOrganisationId() {
        return organisationId;
    }

    /** Get the organisation id */
    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }

    /** Get the id of the user */
    public String getPrincipalId() {
        return principalId;
    }

    /** Set the id of the user */
    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    /** Get the object that the principal is a member of */
    public String getMemberContainerId() {
        return organisationId;
    }

    /** Get the type of object this membership applies to */
    public int getObjectType() {
        return objectType;
    }

    /** Set the type of object this membership applies to */
    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }
}