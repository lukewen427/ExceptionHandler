/*
 * RPCClientListener.java
 */

package com.connexience.server.workflow.rpc;

/**
 * This interface defines a listener to the RPCClient that can be notified
 * about data transfers.
 * @author hugo
 */
public interface RPCClientListener {
    /** Some data has been received */
    public void dataReceived(int bytesReceived);

    /** Transfer started. This is called when there is at least one all object waiting */
    public void transferStarted();

    /** Transfer finished. This is called when there are no call objects waiting */
    public void transferFinished();
}