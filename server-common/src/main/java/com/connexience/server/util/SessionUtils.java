/*
 * SessionUtils.java
 */
package com.connexience.server.util;

import com.connexience.server.ejb.util.EJBLocator;
import com.connexience.server.model.logging.LogMessage;
import com.connexience.server.model.logging.events.LogEvent;
import com.connexience.server.model.organisation.Organisation;
import com.connexience.server.model.security.User;
import com.connexience.server.model.security.WebTicket;
import com.connexience.server.model.social.profile.UserProfile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This class provides utility methods to support session management and
 * the security services.
 *
 * @author hugo
 */
public class SessionUtils {

    /**
     * Check that the session contains all of the required parameters
     */
    private static void checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession(true);

        WebTicket ticket = null;
        User user = null;
        UserProfile profile = null;

        // Set up the security ticket
        if (session.getAttribute("TICKET") == null) {
            try {
                ticket = EJBLocator.lookupTicketBean().createPublicWebTicket();
                session.setAttribute("TICKET", ticket);
            }
            catch (Exception e) {
                
            }
        } else {
            ticket = (WebTicket) session.getAttribute("TICKET");
        }

        // Set up the current user
        if (session.getAttribute("USER") == null) {
            try {
                user = EJBLocator.lookupUserDirectoryBean().getUser(ticket, ticket.getUserId());
                session.setAttribute("USER", user);
            }
            catch (Exception e) {
                
            }

        } else {
            user = (User) session.getAttribute("USER");
        }

        // Set up the profile
        if (session.getAttribute("PROFILE") == null) {
            try {
                profile = EJBLocator.lookupUserDirectoryBean().getUserProfile(ticket, user.getId());
                session.setAttribute("PROFILE", profile);
            }
            catch (Exception e) {
                
            }
        }

        // Set up the organisation
        if (session.getAttribute("ORGANISATION") == null) {
            try {
                Organisation organisation = EJBLocator.lookupOrganisationDirectoryBean().getDefaultOrganisation(ticket);
                session.setAttribute("ORGANISATION", organisation);
            }
            catch (Exception e) {
                
            }
        }

    }

    /**
     * Get the current organisation
     */
    public static Organisation getOrganisation(HttpServletRequest request) {
        checkSession(request);
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute("ORGANISATION") != null) {
            return (Organisation) session.getAttribute("ORGANISATION");
        } else {
            return null;
        }
    }

    /**
     * Get the current WebTicket
     */
    public static WebTicket getTicket(HttpServletRequest request) {
        checkSession(request);
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute("TICKET") != null) {
            return (WebTicket) session.getAttribute("TICKET");
        } else {
            return null;
        }
    }

    /**
     * Get the current User
     */
    public static User getUser(HttpServletRequest request) {
        checkSession(request);
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute("USER") != null) {
            return (User) session.getAttribute("USER");
        } else {
            return null;
        }
    }

    /**
     * Get the current profile
     */
    public static UserProfile getProfile(HttpServletRequest request) {
        checkSession(request);
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute("PROFILE") != null) {
            return (UserProfile) session.getAttribute("PROFILE");
        } else {
            return null;
        }
    }

    /**
     * Reset the ticket and user to public
     */
    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            //todo: log that the user has logged out
            session.removeAttribute("TICKET");
            session.removeAttribute("USER");
            session.removeAttribute("ORGANISATION");
            session.removeAttribute("PROFILE");
            checkSession(request);
        }

    }

    /**
     * Log in a user with a username and password
     */
    public static void login(HttpServletRequest request, String username, String password) {
        HttpSession session = request.getSession(true);
        try {
            session.removeAttribute("TICKET");
            session.removeAttribute("USER");
            session.removeAttribute("ORGANISATION");
            session.removeAttribute("PROFILE");

            WebTicket ticket = EJBLocator.lookupTicketBean().createWebTicket(username, password);
            session.setAttribute("TICKET", ticket);
            checkSession(request);
        }
        catch (Exception e) {
            // Revert to public user
            logout(request);
        }
    }

    /**
     * Log in a user with a userId - used by 'RememberMe'
     */
    public static void login(HttpServletRequest request, String userId) {
        HttpSession session = request.getSession(true);
        try {
            session.removeAttribute("TICKET");
            session.removeAttribute("USER");
            session.removeAttribute("ORGANISATION");
            session.removeAttribute("PROFILE");

            WebTicket ticket = EJBLocator.lookupTicketBean().createWebTicketForDatabaseId(userId);
            session.setAttribute("TICKET", ticket);
            checkSession(request);
        }
        catch (Exception e) {
            // Revert to public user
            logout(request);
        }
    }

    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        if (request != null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
        }
        return null;
    }

}
