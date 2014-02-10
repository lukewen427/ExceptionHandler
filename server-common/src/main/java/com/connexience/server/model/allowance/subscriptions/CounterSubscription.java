/*
 * CounterSubscription.java
 */

package com.connexience.server.model.allowance.subscriptions;
import com.connexience.server.model.allowance.*;
/**
 * This class represents a counter that can be decremented each time it is used
 * @author hugo
 */
public class CounterSubscription extends Subscription {
    /** Current counter value */
    private int value = 0;

    /** Product units */
    private String units = "";

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void decrementValue(){
        if(value>0){
            value--;
        }
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}