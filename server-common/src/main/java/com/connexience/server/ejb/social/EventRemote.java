/*
 * EventRemote.java
 */

package com.connexience.server.ejb.social;
import com.connexience.server.model.security.Ticket;
import com.connexience.server.model.social.Event;
import com.connexience.server.model.social.event.*;
import com.connexience.server.ConnexienceException;

import javax.ejb.Remote;
import java.util.*;

@Remote
/**
 * This interface defines the behaviour of the event management bean
 * @author hugo
 */
public interface EventRemote {
    /** Add an event for a group */
    public GroupEvent saveGroupEvent(Ticket ticket, GroupEvent event) throws ConnexienceException;

    /** List all of the events for a group */
    public List listGroupEvents(Ticket ticket, String groupId) throws ConnexienceException;

    /** List all of the events for a group in a certain timeframe */
    public List listGroupEvents(Ticket ticket, String groupId, Date startDate, Date endDate) throws ConnexienceException;
    
    /** Remove an event for a group */
    public void deleteEvent(Ticket ticket, Event event) throws ConnexienceException;

    /** Get a group event by ID */
    public GroupEvent getGroupEvent(Ticket ticket, String id) throws ConnexienceException;
}