/*
 * InkspotAPIFactory.java
 */

package com.connexience.server.api;
import java.net.*;

/**
 * This interface defines the functionality of the factory that can provide
 * InkspotAPI connection objects.
 * @author hugo
 */
public class APIFactory {
    /** Class of the API to return */
    private Class apiClass;

    /** Set the API class */
    public void setApiClass(Class apiClass){
        this.apiClass = apiClass;
    }

    /** Instantiate an API connection */
    private API instantiateApiClass(URL serverUrl) throws APIConnectException {
        API apiObject = null;
        try {
            apiObject = (API)apiClass.newInstance();
        } catch (Exception e){
            throw new APIConnectException("Cannot create API object");
        }

        ((APIControl)apiObject).initialise(serverUrl);
        return apiObject;
    }
    /** Get a connected API instance by using an application ID and a key */
    public API authenticateApplication(URL serverUrl, String applicationId, String applicationKey) throws APIConnectException {
        API api = instantiateApiClass(serverUrl);
        ((APIControl)api).setKeyDetails(applicationId, applicationKey);
        return api;
    }
}