/*
 * WorkflowInvocationListener.java
 */

package com.connexience.server.workflow.engine;

/**
 * This interface defines a workflow invocation listener
 * @author hugo
 */
public interface WorkflowInvocationListener {
    public void executionFinished(WorkflowInvocation invocation);
}