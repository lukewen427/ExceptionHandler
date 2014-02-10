/*
 * RemoteTicketData.java
 */

package com.connexience.server.model.security;

import com.connexience.server.util.*;

import java.io.*;
import java.util.*;
import java.security.*;
import java.security.cert.*;

import javax.xml.transform.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.apache.axis.message.*;

/**
 * This class represents a ticket that is exchanged between partner organisations
 * It contains details of the calling organisation, the target organisation and
 * a list of the groups that the call is currently asserting membership of. It
 * is constructed using an existing ticket. It is signed using the private key of 
 * the partnership originating the call.
 * @author nhgh
 */
public class RemoteTicketData implements Serializable {
    /** Element name in XML */
    public static final String elementName = "RemoteTicket";
    
    /** Ticket namespace */
    public static final String ticketNS = "http://connexience.com/remoteticket";
    
    
    /** Originating (Local) organisation Id */
    private String originatingOrganisationId;
    
    /** Target (Remote) organisation Id */
    private String targetOrganisationId;
    
    /** ID of the originating ticket */
    private String originatingTicketId;
    
    /** Signature of original ticket */
    private byte[] ticketSignatureData;
    
    /** Signature data */
    private byte[] signatureData;
    
    /** Creates a new instance of RemoteTicketData */
    public RemoteTicketData() {
    }
    
    /** Creates a new instance of RemoteTicketData with an existing Ticket */
    public RemoteTicketData(TicketData td) {
        this.originatingOrganisationId = td.getOrganisationId();
        this.originatingTicketId = td.getTicketId();
        this.ticketSignatureData = td.getSignatureData();
    }
    
    /** Set the target organisation */
    public void setTargetOrganisationId(String targetOrganisationId){
        this.targetOrganisationId = targetOrganisationId;
    }
    
    /** Get the originating (source) organisation id */
    public String getTargetOrganisationId(){
        return targetOrganisationId;
    }
    
    /** Reconstruct the TicketData object that created this RemoteTicket */
    public TicketData getTicketData(){
        TicketData td = new TicketData();
        td.setOrganisationId(originatingOrganisationId);
        td.setTicketId(originatingTicketId);
        td.setSignatureData(ticketSignatureData);
        return td;
    }
    
    /** Sign this remote ticket using a PrivateKey */
    public void sign(PrivateKey key){
        try {
            Signature sig = Signature.getInstance(KeyData.sigAlg);
            sig.initSign(key);
            sig.update(originatingOrganisationId.getBytes());
            sig.update(targetOrganisationId.getBytes());
            sig.update(originatingTicketId.getBytes());
            sig.update(ticketSignatureData);
            signatureData = sig.sign();
        } catch (Exception e){
            signatureData = new byte[0];
            System.out.println("Error signing remote ticket: " + e.getMessage());
        }
    }
    
    /** Verify this remote ticket using a certificate */
    public boolean validate(X509Certificate cert){
        try {
            Signature sig = Signature.getInstance(KeyData.sigAlg);
            sig.initVerify(cert);
            sig.update(originatingOrganisationId.getBytes());
            sig.update(targetOrganisationId.getBytes());
            sig.update(originatingTicketId.getBytes());
            sig.update(ticketSignatureData);
            return sig.verify(signatureData);
            
        } catch (Exception e){
            System.out.println("RemoteTicket validation error: " + e.getMessage());
            return false;
        }
    }
    /** Set the properties in this remote ticket from an XML element */
    public void setFromElement(Element e){
        if(e.getNodeName().endsWith(elementName) || e.getLocalName().equalsIgnoreCase(elementName)){
            originatingOrganisationId = e.getAttribute("OriginatingOrgID");
            targetOrganisationId = e.getAttribute("TargetOrgID");
            originatingTicketId = e.getAttribute("TicketID");
            
            String ticketSigEncoded = e.getAttribute("TicketSignature");
            if(ticketSigEncoded!=null){
                ticketSignatureData = Base64.decode(ticketSigEncoded);
            } else {
                ticketSignatureData = new byte[0];
            }
            
            String signatureEncoded = e.getAttribute("Signature");
            if(signatureEncoded!=null){
                signatureData = Base64.decode(signatureEncoded);
            } else {
                signatureData = new byte[0];
            }
        }
    }
    
    /** Save to a SOAPHeaderElement */
    public SOAPHeaderElement saveToSOAPHeaderElement() {
        SOAPHeaderElement element = new SOAPHeaderElement(ticketNS, elementName);
        element.setAttribute("OriginatingOrgID", originatingOrganisationId);
        element.setAttribute("TargetOrgID", targetOrganisationId);
        element.setAttribute("TicketID", originatingTicketId);
        element.setAttribute("TicketSignature", Base64.encodeBytes(ticketSignatureData));
        element.setAttribute("Signature", Base64.encodeBytes(signatureData));
        return element;
    }
}