/*
 * Product.java
 */

package com.connexience.server.model.allowance;

import java.io.*;
import java.util.*;

/**
 * This is the base class for a product that can be purchased or subscribed to.
 * System products are created for various parts of the system i.e. storage, workflow
 * queue etc. Also products can be created for external applications and resources.
 *
 * A product contains information on the initial allowances (i.e. a product can
 * be for XX Gb of storage space), when a subscription for a product is created,
 * this initial information is copied into the subscription for a given user.
 * @author hugo
 */
public class Product implements Serializable {
    // Protected system product category labels */

    /** Storage quota product */
    public static final String STORAGE_QUOTA = "Storage";

    /** Workflow queue product */
    public static final String WORKFLOW_QUEUE = "Workflow";

    /** System wide products */
    private static final Vector<String> systemProducts = new Vector<String>();
    static {
        systemProducts.add(STORAGE_QUOTA);
        systemProducts.add(WORKFLOW_QUEUE);
    }
    
    /** Database ID of this product */
    private String id;

    /** Product category string */
    private String categoryLabel;
    
    /** Product name */
    private String name;
    
    /** Product description */
    private String description;
    
    /** Product owner ID */
    private String ownerId;

    /** Does the product expire */
    private boolean expirable = true;

    /** Expiry duration in days */
    private int duration = 1;

    /** Does a subscription automatically renew */
    private boolean autoRenewing = false;

    /** Product cost in internal currency units */
    private int productCost = 1;

    /** URL for the product icon in the product list */
    private String imageUrl = "../images/app.png";

    /** Can this product be purchased using promotional spots */
    private boolean purchasableUsingPromotionalTokens = true;

    /** Can this product be purchased multiple times at once */
    private boolean singleSubscriptionOnly = false;

    /** ID / Name of the object that this product refers to */
    private String objectIdOrName;
    
    /** Is this a default product that gets added to all new users */
    private boolean universalProduct = false;
    
    public boolean isAutoRenewing() {
        return autoRenewing;
    }

    public void setAutoRenewing(boolean autoRenewing) {
        this.autoRenewing = autoRenewing;
    }

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(String categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isExpirable() {
        return expirable;
    }

    public void setExpirable(boolean expirable) {
        this.expirable = expirable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getProductCost() {
        return productCost;
    }

    public void setProductCost(int productCost) {
        this.productCost = productCost;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPurchasableUsingPromotionalTokens(boolean purchasableUsingPromotionalTokens) {
        this.purchasableUsingPromotionalTokens = purchasableUsingPromotionalTokens;
    }

    public boolean isPurchasableUsingPromotionalTokens() {
        return purchasableUsingPromotionalTokens;
    }

    public void setSingleSubscriptionOnly(boolean singleSubscriptionOnly) {
        this.singleSubscriptionOnly = singleSubscriptionOnly;
    }

    public boolean isSingleSubscriptionOnly() {
        return singleSubscriptionOnly;
    }

    /** Is this product a system protected product */
    public final boolean isSystemProduct(){
        return Product.isSystemProduct(categoryLabel);
    }

    public String getObjectIdOrName() {
        return objectIdOrName;
    }

    public void setObjectIdOrName(String objectIdOrName) {
        this.objectIdOrName = objectIdOrName;
    }

    /** Is a product category a system product */
    public static final boolean isSystemProduct(String categoryLabel){
        if(systemProducts.contains(categoryLabel)){
            return true;
        } else {
            return false;
        }
    }

    /** Create a new subscription object */
    public Subscription createSubscription(){
        return null;
    }

    public void setUniversalProduct(boolean universalProduct) {
        this.universalProduct = universalProduct;
    }

    public boolean isUniversalProduct() {
        return universalProduct;
    }

    
    /** Configure a subscription object with product data */
    protected void configureSubscription(Subscription subscription){
        Calendar expiry = Calendar.getInstance();
        
        // Set the start time as midnight tonight
        expiry.setTime(new Date());
        expiry.set(Calendar.HOUR_OF_DAY, 0);
        expiry.set(Calendar.MINUTE, 0);
        expiry.set(Calendar.SECOND, 0);
        expiry.set(Calendar.MILLISECOND, 0);
        subscription.setStartTime(expiry.getTimeInMillis());
        
        // Set the end time as n days from now
        expiry.add(Calendar.DAY_OF_YEAR, duration);
        subscription.setExpiry(expiry.getTimeInMillis());
        subscription.setName(this.getName());
        subscription.setProductId(this.id);
        subscription.setImageUrl(this.imageUrl);
        subscription.setAutoRenewing(this.autoRenewing);
        subscription.setCategoryLabel(this.categoryLabel);
    }
}
