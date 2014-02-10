/*
 * InkspotApplicationRegistration.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;

/**
 * This class provides an implementation of a registration data object.
 * @author nhgh
 */
public class InkspotApplicationRegistration extends InkspotObject implements IApplicationRegistration {

    public InkspotApplicationRegistration() {
        super();
        putProperty("userid", "");
        putProperty("firstname", "");
        putProperty("surname", "");
        putProperty("registrationrequest", "false");
    }

    public String getFirstname() {
        return getPropertyString("firstname");
    }

    public String getSurname() {
        return getPropertyString("surname");
    }

    public String getUserId() {
        return getPropertyString("userid");
    }

    public boolean isRegistrationRequest() {
        if(getPropertyString("registrationrequest").equalsIgnoreCase("true")){
            return true;
        } else {
            return false;
        }
    }

    public void setFirstname(String firstname) {
        putProperty("firstname", firstname);
    }

    public void setRegistrationRequest(boolean registrationRequest) {
        if(registrationRequest==true){
            putProperty("registrationrequest", "true");
        } else {
            putProperty("registrationrequest", "false");
        }
    }

    public void setSurname(String surname) {
        putProperty("surname", surname);
    }

    public void setUserId(String userId) {
        putProperty("userid", userId);
    }
}
