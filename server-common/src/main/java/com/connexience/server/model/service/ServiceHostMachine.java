/*
 * ServiceHostMachine.java
 */

package com.connexience.server.model.service;

import java.io.*;

/**
 * This class represents a machine that is running the service host executable
 * @author hugo
 */
public class ServiceHostMachine implements Serializable {
    /** Database ID */
    private long id;

    /** Host UUID */
    private String guid = null;
    
    /** Machine IP address */
    private String ipAddress;

    /** Is the process active */
    private boolean active;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    
}