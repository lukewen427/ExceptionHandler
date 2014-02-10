/*
 * InkspotUser.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.INewUser;

/**
 * This class provides the Inkspot client implementation of a user to be added to the system
 *
 * @author hugo
 */
public class InkspotNewUser extends InkspotSecuredObject implements INewUser {
    /**
     * Construct and make sure the correct properties exist
     */
    public InkspotNewUser() {
        super();
        putProperty("firstname", "");
        putProperty("surname", "");
        putProperty("password", "");
        putProperty("email", "");
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

    @Override
    public String getPassword() {
        return getPropertyString("password");
    }

    @Override
    public void setPassword(String password) {
        putProperty("password", password);
    }

    @Override
    public String getEmail() {
        return getPropertyString("email");
    }

    @Override
    public void setEmail(String email) {
        putProperty("email", email);
    }

}
