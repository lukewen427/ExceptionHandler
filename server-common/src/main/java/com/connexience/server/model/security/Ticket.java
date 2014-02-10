package com.connexience.server.model.security;

import com.connexience.server.util.*;

import java.io.*;
import java.util.*;

public class Ticket implements Serializable {
    static final long serialVersionUID = 6826210287308161730L;
    
    /** ID that represents the organisation for a root ticket */
    public static final String ROOT_ORGANISATION_ID = "0000000000000000000";
    
    /** Property of the remote IP address in the Axis Engine */
    public static final String REMOTE_IP_PROPERTY = "RemoteIP";
    
    /** Should this ticket be stored. This is only set when the ticket is created, and is set to
     * false the first time the ticket has been stored */
    private boolean storable = false;
    
    /** ID of the logon ticket */
    private String id;
    
    /** User ID that this ticket refers to */
    private String userId;

    /** Organisation id */
    private String organisationId;
    
    /** Last access time */
    private Date lastAccessTime;
    
    /** Is this a super user ticket for this organisation */
    private boolean superTicket = false;
    
    /** IP of the host that sent this ticket. This is not set in the database and is recorded
     * in the ticket handler when the ticket is received */
    private String remoteHost;
    
    /** Empty constructor */
    public Ticket(){
    }
        
    /** Is this ticket associated with an organisation that isn't the root org */
    public boolean isAssociatedWithNonRootOrg(){
        if(organisationId.equals(ROOT_ORGANISATION_ID)){
            return false;
        } else {
            return true;
        }
    }
    
    /** Return the ID of this ticket */
    public String getId(){
        return id;
    }
    
    /** Set the ID of this ticket */
    public void setId(String id){
        this.id = id;
    }    
    
    /** Get the user id this ticket refers to */
    public String getUserId(){
        return userId;
    }
        
    /** Set the user id this ticket refers to */
    public void setUserId(String userId){
        this.userId = userId;
    }
    
    /** Get the last access time */
    public Date getLastAccessTime(){
        return lastAccessTime;
    }
    
    /** Set the last access time */
    public void setLastAccessTime(Date lastAccessTime){
        this.lastAccessTime = lastAccessTime;
    }
    
    /** Update the access time */
    public void updateAccessTime(){
        lastAccessTime = new Date();
    }

    /** Get the id of the organisation hosting the logon */
    public String getOrganisationId() {
        return organisationId;
    }

    /** Set the id of the organisation hosting the logon */
    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }

    /** Is this a superuser ticket for the organisation */
    public boolean isSuperTicket() {
        return superTicket;
    }

    /** Set whether this is a superuser ticket for the organisation */
    public void setSuperTicket(boolean superTicket) {
        this.superTicket = superTicket;
    }

    /** Get the IP address of the remote host */
    public String getRemoteHost() {
        return remoteHost;
    }

    /** Set the IP address of the remote host */
    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }
    
    /** Set whether to store this ticket */
    public void setStorable(boolean storable){
        this.storable = storable;
    }
    
    /** Get whether to store this ticket */
    public boolean isStorable() {
        return storable;
    }
}