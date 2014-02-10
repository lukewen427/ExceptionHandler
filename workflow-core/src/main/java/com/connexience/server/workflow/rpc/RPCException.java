/*
 * RPCException.java
 */

package com.connexience.server.workflow.rpc;

/**
 * This exception is thrown by the RPC code
 * @author hugo
 */
public class RPCException extends Exception {

    /**
     * Creates a new instance of <code>RPCException</code> without detail message.
     */
    public RPCException() {
    }


    /**
     * Constructs an instance of <code>RPCException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public RPCException(String msg) {
        super(msg);
    }

    public RPCException(String msg, Throwable cause){
        super(msg, cause);
    }
}
