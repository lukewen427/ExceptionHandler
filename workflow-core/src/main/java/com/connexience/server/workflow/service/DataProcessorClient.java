/*
 * DataProcessorClient.java
 */

package com.connexience.server.workflow.service;

import java.util.*;
/**
 * This abstract calass defines the client side functionality of the data processor service. It
 * allows the caller to send data references to the server asynchronously.
 * @author hugo
 */
public abstract class DataProcessorClient {
    /** Service URL */
    private String url;
    
    /** Listeners */
    private Vector<DataProcessorClientListener> listeners = new Vector<DataProcessorClientListener>();
    
    /** Response timeout in seconds */
    private int timeout = 3600;
    
    /** Set the service URL string */
    public void setUrl(String url) {
        this.url = url;
    }
    
    /** Set the response timeout */
    public void setTimeout(int timeout){
        this.timeout = timeout;
    }
    
    /** Get the response timeout */
    public int getTimeout(){
        return timeout;
    }

    /** Remove all the listeners */
    public void removeAllListeners(){
        listeners.clear();
    }
    
    /** Add a listener */
    public void addListener(DataProcessorClientListener listener){
        listeners.add(listener);
    }
    
    /** Remove a listener */
    public void removeListener(DataProcessorClientListener listener){
        listeners.remove(listener);
    }
    
    /** Notify sucessful message transmission */
    protected void notifyMessageSent(){
        Vector<DataProcessorClientListener> tempList = new Vector<DataProcessorClientListener>(listeners);
        Iterator<DataProcessorClientListener> i = tempList.iterator();
        while(i.hasNext()){
            i.next().messageRecieved();
        }
    }
    
    /** Notify message failure */
    protected void notifyMessageRejected(String errorMessage){
        Vector<DataProcessorClientListener> tempList = new Vector<DataProcessorClientListener>(listeners);
        Iterator<DataProcessorClientListener> i = tempList.iterator();
        while(i.hasNext()){
            i.next().messageRejected(errorMessage);
        }
    }
    
    /** Invoke the service asynchronously */
    public abstract void invoke(DataProcessorCallMessage message) throws DataProcessorException;

    /** Terminate a service instance if possible */
    public abstract void terminate(DataProcessorCallMessage message) throws DataProcessorException;
}
