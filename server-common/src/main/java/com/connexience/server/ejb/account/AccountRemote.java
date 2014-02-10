/*
 * AccountRemote.java
 */

package com.connexience.server.ejb.account;
import com.connexience.server.model.currency.*;
import com.connexience.server.model.security.*;
import com.connexience.server.*;

import javax.ejb.Remote;
import java.util.*;
import java.math.BigDecimal;

/**
 * This interface defines the functionality of the accounting bean
 * @author hugo
 */
@Remote
public interface AccountRemote {
    /** Get the account for a user */
    public Account getUserAccount(Ticket ticket, String userId) throws ConnexienceException;

    /** Redeem a promotional item */
    public boolean redeemPromotionalItem(Ticket ticket, String itemCode) throws ConnexienceException;

    /** Add a purchase to the users account */
    public void rechargeWithCash(Ticket ticket, String userId, int balanceToAdd, Currency currency, BigDecimal cost, String transactionId, String processorName) throws ConnexienceException;

    /** List purchases */
    public List listActivity(Ticket ticket, String userId, String type, Date startDate, Date endDate) throws ConnexienceException;

    /** Save promotional item */
    public PromotionalItem savePromotionalItem(Ticket ticket, PromotionalItem item) throws ConnexienceException;

    /** Remove a promotional item */
    public void removePromotionalItem(Ticket ticket, long id) throws ConnexienceException;

    /** Create a new promotional item and code */
    public PromotionalItem createPromotionalItem(Ticket ticket, String label, int tokenCount, int validityDays) throws ConnexienceException;

    /** List all of the promotional items */
    public List listPromotionalItems(Ticket ticket, int offset, int size) throws ConnexienceException;

    /** Redeem a promotional item */
    public boolean validatePromotionalItem(String itemCode) throws ConnexienceException;

}
