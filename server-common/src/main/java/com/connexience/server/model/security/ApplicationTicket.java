/*
 * ApplicationTicket.java
 */

package com.connexience.server.model.security;

/**
 * This class provides an implementation of a Ticket that is used by the
 * external application API.
 * @author nhgh
 */
public class ApplicationTicket extends Ticket {
    static final long serialVersionUID = -7567532782449003495L;

    public ApplicationTicket() {
        super();
        setStorable(true);
    }

    /** Get the application ID. This is actually the same as the UserID */
    public String getApplicationId(){
        return getUserId();
    }

}