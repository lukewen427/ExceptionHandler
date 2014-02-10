/*
 * APIConnectException.java
 */

package com.connexience.server.api;
import java.rmi.*;
/**
 * This exception is thrown during connection errors.
 * @author nhgh
 */
public class APIConnectException extends RemoteException {

    /**
     * Creates a new instance of <code>APIConnectException</code> without detail message.
     */
    public APIConnectException() {
    }


    /**
     * Constructs an instance of <code>APIConnectException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public APIConnectException(String msg) {
        super(msg);
    }

    public APIConnectException(String msg, Throwable cause){
        super(msg, cause);
    }
}
