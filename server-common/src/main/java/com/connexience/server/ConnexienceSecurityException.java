/*
 * ConnexienceSecurityException.java
 */

package com.connexience.server;

/**
 * This Exception is thrown whenever there is a security violation
 * @author nhgh
 */
public class ConnexienceSecurityException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>ConnexienceSecurityException</code> without detail message.
     */
    public ConnexienceSecurityException() {
    }
    
    
    /**
     * Constructs an instance of <code>ConnexienceSecurityException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ConnexienceSecurityException(String msg) {
        super(msg);
    }
}
