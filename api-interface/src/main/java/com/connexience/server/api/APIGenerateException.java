/*
 * APIGenerateException.java
 */

package com.connexience.server.api;

/**
 * This exception is thrown when Xml documents cannot be generated by the API tools.
 * @author nhgh
 */
public class APIGenerateException extends Exception {

    /**
     * Creates a new instance of <code>APIGenerateException</code> without detail message.
     */
    public APIGenerateException() {
    }


    /**
     * Constructs an instance of <code>APIGenerateException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public APIGenerateException(String msg) {
        super(msg);
    }
}
