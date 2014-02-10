/*
 * WebTicket.java
 */
package com.connexience.server.model.security;

import java.io.Serializable;

/**
 * This class provides a ticket that is created when using the web pages.
 *
 * @author nhgh
 */
public class WebTicket extends Ticket implements Serializable {
    static final long serialVersionUID = -5702427820076897159L;

    /**
     * List of Group IDs for the user
     */
    private String[] groupIds;

    /**
     * Creates a new instance of WebTicket
     */
    public WebTicket() {
        super();
        setStorable(true);
    }

    /**
     * Get the group ids
     */
    public String[] getGroupIds() {
        return groupIds;
    }

    /**
     * Set the group ids
     */
    public void setGroupIds(String[] groupIds) {
        this.groupIds = groupIds;
    }
}
