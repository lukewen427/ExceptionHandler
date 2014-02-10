/*
 * TicketData.java
 */

package com.connexience.server.model.security;
import com.connexience.server.util.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.axis.message.*;
import org.w3c.dom.*;

/** Simple class that just contains a ticket id and a signature */
public class TicketData implements Serializable {
    /** Property name of the ticket data in the AxisEngine property collection */
    public static final String ticketPropertyName = "TICKET_DATA_CONTEXT";
    
    /** Element name in XML */
    public static final String elementName = "Ticket";
    
    /** Ticket namespace */
    public static final String ticketNS = "http://connexience.com/ticket";
    
    /** Ticket ID */
    private String ticketId;
    
    /** Signature data */
    private byte[] signatureData;
    
    /** Organisation ID of the ticket */
    private String organisationId;
    
    /** Create with data */
    public TicketData(String ticketId, byte[] signatureData){
        this.ticketId = ticketId;
        this.signatureData = signatureData;
    }
    
    /** Create with just an ID. Signature needs to be added later */
    public TicketData(String ticketId){
        this.ticketId = ticketId;
        signatureData = new byte[0];
    }
    
    /** Empty constructor */
    public TicketData(){
        ticketId = "";
        organisationId = "";
        signatureData = new byte[0];
    }
    
    /** Save to an Xml encoded String */
    public String toXmlString(){
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            
            Element root = doc.createElementNS(ticketNS, "TicketData");
            doc.appendChild(root);
            saveToElement(doc, root);
            StringWriter writer = new StringWriter();
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.toString();
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
    
    /** Parse from an Xml encoded String */
    public void parseXmlString(String xmlTicket){
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(xmlTicket.getBytes());
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
            Element root = doc.getDocumentElement();
            NodeList children = root.getChildNodes();
            if(children.getLength()==1){
                System.out.println(children.item(0).getNodeName());
                setFromElement((Element)children.item(0));
            }
            
        } catch (Exception e){
            e.printStackTrace();
            signatureData = new byte[0];
            organisationId = "";
            ticketId = "";
        }
    }
    
    /** Save to a SOAPHeaderElement */
    public SOAPHeaderElement saveToSOAPHeaderElement() {
        SOAPHeaderElement element = new SOAPHeaderElement(ticketNS, elementName);
        element.setAttribute("TicketID", ticketId);
        element.setAttribute("OrgID", organisationId);
        element.setAttribute("Signature", Base64.encodeBytes(signatureData));
        return element;
    }
    
    /** Save to an XML element */
    public Element saveToElement(Document doc, Element parent){
        Element e = doc.createElementNS(ticketNS, elementName);
        e.setAttribute("TicketID", ticketId);
        e.setAttribute("OrgID", organisationId);
        e.setAttribute("Signature", Base64.encodeBytes(signatureData));
        parent.appendChild(e);
        return e;
    }
    
    /** Set values from an XML element */
    public void setFromElement(Element e){
        if(e.getNodeName().endsWith(elementName) || e.getLocalName().equalsIgnoreCase(elementName)){
            ticketId = e.getAttribute("TicketID");
            organisationId = e.getAttribute("OrgID");
            String sigEncoded = e.getAttribute("Signature");
            if(sigEncoded!=null){
                signatureData = Base64.decode(sigEncoded);
            } else {
                signatureData = new byte[0];
            }
        }
    }
    
    /** Sign using a PrivateKey */
    public void sign(PrivateKey key){
        try {
            String alg = KeyData.sigAlg; 
            Signature sig = Signature.getInstance(alg);
            sig.initSign(key);
            sig.update(ticketId.getBytes());
            sig.update(organisationId.getBytes());
            signatureData = sig.sign();
            
        } catch (Exception e){
            signatureData = new byte[0];
            System.out.println("Error signing ticket: " + e.getMessage());
        }
    }
    
    /** Validate using a certificate */
    public boolean validate(X509Certificate cert){
        try {
            Signature sig = Signature.getInstance(KeyData.sigAlg);
            sig.initVerify(cert);
            sig.update(ticketId.getBytes());
            sig.update(organisationId.getBytes());
            return sig.verify(signatureData);
            
        } catch (Exception e){
            System.out.println("Ticket validation error: " + e.getMessage());
            return false;
        }
    }
    
    /** Get the ID */
    public String getTicketId(){
        return ticketId;
    }
    
    /** Set the ID */
    public void setTicketId(String ticketId){
        this.ticketId = ticketId;
    }
    
    /** Get the signature data */
    public byte[] getSignatureData(){
        return signatureData;
    }
    
    /** Set the signature data */
    public void setSignatureData(byte[] signatureData){
        if(signatureData!=null){
            this.signatureData = signatureData;
        } else {
            this.signatureData = new byte[0];
        }
    }

    /** Get the id of the organisation that issued this ticket */
    public String getOrganisationId() {
        return organisationId;
    }

    /** Set the id of the organisation that issued this ticket */
    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }
}
