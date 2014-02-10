/*
 * CloudDataProcessorMessageDestination.java
 */

package com.connexience.server.workflow.engine.cloud;

import com.connexience.server.workflow.service.*;

/**
 * This interface defines a class that can accept data processor messages for
 * an autodeploying cloud service
 * @author nhgh
 */
public interface CloudDataProcessorMessageDestination {
    /** Post a data processor message. This method returns true if the message was accepted,
     * and false if it was rejected */
    public boolean postCallMessage(DataProcessorCallMessage message) throws DataProcessorException;


    /** Post a data processor response message. This method returns true if the message was
     * accepted and false if it was rejected */
    public boolean postResponseMessage(DataProcessorResponseMessage message) throws DataProcessorException;

    /** Terminate a service running for a call message */
    public void terminate(DataProcessorCallMessage message) throws DataProcessorException;
}