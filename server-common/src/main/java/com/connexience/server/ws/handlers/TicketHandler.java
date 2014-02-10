/*
 * TicketHandler.java
 */

package com.connexience.server.ws.handlers;
import com.connexience.server.model.*;
import com.connexience.server.model.security.*;

import javax.servlet.http.HttpServletRequest;
import org.apache.axis.*;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.message.*;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.axis.wsdl.Java2WSDL;
import org.w3c.dom.*;

/**
 * This class handles TicketData objects on the server side.
 * @author nhgh
 */
public class TicketHandler extends BasicHandler {
    
    /**
     * Creates a new instance of TicketHandler
     */
    public TicketHandler() {
    }
    
    /** Invoke this handler to process a message */
    public void invoke(MessageContext context) throws AxisFault {
        if(context.isClient()){
            doClient(context);
        } else {
            doServer(context);
        }
    }
    
    /** Do the server side ticket processing. This gets the ticket data from the
     * SOAP message and puts it into the MessageContext as a property so that
     * the services can access it */
    private final void doServer(MessageContext context) throws AxisFault {
        if(!context.getPastPivot()){
            // Only process incoming messages
            Message msg = context.getRequestMessage();
            try {
                NodeList tickets = msg.getSOAPHeader().getElementsByTagNameNS(TicketData.ticketNS, TicketData.elementName);
                if(tickets.getLength()==1){
                    Element ticketElement = (Element)tickets.item(0);
                    TicketData td = new TicketData();
                    td.setFromElement(ticketElement);          
                    Object o = context.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
                    if(o!=null){
                        if(((HttpServletRequest)o).getRemoteHost()!=null){
                            context.setProperty(Ticket.REMOTE_IP_PROPERTY, ((HttpServletRequest)o).getRemoteHost());
                        } else {
                            context.setProperty(Ticket.REMOTE_IP_PROPERTY, "UNKNOWN");
                        }
                    }
                    
                    context.setProperty(TicketData.ticketPropertyName, td);

                } else if(tickets.getLength()>1){
                    System.out.println("Too many tickets. Only one logon ticket can be present");
                    throw new Exception("Too many tickets");
                }                
                
            } catch (Exception e){
                throw new AxisFault("Error processing SOAP header: " + e.getMessage());
            }
            
        }
    }
    
    /** Do the client side ticket processing. This gets the current TicketData object 
     * from the AxisEngine and serialises it to an XML element which gets put into 
     * the message header */
    private final void doClient(MessageContext context) throws AxisFault {
        if(!context.getPastPivot()){
            // Only process requests that are going out
            AxisEngine engine = context.getAxisEngine();
            Object ticketObject = engine.getOption(TicketData.ticketPropertyName);
            if(ticketObject!=null){
                TicketData td = (TicketData)ticketObject;
                Message msg = context.getRequestMessage();
                
                try {
                    SOAPEnvelope envelope = msg.getSOAPEnvelope();
                    envelope.addHeader(td.saveToSOAPHeaderElement());
                    
                } catch (Exception e){
                    throw new AxisFault("Error processing SOAP message: " + e.getMessage());
                }                
            }
        }
    }
}
