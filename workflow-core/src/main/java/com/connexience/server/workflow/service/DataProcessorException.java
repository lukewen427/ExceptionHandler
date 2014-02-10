/*
 * DataProcessorException.java
 */

package com.connexience.server.workflow.service;

/**
 * This exception is thrown by the data processor calls
 * @author hugo
 */
public class DataProcessorException extends Exception {

    /**
     * Creates a new instance of <code>DataProcessorException</code> without detail message.
     */
    public DataProcessorException() {
    }


    /**
     * Constructs an instance of <code>DataProcessorException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DataProcessorException(String msg) {
        super(msg);
    }

    public DataProcessorException(String msg, Throwable cause){
        super(msg, cause);
    }
}
