/*
 * APIControl.java
 */

package com.connexience.server.api;

import java.net.*;

/**
 * This interface allows API objects to be controlled without polluting the API
 * interface with these methods.
 * @author nhgh
 */
public interface APIControl {
    /** Initialise this API with a server URL */
    public void initialise(URL serverUrl);

    /** Set the API key information */
    public void setKeyDetails(String apiId, String apiKey);

    /** Set the ID of the user */
    public void setUserContextId(String userContextId);
}
