/*
 * ConnexienceSessionListener.java
 */

package com.connexience.server.util;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * This class listens to http sessions and maintains a list of active sessions
 * in the database
 * @author hugo
 */
public class ConnexienceSessionListener implements HttpSessionListener {

    public void sessionCreated(HttpSessionEvent hse) {
        // TODO: Store session details in database
    }

    public void sessionDestroyed(HttpSessionEvent hse) {
        // TODO: Remove session details from databse
    }
}