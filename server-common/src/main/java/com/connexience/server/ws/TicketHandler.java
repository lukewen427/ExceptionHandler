/*
 * TicketHandler.java
 *
 * Created on 05 November 2006, 18:03
 */

package com.connexience.server.ws;

import com.connexience.server.ws.*;
import com.connexience.server.model.security.*;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.*;

/**
 * This handler extracts ticket data from the WebService request and puts it into
 * a ThreadLocal variable contained in the EJB base class.
 * @author hugo
 */
public class TicketHandler implements SOAPHandler<SOAPMessageContext> {
    
    public boolean handleMessage(SOAPMessageContext messageContext) {
        SOAPMessage msg = messageContext.getMessage();
        try {
            if(((Boolean)messageContext.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue()==true){
                // Response
                return processResponse(msg);
            } else {
                return processCall(msg);
            }
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    /** Process a call */
    private boolean processCall(SOAPMessage msg){
        try {
            SOAPHeader header = msg.getSOAPHeader();
            NodeList tickets = header.getElementsByTagNameNS(TicketData.ticketNS, TicketData.elementName);
            if(tickets.getLength()==1){
                Element ticketElement = (Element)tickets.item(0);
                TicketData td = new TicketData();
                td.setFromElement(ticketElement);
                //ConnexienceWebService.TICKET_DATA.remove();
                //ConnexienceWebService.TICKET_DATA.set(td);
                
            } else if(tickets.getLength()>1){
                System.out.println("Too many tickets. Only one logon ticket can be present");
                return false;
                
            } else {
                return false;
            }
            
            return true;
        } catch (Exception e){
            System.out.println("Error in ticket handler: " + e.getMessage());
            return false;
        }
    }
    
    /** Process a response */
    private boolean processResponse(SOAPMessage msg){
        return true;
    }
    
    public Set<QName> getHeaders() {
        return Collections.EMPTY_SET;
    }
    
    public boolean handleFault(SOAPMessageContext messageContext) {
        return true;
    }
    
    public void close(MessageContext context) {
    }
    
}
