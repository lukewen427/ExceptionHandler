/*
 * WorkflowInvocationException.java
 */

package com.connexience.server.workflow.engine;

/**
 * This exception is thown by the workflow invocation engine
 * @author hugo
 */
public class WorkflowInvocationException extends Exception {

    /**
     * Creates a new instance of <code>WorkflowInvocationException</code> without detail message.
     */
    public WorkflowInvocationException() {
    }


    /**
     * Constructs an instance of <code>WorkflowInvocationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public WorkflowInvocationException(String msg) {
        super(msg);
    }

    public WorkflowInvocationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
