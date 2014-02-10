/*
 * CallInvocation
 */

package com.connexience.server.workflow.rpc;

/**
 * This class defines an object that can track the progress of call invocations
 * @author hugo
 */
public interface CallInvocationListener {
    /** Call suceeded */
    public void callSucceeded(CallObject call);
    
    /** Call failed */
    public void callFailed(CallObject call);
}