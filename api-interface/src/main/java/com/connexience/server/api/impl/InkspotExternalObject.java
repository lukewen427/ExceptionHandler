/*
 * InkspotExternalObject.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;

/**
 * This class provides an implementation of an external object that is used
 * so that external applications can delegate access control decisions to the
 * server.
 * @author nhgh
 */
public class InkspotExternalObject extends InkspotFolder implements IExternalObject {

    public InkspotExternalObject() {
        super();
        putProperty("applicationid", "");
        putProperty("typestring", "");
    }

    public String getApplicationId() {
        return getPropertyString("applicationid");
    }

    public void setApplicationId(String applicationId) {
        putProperty("applicationid", applicationId);
    }

    public String getTypeString() {
        return getPropertyString("typestring");
    }

    public void setTypeString(String typeString) {
        putProperty("typestring", typeString);
    }
}