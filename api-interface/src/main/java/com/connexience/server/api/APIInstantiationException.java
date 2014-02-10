/*
 * APIInstantiationException.java
 */

package com.connexience.server.api;

/**
 * This exception is thrown when an object cannot be instantiated from an Xml document.
 * @author nhgh
 */
public class APIInstantiationException extends Exception {

    /**
     * Creates a new instance of <code>APIInstantiationException</code> without detail message.
     */
    public APIInstantiationException() {
    }


    /**
     * Constructs an instance of <code>APIInstantiationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public APIInstantiationException(String msg) {
        super(msg);
    }
}
