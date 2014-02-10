/*
 * Account.java
 */

package com.connexience.server.model.currency;
import com.connexience.server.model.*;
import com.connexience.server.model.allowance.*;

/**
 * This class represent the users tokens account. It contains balances for
 * two separate types of purchase - paid for and complimentary tokens.
 * @author hugo
 */
public class Account extends ServerObject {
    /** Promotional balance */
    private int promotionalBalance = 0;

    /** Purchased balance */
    private int purchasedBalance = 0;

    /** Currency label */
    private String currencyLabel = "Spots";

    public int getPromotionalBalance() {
        return promotionalBalance;
    }

    public void setPromotionalBalance(int promotionalBalance) {
        this.promotionalBalance = promotionalBalance;
    }

    public int getPurchasedBalance() {
        return purchasedBalance;
    }

    public void setPurchasedBalance(int purchasedBalance) {
        this.purchasedBalance = purchasedBalance;
    }

    public String getCurrencyLabel() {
        return currencyLabel;
    }

    public void setCurrencyLabel(String currencyLabel) {
        this.currencyLabel = currencyLabel;
    }

    public void increasePromotionalBalance(int quantity){
        promotionalBalance = promotionalBalance + quantity;
    }

    /** Can this account purchase a product */
    public boolean canPurchase(Product p){
        if(p.isPurchasableUsingPromotionalTokens()){
            if(p.getProductCost()<=promotionalBalance){
                // Just debit promotional
                return true;

            } else if(p.getProductCost()<=(promotionalBalance + purchasedBalance)){
                return true;

            } else {
                return false;

            }
        } else {
            if(p.getProductCost()<purchasedBalance){
                return true;
            } else {
                return false;
            }
        }
    }
    
    /** Make a purchase using available spots. usePromotional flag
     * specifies whether a preference is given to spending promotional
     * spots */
    public boolean debitPurchase(int quantity, boolean favourPromotional){
        if(favourPromotional){
            if(quantity<=promotionalBalance){
                // Just debit promotional
                promotionalBalance = promotionalBalance - quantity;
                return true;

            } else if(quantity<=(promotionalBalance + purchasedBalance)){
                int purchasedBalanceToSpend = quantity - promotionalBalance;
                promotionalBalance = 0;
                purchasedBalance = purchasedBalance - purchasedBalanceToSpend;
                return true;

            } else {
                return false;

            }
        } else {
            if(quantity<=purchasedBalance){
                purchasedBalance = purchasedBalance - quantity;
                return true;
            } else {
                return false;
            }
        }
    }
    
}