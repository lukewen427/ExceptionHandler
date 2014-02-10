/*
 * BooleanProduct.java
 */

package com.connexience.server.model.allowance.products;

import com.connexience.server.model.allowance.*;
import com.connexience.server.model.allowance.*;
import com.connexience.server.model.allowance.subscriptions.BooleanSubscription;
/**
 * This product contains a simple flag that indicates whether or not a product
 * has been subscribed to.
 * @author hugo
 */
public class BooleanProduct extends Product {
    /** Flag value */
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public Subscription createSubscription() {
        BooleanSubscription subscription = new BooleanSubscription();
        super.configureSubscription(subscription);
        return subscription;
    }
}
