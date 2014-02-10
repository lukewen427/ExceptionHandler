/*
 * IUser.java
 */

package com.connexience.server.api;

/**
 * This interface represents a single User within the system
 * @author hugo
 */
public interface IUser extends ISecuredObject {
    /** XML Name for object */
    public static final String XML_NAME = "User";

    /** Set the users first name */
    public void setFirstname(String firstname);

    /** Get the users first name */
    public String getFirstname();

    /** Set the users surname */
    public void setSurname(String surname);

    /** Get the users surname */
    public String getSurname();

    /** Get the ID of the users home folder */
    public String getHomeFolderId();

    /** Set the ID of the users home folder */
    public void setHomeFolderId(String homeFolderId);

    /** Set the Worklfow Folder Id for the user */
    public void setWorkflowFolderId(String workflowFolderId);

    /** Get the Worklfow Folder Id for the user */
    public String getWorkflowFolderId();

}
