/*
 * Subscription.java
 */

package com.connexience.server.model.allowance;
import java.io.*;

/**
 * This is the base class for a subscription to a product. Different subscription
 * classes exist for each product type - Integer/Boolean/Counter products. The
 * subscription is created with an initial quota or flag which can be changed
 * as the product is used.
 * @author hugo
 */
public class Subscription implements Serializable {
    /** Database id */
    private long id;

    /** Owner of this subscription */
    private String userId;

    /** Start time of this subscription */
    private long startTime;

    /** Expiry time of this subscription */
    private long expiry;

    /** Subscription name */
    private String name;

    /** ID of the product subscribed to */
    private String productId;

    /** Image to display in web page */
    private String imageUrl;

    /** Does the subscription auto renew */
    private boolean autoRenewing;

    /** Category label for this subscription */
    private String categoryLabel;

    public long getExpiry() {
        return expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

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

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }
}