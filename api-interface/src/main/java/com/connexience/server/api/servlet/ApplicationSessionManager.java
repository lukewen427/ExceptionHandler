/*
 * ApplicationSessionManager.java
 */

package com.connexience.server.api.servlet;

import com.connexience.server.api.*;
import java.io.*;
import java.util.*;
import javax.servlet.http.*;

/**
 * This class provides a session manager that is used to control storage of session
 * objects.
 * @author nhgh
 */
public class ApplicationSessionManager implements Serializable {
    /** Global application manager instance */
    public static final ApplicationSessionManager GLOBAL_INSTANCE = new ApplicationSessionManager();
    
    /** Session store */
    private Hashtable<String,ApplicationSession> sessionStore = new Hashtable<String,ApplicationSession>();
    
    /** Private constructor so that there is only one session manager */
    private ApplicationSessionManager() {
    }

    /** Get a session for a specified ID. This is typically the session ID from the servlet container.
     * If the session does not exist, then a new one will be created */
    public ApplicationSession getSession(HttpServletRequest request) {
        // Check to see if the request has a context ID associated with it.
        // If it does, then either:
        //  - return the session if it exists
        //  - create a new session and store it's id in a new http session object
        //
        // If it does not, then:
        //  - check the http session for an id and return a new app session object
        //
        // If both of these fail, a new session will be returned, and stored in
        // the http session. It won't however, contain the correct data to
        // talk to the remote server.
        HttpSession htSession;

        if(request.getParameter("contextid")!=null){
            String contextId = request.getParameter("contextid");
            if(sessionStore.containsKey(contextId)){
                return sessionStore.get(contextId);
            } else {
               ApplicationSession session = new ApplicationSession();
               session.setId(contextId);

               // Configure the link if it is present in the session
               htSession = request.getSession(true);
               if(htSession.getAttribute("LINKOBJECT")!=null){
                   session.setLink((API)htSession.getAttribute("LINKOBJECT"));
               }
               sessionStore.put(contextId, session);
               htSession.setAttribute("APPLICATIONSESSION", session);
               return session;
            }

        } else {
            htSession = request.getSession(true);
            if(htSession.getAttribute("APPLICATIONSESSION")!=null){
                return (ApplicationSession)htSession.getAttribute("APPLICATIONSESSION");
            } else {
                ApplicationSession session = new ApplicationSession();
                session.setId(htSession.getId());
                sessionStore.put(htSession.getId(), session);
                htSession.setAttribute("APPLICATIONSESSION", session);
                return session;
            }
        }
    }

    /** Check for timed out sessions */
    public void removeDeadSessions(){

    }
}