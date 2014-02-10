/*
 * HttpDataProcessorClient.java
 */

package com.connexience.server.workflow.service.clients;

import com.connexience.server.workflow.service.*;
import com.connexience.server.util.*;
import java.io.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;

/**
 * This client sends data processor call messages via a simple http post using
 * the message xml representation.
 * @author nhgh
 */
public class HttpDataProcessorClient extends DataProcessorClient implements Serializable {

    public HttpDataProcessorClient() {
        
    }

    /** Send the message to the server */
    public void invoke(final DataProcessorCallMessage message) throws DataProcessorException {

        try {
            HttpClient client = new HttpClient();
            PostMethod method = new PostMethod(message.getServiceUrl());
            method.setRequestHeader("Content-Type", "text/xml");
            method.setRequestBody(XmlUtils.xmlDocumentToString(message.toXmlDocument()));
            int status = client.executeMethod(method);
            if(status==200){
                notifyMessageSent();
            } else {
                notifyMessageRejected("Error HTTP Code: " + status);
            }
        } catch (Exception e){
            notifyMessageRejected(e.getMessage());

        }

    }

    @Override
    public void terminate(DataProcessorCallMessage message) throws DataProcessorException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}
