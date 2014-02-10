/*
 * InkspotErrorMessage.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;

/**
 * This class provides an implementation of an error message
 * @author nhgh
 */
public class InkspotErrorMessage extends InkspotObject implements IAPIErrorMessage {

    public InkspotErrorMessage() {
        super();
        getProperties().put("ErrorMessage", "");
    }

    public void setErrorMessage(String errorMessage) {
        getProperties().put("ErrorMessage", errorMessage);
    }

    public String getErrorMessage() {
        return getProperties().get("ErrorMessage").toString();
    }
}