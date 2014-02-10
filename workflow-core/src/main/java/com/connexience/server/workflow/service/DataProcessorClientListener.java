/*
 * DataProcessorClientListener.java
 */

package com.connexience.server.workflow.service;

/**
 * This interface defines a listener for a data processor client
 * @author hugo
 */
public interface DataProcessorClientListener {
    /** Call was received Ok by the service */
    public void messageRecieved();
    
    /** Message was rejected by the service */
    public void messageRejected(String errorMessage);
}