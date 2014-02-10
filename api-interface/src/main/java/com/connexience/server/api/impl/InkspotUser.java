/*
 * InkspotUser.java
 */

package com.connexience.server.api.impl;
import com.connexience.server.api.*;

/**
 * This class provides the Inkspot client implementation of a user
 * @author hugo
 */
public class InkspotUser extends InkspotSecuredObject implements IUser {
    /** Construct and make sure the correct properties exist */
    public InkspotUser(){
        super();
        putProperty("firstname", "");
        putProperty("surname", "");
        putProperty("homefolderid", "");
        putProperty("workflowfolderid", "");
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return getPropertyString("firstname").toString();
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        putProperty("firstname", firstname);
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return getPropertyString("surname").toString();
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        putProperty("surname", surname);
    }

    public String getHomeFolderId() {
        return getPropertyString("homefolderid");
    }

    public void setHomeFolderId(String homeFolderId) {
        putProperty("homefolderid",homeFolderId);
    }

    public void setWorkflowFolderId(String workflowFolderId) {
        putProperty("workflowfolderid", workflowFolderId);
    }

    public String getWorkflowFolderId(){
        return getPropertyString("workflowfolderid");
    }
}
