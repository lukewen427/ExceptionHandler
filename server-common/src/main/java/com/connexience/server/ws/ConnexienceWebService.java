/*
 * ConnexienceWebService.java
 */

package com.connexience.server.ws;
import com.connexience.server.*;
import com.connexience.server.model.security.*;
import com.connexience.server.ejb.ticket.*;
import com.connexience.server.ejb.util.*;

import org.apache.axis.MessageContext;

/**
 * This is the base web service class. It contains utilities for storing
 * and verifying tickets and for calling the relevant access control
 * beans when object changes are made.
 * @author hugo
 */
public class ConnexienceWebService {   
    /** Creates a new instance of ConnexienceWebService */
    public ConnexienceWebService() {
    }
    
    /** Validate the ticket data from the web service header and send the physical ticket to
     * the EJB */
    public Ticket processTicket() throws ConnexienceException {
        TicketData td = validateTicket();
        TicketRemote bean = EJBLocator.lookupTicketBean();
        Ticket ticket = bean.getTicket(td.getTicketId());     
        ticket.setRemoteHost(MessageContext.getCurrentContext().getProperty(Ticket.REMOTE_IP_PROPERTY).toString());
        ticket.setStorable(true);
        return ticket;
    }
    
    /** Get the TicketData object passed in the SOAP header */
    public TicketData getTicketData() throws ConnexienceException {
        TicketData td = validateTicket();
        return td;
    }
    
    /** Get the current ticket from the ticket store */
    public Ticket getCurrentTicket() {
        return TicketThreadStore.getTicket();
    }
    
    /** Check to see if there is a valid ticket */
    public TicketData validateTicket() throws ConnexienceException {
        Object tdo = MessageContext.getCurrentContext().getProperty(TicketData.ticketPropertyName);
        
        if(tdo!=null){
            TicketData td = (TicketData)tdo;
            TicketRemote bean = EJBLocator.lookupTicketBean();
            if(bean!=null){
                if(!bean.isTicketValid(td)){
                    throw new ConnexienceException("Invalid ticket");
                } else {
                    return td;
                }
                
            } else {
                throw new ConnexienceException("Cannot connect to ticket validator");
            }
        } else {
            throw new ConnexienceException("No ticket present");
        }
    }

}