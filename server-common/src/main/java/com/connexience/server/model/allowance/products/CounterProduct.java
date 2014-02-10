/*
 * CounterProduct.java
 */

package com.connexience.server.model.allowance.products;

import com.connexience.server.model.allowance.*;
import com.connexience.server.model.allowance.subscriptions.CounterSubscription;

/**
 * This product represents a counter that is decremented each time the product is
 * used. This can be used to represent, for example, the purchase of a fixed
 * number of calls to a service.
 * @author hugo
 */
public class CounterProduct extends Product {
    /** Initial counter value */
    private int initialValue = 10;

    /** Product units */
    private String units = "";
    
    public int getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(int initialValue) {
        this.initialValue = initialValue;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Override
    public Subscription createSubscription() {
        CounterSubscription subscription = new CounterSubscription();
        subscription.setValue(initialValue);
        subscription.setUnits(units);
        configureSubscription(subscription);
        return subscription;
    }
}