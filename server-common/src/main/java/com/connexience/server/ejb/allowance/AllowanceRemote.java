/*
 * AllowanceRemote.java
 */

package com.connexience.server.ejb.allowance;
import com.connexience.server.model.security.*;
import com.connexience.server.*;

import javax.ejb.Remote;

/**
 * This product manages allowances within the system.
 * @author hugo
 */
@Remote
public interface AllowanceRemote {
    /** Is a user subscribed to a product */
    public boolean isUserSubscribedToProduct(Ticket ticket, String userId, String productId) throws ConnexienceException;

    /** Is the user subscribed to a product */
    public boolean isUserSubscribedToProduct(Ticket ticket, String userId, String categoryLabel, String productName) throws ConnexienceException;

    /** Get the total value of subscriptions in a category */
    public int getCategoryTotalForUser(Ticket ticket, String userId, String categoryLabel) throws ConnexienceException;

    /** Get the total counter value for a product */
    public int getCounterValueForUser(Ticket ticket, String userId, String productId) throws ConnexienceException;

    /** Decrement a counter value for a product */
    public void decrementCounterValueForUser(Ticket ticket, String userId, String productId, int decrementQuantity) throws ConnexienceException;
    
    
    // =========================================================================
    // Storage quota helper methods
    // =========================================================================
    /** Get the total used storage quota */
    public long getTotalStorageQuota(Ticket ticket, String userId) throws ConnexienceException;
    
    /** Get the available storage quota */
    public long getAvailableStorageQuota(Ticket ticket, String userId) throws ConnexienceException;
    
    /** Get the total ammount of storage used */
    public long getStorageQuotaUsed(Ticket ticket, String userId) throws ConnexienceException;

}