/*
 * ApplicationDataFetcherFactory.java
 */

package com.connexience.server.api.servlet;

import java.util.*;

/**
 * This class provides a factory that can be kept in the http session on the
 * server to create data fetchers that talk to external applications. The
 * data fetchers are stored here so that client side session ids can be stored
 * during the users session.
 * @author nhgh
 */
public class ApplicationDataFetcherFactory {
    /** List of existing fetchers indexed by application id */
    private Hashtable<String, ApplicationDataFetcher> fetchers = new Hashtable<String, ApplicationDataFetcher>();

    /** Create a fetcher for a subscription */
    public ApplicationDataFetcher getDataFetcher(String applicationId, String url){
        if(fetchers.containsKey(applicationId)){
            return fetchers.get(applicationId);
        } else {
            ApplicationDataFetcher fetcher = new ApplicationDataFetcher(url, applicationId);
            fetchers.put(applicationId, fetcher);
            return fetcher;
        }
    }
}