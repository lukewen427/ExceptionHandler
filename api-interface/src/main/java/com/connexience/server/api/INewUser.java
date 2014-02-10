/*
 * IUser.java
 */

package com.connexience.server.api;

/**
 * This interface represents a single User within the system
 *
 * @author hugo
 */
public interface INewUser extends ISecuredObject {
    /**
     * XML Name for object
     */
    public static final String XML_NAME = "NewUser";

    /**
     * Set the users first name
     */
    public void setFirstname(String firstname);

    /**
     * Get the users first name
     */
    public String getFirstname();

    /**
     * Set the users surname
     */
    public void setSurname(String surname);

    /**
     * Get the users surname
     */
    public String getSurname();

    public String getPassword();

    public void setPassword(String password);

    public String getEmail();

    public void setEmail(String email);


}
