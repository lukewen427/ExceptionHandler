/*
 * WorkflowEngineListener.java
 */

package com.connexience.server.workflow.engine;

/**
 * This interface defines a an object that listens to a workflow engine
 * @author hugo
 */
public interface WorkflowEngineListener {
    /** An invocation has been started */
    public void invocationStarted(WorkflowInvocation invocation);

    /** An invocation has finished */
    public void invocationFinished(WorkflowInvocation invocation);
}