/*
 * APISecurityException.java
 */

package com.connexience.server.api;

/**
 * This exception is thrown by the security code
 * @author hugo
 */
public class APISecurityException extends Exception {

    /**
     * Creates a new instance of <code>APISecurityException</code> without detail message.
     */
    public APISecurityException() {
    }


    /**
     * Constructs an instance of <code>APISecurityException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public APISecurityException(String msg) {
        super(msg);
    }
}
