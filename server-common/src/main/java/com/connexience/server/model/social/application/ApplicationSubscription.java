/*
 * ApplicationSubscription.java
 */

package com.connexience.server.model.social.application;

import java.io.*;

/**
 * This class provides a subscription reference for an application. It contains
 * the application ID, name and a set of permissions that control the operations a
 * given user allows an application to perfrom.
 * @author nhgh
 */
public class ApplicationSubscription implements Serializable {
    /** See user details */
    public static final int VIEW_USER_DETAILS = 0;
    
    /** File viewing flag */
    public static final int VIEW_FILES = 1;

    /** Connection viewing flag */
    public static final int VIEW_CONNECTIONS = 2;

    /** News posting flag */
    public static final int POST_NEWS = 3;

    /** Workflow calling flag */
    public static final int EXECUTE_WORKFLOWS = 4;

    /** Write to user files */
    public static final int UPLOAD_FILES = 5;

    /** Modify user files */
    public static final int MODIFY_FILES = 6;
    

    /** Object id */
    private long id;

    /** User ID */
    private String userId;

    /** Application ID */
    private String applicationId;

    /** Can the application view files */
    private boolean allowedToViewFiles = false;

    /** Can the application see friends */
    private boolean allowedToViewConnections = false;

    /** Can the application post news items */
    private boolean allowedToPostNews = false;

    /** Can the application call user workflows */
    private boolean allowedToExecuteWorkflows = false;

    /** Can the application see the user details */
    private boolean allowedToViewDetails = true;

    /** Can the application modify user files */
    private boolean allowedToModifyFiles = false;

    /** Can the application upload files */
    private boolean allowedToUploadFiles = false;
    
    /**
     * Object id
     * @return the id
     */
    public long getId() {
        return id;
    }

    public void setAllowedToModifyFiles(boolean allowedToModifyFiles) {
        this.allowedToModifyFiles = allowedToModifyFiles;
    }

    public void setAllowedToUploadFiles(boolean allowedToUploadFiles) {
        this.allowedToUploadFiles = allowedToUploadFiles;
    }

    public boolean isAllowedToModifyFiles() {
        return allowedToModifyFiles;
    }

    public boolean isAllowedToUploadFiles() {
        return allowedToUploadFiles;
    }

    /**
     * Object id
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * User ID
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * User ID
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Application ID
     * @return the applicationId
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * Application ID
     * @param applicationId the applicationId to set
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * Can the application view files
     * @return the allowedToViewFiles
     */
    public boolean isAllowedToViewFiles() {
        return allowedToViewFiles;
    }

    /**
     * Can the application view files
     * @param allowedToViewFiles the allowedToViewFiles to set
     */
    public void setAllowedToViewFiles(boolean allowedToViewFiles) {
        this.allowedToViewFiles = allowedToViewFiles;
    }

    /**
     * Can the application see friends
     * @return the allowedToViewConnections
     */
    public boolean isAllowedToViewConnections() {
        return allowedToViewConnections;
    }

    /**
     * Can the application see friends
     * @param allowedToViewConnections the allowedToViewConnections to set
     */
    public void setAllowedToViewConnections(boolean allowedToViewConnections) {
        this.allowedToViewConnections = allowedToViewConnections;
    }

    /**
     * Can the application post news items
     * @return the allowedToPostNews
     */
    public boolean isAllowedToPostNews() {
        return allowedToPostNews;
    }

    /**
     * Can the application post news items
     * @param allowedToPostNews the allowedToPostNews to set
     */
    public void setAllowedToPostNews(boolean allowedToPostNews) {
        this.allowedToPostNews = allowedToPostNews;
    }

    /**
     * Can the application call user workflows
     * @return the allowedToExecuteWorkflows
     */
    public boolean isAllowedToExecuteWorkflows() {
        return allowedToExecuteWorkflows;
    }

    /**
     * Can the application call user workflows
     * @param allowedToExecuteWorkflows the allowedToExecuteWorkflows to set
     */
    public void setAllowedToExecuteWorkflows(boolean allowedToExecuteWorkflows) {
        this.allowedToExecuteWorkflows = allowedToExecuteWorkflows;
    }

    /**
     * @return the allowedToViewDetails
     */
    public boolean isAllowedToViewDetails() {
        return allowedToViewDetails;
    }

    /**
     * @param allowedToViewDetails the allowedToViewDetails to set
     */
    public void setAllowedToViewDetails(boolean allowedToViewDetails) {
        this.allowedToViewDetails = allowedToViewDetails;
    }

    /**
     * Copy the permissions from this subscription into another one
     */
    public void copyPermissionsIntoSubscription(ApplicationSubscription sub){
        sub.setAllowedToExecuteWorkflows(allowedToExecuteWorkflows);
        sub.setAllowedToModifyFiles(allowedToModifyFiles);
        sub.setAllowedToPostNews(allowedToPostNews);
        sub.setAllowedToUploadFiles(allowedToUploadFiles);
        sub.setAllowedToViewConnections(allowedToViewConnections);
        sub.setAllowedToViewDetails(allowedToViewDetails);
        sub.setAllowedToViewFiles(allowedToViewFiles);
    }
}