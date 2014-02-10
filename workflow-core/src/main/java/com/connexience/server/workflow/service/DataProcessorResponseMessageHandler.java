/*
 * DataProcessorResponseMessageHandler.java
 */

package com.connexience.server.workflow.service;

/**
 * This interface defines a class that can send data processor
 * response messages back to the callback server 
 * @author hugo
 */
public interface DataProcessorResponseMessageHandler {
    /** Send a response message */
    public void sendResponseMessage(DataProcessorResponseMessage message) throws DataProcessorException;
}