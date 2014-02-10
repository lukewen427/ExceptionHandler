/*
 * IAuthenticationRequest.java
 */

package com.connexience.server.api;

/**
 * This interface defines a request for authentication for a user using
 * a logon name and hashed password.
 * @author nhgh
 */
public interface IAuthenticationRequest extends IObject {
    /** Name to use in the XML representation */
    public static final String XML_NAME = "AuthenticationRequest";
    
    /** Get the logon name */
    public String getLogonName();

    /** Set the logon name */
    public void setLogonName(String logonName);

    /** Get the hashed password */
    public String getHashedPassword();

    /** Set the hashed password */
    public void setPassword(String password);
}