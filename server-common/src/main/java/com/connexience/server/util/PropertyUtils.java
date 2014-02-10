/*
 * PropertyUtils.java
 */

package com.connexience.server.util;

import com.connexience.server.ejb.util.*;
import com.connexience.server.model.properties.*;
import com.connexience.server.model.security.*;

/**
 * This class provides some simple property utility methods
 * @author nhgh
 */
public class PropertyUtils
{
    public static String getSystemProperty(String groupName, String propertyName, String defaultValue){
        try {
            Ticket ticket = EJBLocator.lookupTicketBean().createPublicWebTicket();
            PropertyItem item = EJBLocator.lookupPropertiesBean().getSystemProperty(ticket, groupName, propertyName);
            if(item!=null){
                return item.getValue();
            } else {
                System.out.println("Warning: Property " + groupName + "/" + propertyName + " is undefined using: " + defaultValue);
                return defaultValue;
            }
        } catch (Exception e){
            System.out.println("Error reading system property: " + groupName + "/" + propertyName + ": " + e.getMessage());
            return defaultValue;
        }
    }

    public static String getSystemProperty(Ticket ticket, String groupName, String propertyName, String defaultValue){
        try {
            PropertyItem item = EJBLocator.lookupPropertiesBean().getSystemProperty(ticket, groupName, propertyName);
            if(item!=null){
                return item.getValue();
            } else {
                System.out.println("Warning: Property " + groupName + "/" + propertyName + " is undefined using: " + defaultValue);
                return defaultValue;
            }
        } catch (Exception e){
            System.out.println("Error reading system property: " + groupName + "/" + propertyName + ": " + e.getMessage());
            return defaultValue;
        }
    }
}
