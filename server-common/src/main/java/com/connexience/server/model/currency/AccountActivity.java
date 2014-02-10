/*
 * Purchase.java
 */

package com.connexience.server.model.currency;

import java.io.*;
import java.util.*;
import java.math.BigDecimal;

/**
 * This class represents a piece of activity in the system that forms part of a
 * users account. Items are created in the database when they are purchased
 * in order to provide a record of the purchase. The actual ammount is transferred
 * to the account object.
 * @author hugo
 */
public class AccountActivity implements Serializable {
    /** Purchase activity */
    public static final String RECHARGE_WITH_CASH_ACTIVITY = "CashRecharge";
    
    /** Expenditure activity */
    public static final String EXPENDITURE_ACTIVITY = "Expenditure";

    /** Promotional item redemption */
    public static final String PROMOTIONAL_ITEM_REDEMPTION = "PromotionalItem";

    /** Database id */
    private long id;

    /** Account ID */
    private String accountId;

    /** Purchase date */
    private long purchaseTime;

    /** Currency used */
    private Currency currency;

    /** Actual cost */
    private BigDecimal cost;

    /** Activity type */
    private String type = RECHARGE_WITH_CASH_ACTIVITY;

    /** Transaction ID */
    private String transactionId;

    /** Payment processor */
    private String processorName;

    /** Quantity of tokens processed */
    private int quantity;

    /** Promotional spot balance before activity */
    private int promotionalBalanceBefore = 0;
    
    /** Promotional spot balance after activity */
    private int promotionalBalanceAfter = 0;
    
    /** Purchased spot balance before activity */
    private int purchasedBalanceBefore = 0;
    
    /** Purchased spot balance after activity */
    private int purchasedBalanceAfter = 0;
    
    public long getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(long purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getProcessorName() {
        return processorName;
    }

    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPromotionalBalanceAfter() {
        return promotionalBalanceAfter;
    }

    public int getPurchasedBalanceAfter() {
        return purchasedBalanceAfter;
    }

    public int getPromotionalBalanceBefore() {
        return promotionalBalanceBefore;
    }

    public int getPurchasedBalanceBefore() {
        return purchasedBalanceBefore;
    }

    public void setPromotionalBalanceAfter(int promotionalBalanceAfter) {
        this.promotionalBalanceAfter = promotionalBalanceAfter;
    }

    public void setPromotionalBalanceBefore(int promotionalBalanceBefore) {
        this.promotionalBalanceBefore = promotionalBalanceBefore;
    }

    public void setPurchasedBalanceAfter(int purchasedBalanceAfter) {
        this.purchasedBalanceAfter = purchasedBalanceAfter;
    }

    public void setPurchasedBalanceBefore(int purchasedBalanceBefore) {
        this.purchasedBalanceBefore = purchasedBalanceBefore;
    }
}