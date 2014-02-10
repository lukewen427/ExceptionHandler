/*
 * AutoDeployDataProcessorClient.java
 */

package com.connexience.server.workflow.service.clients;

import com.connexience.server.workflow.engine.cloud.CloudDataProcessorMessageDestination;
import com.connexience.server.workflow.service.*;
import com.connexience.server.workflow.engine.cloud.*;

import java.io.*;

/**
 * This class provides a data processor service client that knows how to send
 * messages to a local autodeploying workflow server.
 * @author nhgh
 */
public class AutoDeployDataProcessorClient extends DataProcessorClient {
    /** Global message destintion */
    private static CloudDataProcessorMessageDestination messageDestination;

    /** Set the global message destination */
    public static void setGlobalMessageDestination(CloudDataProcessorMessageDestination destination){
        messageDestination = destination;
    }

    @Override
    public void invoke(DataProcessorCallMessage message) throws DataProcessorException {
        if(messageDestination!=null){
            if(!messageDestination.postCallMessage(message)){
                notifyMessageRejected("Message rejected");
            } else {
                notifyMessageSent();
            }

        } else {
            notifyMessageRejected("No destination set");
            throw new DataProcessorException("No global destination specified");
        }
    }

    @Override
    public void terminate(DataProcessorCallMessage message) throws DataProcessorException {
        if(messageDestination!=null){
            messageDestination.terminate(message);
        }
    }

}