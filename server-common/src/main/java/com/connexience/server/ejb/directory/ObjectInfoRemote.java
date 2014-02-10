
package com.connexience.server.ejb.directory;

import javax.ejb.Remote;
import com.connexience.server.*;
import com.connexience.server.model.security.Ticket;

/**
 * This is the business interface for ObjectInfo enterprise bean.
 */
@Remote
public interface ObjectInfoRemote {
    /**
     * Get the name of an object
     */
    java.lang.String getObjectName(Ticket ticket, String objectId) throws ConnexienceException;

    /**
     * Get the names of a collection of objects
     */
    java.lang.String[] getObjectNames(Ticket ticket, String[] objectIds) throws ConnexienceException;

    /**
     * Does an organisation contain a specific object
     */
    boolean containsObject(Ticket ticket, String objectId) throws ConnexienceException;
    
}
