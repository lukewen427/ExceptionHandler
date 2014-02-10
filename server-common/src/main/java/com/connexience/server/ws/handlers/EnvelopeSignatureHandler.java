/*
 * EnvelopeSignatureHandler.java
 */

package com.connexience.server.ws.handlers;

import com.connexience.server.model.*;
import com.connexience.server.model.security.*;
import org.apache.axis.*;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.message.*;
import org.w3c.dom.*;

/**
 * This handler signs the SOAP envelopes with the key of the user making the
 * call if this is a client side handler. On the server side, this handler
 * verifies that the signature matches the users key and places a reference
 * to the user in the Axis engine. This is used with the ticket to ensure
 * that the user using the ticket matches the UserId specified in the 
 * ticket database.
 * @author nhgh
 */
public class EnvelopeSignatureHandler {
    
    /** Creates a new instance of EnvelopeSignatureHandler */
    public EnvelopeSignatureHandler() {
    }
    
}
