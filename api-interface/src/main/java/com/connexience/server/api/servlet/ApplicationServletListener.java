/*
 * ApplicationServletListener.java
 */

package com.connexience.server.api.servlet;

import com.connexience.server.api.*;

import javax.servlet.http.*;
import java.io.*;

/**
 * This interface defines the behavior of a listener that can manage application
 * lifecycle events such as users registering / unregistering.
 * @author nhgh
 */
public interface ApplicationServletListener {
    /** Process a registration / unregistration request */
    public void registrationEvent(IApplicationRegistration request) throws ApplicationException;

    /** Render a summary view to the output stream */
    public void renderSummary(HttpServletRequest request, HttpServletResponse response, String contextId, API link, String userId) throws ApplicationException;
}