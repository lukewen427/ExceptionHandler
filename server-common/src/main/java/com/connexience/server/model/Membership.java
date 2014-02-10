/*
 * Membership.java
 */

package com.connexience.server.model;

/**
 * This interface defines a membership object
 * @author hugo
 */
public interface Membership {
    /** Get the object that the principal is a member of */
    public String getMemberContainerId();
    
    /** Get the principal */
    public String getPrincipalId();
}
