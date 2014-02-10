/*
 * ApplicationException.java
 */

package com.connexience.server.api;

/**
 * This class provides an exception that can be thrown by external applications
 * when there is an internal error on their side.
 * @author nhgh
 */
public class ApplicationException extends Exception {

    /**
     * Creates a new instance of <code>ApplicationException</code> without detail message.
     */
    public ApplicationException() {
    }


    /**
     * Constructs an instance of <code>ApplicationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ApplicationException(String msg) {
        super(msg);
    }
}
