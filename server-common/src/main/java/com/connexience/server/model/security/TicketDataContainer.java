/*
 * TicketDataContainer.java
 */

package com.connexience.server.model.security;
import com.connexience.server.model.organisation.*;

/**
 * This class gets returned whenever a client acquires a ticket. It contains
 * the actual ticket data plus information regarding the organisation, the 
 * type of ticket and the organisation X509 certificate so that it can be
 * added to the trusted store if needed.
 * @author nhgh
 */
public class TicketDataContainer implements java.io.Serializable {
    /** Ticket Data object */
    private TicketData ticketData;
    
    /** Organisation */
    private String organisationId;
    
    /** Is the ticket a super ticket */
    private boolean superTicket = false;
    
    /** Organisation X509 certificate data */
    private byte[] certificateData;
    
    /** Logged on user */
    private User user;
    
    /** Creates a new instance of TicketDataContainer */
    public TicketDataContainer() {
    }

    /** Get the ticket data object that is used in the SOAP headers */
    public TicketData getTicketData() {
        return ticketData;
    }

    /** Set the ticket data object that is used in the SOAP headers */
    public void setTicketData(TicketData ticketData) {
        this.ticketData = ticketData;
    }

    /** Get the organisation that this ticket has logged on to */
    public String getOrganisationId() {
        return organisationId;
    }

    /** Set the organisation that this ticket has logged on to */
    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }

    /** Is this a super user ticket. This is used to determine if certain UI
     * functions are displayed. It has no influence on the activities that are
     * actually allowed because these are determined at the server */
    public boolean isSuperTicket() {
        return superTicket;
    }

    /** Set whether the ticket referred to by this object is a super ticket */
    public void setSuperTicket(boolean superTicket) {
        this.superTicket = superTicket;
    }

    /** Get the serialized X509 certificate */
    public byte[] getCertificateData() {
        return certificateData;
    }

    /** Set the serialized X509 certificate */
    public void setCertificateData(byte[] certificateData) {
        this.certificateData = certificateData;
    }

    /** Get the logged on user */
    public User getUser() {
        return user;
    }

    /** Set the logged on user */
    public void setUser(User user) {
        this.user = user;
    }
    
}
