/*
 * PromotionalItem.java
 */

package com.connexience.server.model.currency;

import java.security.SecureRandom;
import java.io.*;

/**
 * This class represents a promotional piece of currency that can be added to
 * a users account.
 * @author hugo
 */
public class PromotionalItem implements Serializable {
    /** Database id */
    private long id;

    /** Account balance to add */
    private int quantity = 0;

    /** Creation date */
    private long creationTime;

    /** Expiry date */
    private long expiryTime;

    /** Unique code */
    private String promotionCode;

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /** Generate a random code */
    public static final String generateCode(int blocks, int blockSize){
        
        SecureRandom rnd = new SecureRandom();

        StringBuffer code = new StringBuffer();
        int value;

        for(int i=0;i<blocks;i++){
            if(i!=0){
                code.append("-");
            }
            for(int j=0;j<blockSize;j++){
                value = 65 + rnd.nextInt(26);
                code.append((char)value);
            }

        }

        return code.toString();
    }

    public static void main(String[] args){
        for(int i=0;i<500;i++){
            System.out.println(generateCode(5, 4));
        }
    }
}
