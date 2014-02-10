/*
 * IntegerProduct.java
 */

package com.connexience.server.model.allowance.products;

import com.connexience.server.model.allowance.*;
import com.connexience.server.model.allowance.subscriptions.*;

/**
 * This product represents an integer value. It can be used to contain things
 * like storage allowances. Integer products with the same category label can
 * be summed to give a total integer value - e.g. Storage allowances can be added
 * together to give a total size.
 * @author hugo
 */
public class IntegerProduct extends Product {
    /** Initial integer value */
    int value = 10;

    /** Product units */
    private String units = "";

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Override
    public Subscription createSubscription() {
        IntegerSubscription subscription = new IntegerSubscription();
        subscription.setValue(value);
        subscription.setUnits(units);
        configureSubscription(subscription);
        return subscription;
    }
}