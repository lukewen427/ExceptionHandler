/*
 * APIParseException.java
 */

package com.connexience.server.api;

/**
 * This exception is thrown when a failure in parsing and XML document is
 * encountered.
 * @author nhgh
 */
public class APIParseException extends Exception {

    /**
     * Creates a new instance of <code>APIParseException</code> without detail message.
     */
    public APIParseException() {
    }


    /**
     * Constructs an instance of <code>APIParseException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public APIParseException(String msg) {
        super(msg);
    }
}
