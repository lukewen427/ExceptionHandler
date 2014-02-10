/*
 * IApplicationRegistration.java
 */

package com.connexience.server.api;

/**
 * This interface defines a registration object that is passed to an application
 * when a user registers or unregisters for it
 * @author nhgh
 */
public interface IApplicationRegistration extends IObject {
    /** XML Name for object */
    public static final String XML_NAME = "ApplicationRegistration";

    /** Id of the user registering */
    public String getUserId();

    /** Set the Id of the user registering */
    public void setUserId(String userId);

    /** First name of the user */
    public String getFirstname();

    /** Set the first name of the user */
    public void setFirstname(String firstname);

    /** Surname of the user */
    public String getSurname();

    /** Set the surname of the user */
    public void setSurname(String surname);

    /** Is this a registration request. true = registration, false = unregistration */
    public boolean isRegistrationRequest();

    /** Set whether this is a registration request */
    public void setRegistrationRequest(boolean registrationRequest);
}