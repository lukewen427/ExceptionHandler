/*
 * IntegerSubscription.java
 */

package com.connexience.server.model.allowance.subscriptions;
import com.connexience.server.model.allowance.*;
/**
 * This class represents a subscription to an integer "quantity" object. This
 * is typically something like a storage quota where a user buys xGB of storage
 * for a period of time.
 * @author hugo
 */
public class IntegerSubscription extends Subscription {
    /** Subscription level / quota */
    private int value;

    /** Product units */
    private String units = "";
    
    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}