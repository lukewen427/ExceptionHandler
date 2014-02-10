/*
 * RPCDataProcessorClient
 */

package com.connexience.server.workflow.service.clients;

import com.connexience.server.workflow.service.*;
import com.connexience.server.workflow.rpc.*;
import org.pipeline.core.xmlstorage.*;
import java.io.*;

/**
 * This client uses the built in java RPC code to send messages to the 
 * data processor services.
 * @author hugo
 */
public class RPCDataProcessorClient extends DataProcessorClient implements CallInvocationListener, Serializable {
    /** Invoke the service */
    public void invoke(DataProcessorCallMessage message) throws DataProcessorException {
        try {
            
            CallObject call = new CallObject("processCallMessage");
            call.getCallArguments().add("CallMessage", message);
            
            RPCClient client = new RPCClient(message.getServiceUrl());
            client.setDefaultTimeout(getTimeout());
            client.asyncCall(call, this);
            
        } catch (XmlStorageException xmlse){
            throw new DataProcessorException("XML Serialization Error: " + xmlse.getMessage());
        } catch (RPCException rpce){
            throw new DataProcessorException("Communications Error: "+ rpce.getMessage());
        }
    }

    @Override
    public void terminate(DataProcessorCallMessage message) throws DataProcessorException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** The RPC call has failed */
    public void callFailed(CallObject call) {
        notifyMessageRejected(call.getStatusMessage());
    }

    /** The RPC call was sent Ok */
    public void callSucceeded(CallObject call) {
        notifyMessageSent();
    }    
}