/*
 * PropertiesRemote.java
 */

package com.connexience.server.ejb.properties;

import com.connexience.server.*;
import com.connexience.server.model.security.*;
import com.connexience.server.model.properties.*;

import javax.ejb.Remote;
import java.util.*;

/**
 * This interface defines the functionality of the property access bean.
 * @author nhgh
 */
@Remote
public interface PropertiesRemote
{
    /** Get the properties groups for a specific object */
    public List getObjectPropertyGroups(Ticket ticket, String objectId) throws ConnexienceException;
    
    /** Get a specific property group */
    public PropertyGroup getPropertyGroup(Ticket ticket, long propertyGroupId) throws ConnexienceException;
    
    /** Get a named property group for a specific object */
    public PropertyGroup getPropertyGroup(Ticket ticket, String objectId, String propertyGroupName) throws ConnexienceException;
    
    /** Save a property group */
    public PropertyGroup savePropertyGroup(Ticket ticket, PropertyGroup propertyGroup) throws ConnexienceException;
    
    /** Remove a property group */
    public void removePropertyGroup(Ticket ticket, long propertyGroupId) throws ConnexienceException;
    
    /** Get a named system property in the form of PropertyGroupName, PropertyName */
    public PropertyItem getSystemProperty(Ticket ticket, String propertyGroupName, String propertyName) throws ConnexienceException;
    
    /** Set a named system property */
    public void setSystemProperty(Ticket ticket, String propertyGroupName, String propertyName, String propertyValue) throws ConnexienceException;
    
    /** Get a property */
    public PropertyItem getProperty(Ticket ticket, String objectId, String propertyGroupName, String propertyName) throws ConnexienceException;
    
    /** Set a property */
    public void setProperty(Ticket ticket, String objectId, String propertyGroupName, String propertyName, String propertyValue) throws ConnexienceException;
    
    /** Remove a property */
    public void removeProperty(Ticket ticket, long propertyId) throws ConnexienceException;

    /** Remove a property from a group by name */
    public void removeProperty(Ticket ticket, String objectId, String propertyGroupName, String propertyName) throws ConnexienceException;

    /** List the system property groups */
    public List getSystemPropertyGroups(Ticket ticket) throws ConnexienceException;
}