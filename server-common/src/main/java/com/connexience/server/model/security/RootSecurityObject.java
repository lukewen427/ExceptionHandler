/*
 * RootSecurityObject.java
 */

package com.connexience.server.model.security;
import com.connexience.server.model.*;

/**
 * This is the root object securing the system. It holds a username and password
 * for a user that can administer everything and also keys etc to sign tickets
 * for this user when they log on
 * @author hugo
 */
public class RootSecurityObject extends ServerObject {
    /** ID of the root user */
    private String rootUserId;
    
    /** Creates a new instance of RootSecurityObject */
    public RootSecurityObject() {
        super();
    }

    /** Get the root user id */
    public String getRootUserId() {
        return rootUserId;
    }

    /** Set the root username */
    public void setRootUserId(String rootUserId) {
        this.rootUserId = rootUserId;
    }
}