/*
 * ExternalObject.java
 */

package com.connexience.server.model.social.application;

import com.connexience.server.model.folder.*;
import java.io.*;

/**
 * This is an object that an external application can register with the system
 * and then use to manage access control and storage of properties. 
 * @author nhgh
 */
public class ExternalObject extends Folder implements Serializable {
    /** ID of the application that created this object */
    private String applicationId;

    /** Object type string. This is used by the external application to filter
     * returned objects by type */
    private String typeString = "Object";

    public ExternalObject() {
        super();
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }
}