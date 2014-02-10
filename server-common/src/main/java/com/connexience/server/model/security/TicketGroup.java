/*
 * TicketGroup.java
 */

package com.connexience.server.model.security;

/**
 * This class represents a group membership within a ticket.
 * @author hugo
 */
public class TicketGroup {
    /** Ticket group id */
    private long id;
    
    /** Ticket id */
    private String ticketId;
    
    /** Group id */
    private String groupId;
    
    /** Creates a new instance of TicketGroup */
    public TicketGroup() {
    }

    /** Get the database id */
    public long getId() {
        return id;
    }

    /** Set the database id */
    public void setId(long id) {
        this.id = id;
    }

    /** Get the id of the related ticket */
    public String getTicketId() {
        return ticketId;
    }

    /** Set the id of the related ticket */
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    /** Get the group id that this membership refers to */
    public String getGroupId() {
        return groupId;
    }

    /** Set the group id that this membership refers to */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
}
