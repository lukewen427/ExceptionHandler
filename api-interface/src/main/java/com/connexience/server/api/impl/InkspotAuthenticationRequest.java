/*
 * InkspotAuthenticationRequest.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;

/**
 * This class provides an implementataion of the IAuthenticationRequest object
 * which is used to log a user onto the server via the API.
 * @author nhgh
 */
public class InkspotAuthenticationRequest extends InkspotObject implements IAuthenticationRequest {

    public InkspotAuthenticationRequest() {
        super();
        putProperty("logon", "");
        putProperty("passwordhash", "");
    }

    public String getHashedPassword() {
        return getPropertyString("passwordhash");
    }

    public String getLogonName() {
        return getPropertyString("logon");
    }

    public void setLogonName(String logonName) {
        putProperty("logon", logonName);
    }

    public void setPassword(String password) {

        putProperty("passwordhash", password);
    }
}
