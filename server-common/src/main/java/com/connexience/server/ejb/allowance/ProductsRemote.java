/*
 * ProductsRemote.java
 */

package com.connexience.server.ejb.allowance;

import com.connexience.server.model.allowance.*;
import com.connexience.server.ConnexienceException;
import com.connexience.server.model.security.*;

import java.util.*;
import javax.ejb.Remote;

/**
 * This class defines the interface of the product management bean
 * @author hugo
 */
@Remote
public interface ProductsRemote {
    /** List all of the available products */
    public List listProducts(Ticket ticket) throws ConnexienceException;

    /** List all of the products that a user owns */
    public List listProducts(Ticket ticket, String userId) throws ConnexienceException;

    /** Save a product to the database */
    public Product saveProduct(Ticket ticket, Product product) throws ConnexienceException;

    /** Get a product by ID */
    public Product getProduct(Ticket ticket, String productId) throws ConnexienceException;

    /** Delete a product */
    public void deleteProduct(Ticket ticket, String productId) throws ConnexienceException;

    /** Is a user subscribed to a product */
    public boolean isSubscribedToProduct(Ticket ticket, String userId, String productId) throws ConnexienceException;

    /** Subscribe to a product */
    public void subscribeToProduct(Ticket ticket, String userId, String productId) throws ConnexienceException;

    /** List a users subscriptions */
    public List listSubscriptions(Ticket ticket, String userId) throws ConnexienceException;

    /** Terminate a renewing subscription */
    public void terminateSubscription(Ticket ticket, long subscriptionId) throws ConnexienceException;

    /** Remove a subscription */
    public void removeSubscription(Ticket ticket, long subscriptionId) throws ConnexienceException;

    /** Remove expired subscriptions */
    public void removeExpiredSubscriptions(Ticket ticket, String userId) throws ConnexienceException;
    
    /** Get a subscription by ID */
    public Subscription getSubscripion(Ticket ticket, long subscriptionId) throws ConnexienceException;

    List<Product> listUniversalProducts(Ticket ticket) throws ConnexienceException;

    void subscribeToAllUniversalProducts(Ticket ticket, String userId) throws ConnexienceException;

    List listAllProducts(Ticket ticket) throws ConnexienceException;
}