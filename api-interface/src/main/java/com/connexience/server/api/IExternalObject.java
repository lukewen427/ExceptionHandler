/*
 * IExternalObject.java
 */

package com.connexience.server.api;

/**
 * This interface defines an object reference that is saved in the users
 * external objects folder that can be used to allow the system to manage
 * access control restrictions for objects that are hosted externally.
 * @author nhgh
 */
public interface IExternalObject extends IFolder {
    /** XML Document name */
    public static final String XML_NAME = "ExternalObject";

    // Some standard type strings

    /** Object is an external database */
    public static final String DATABASE_OBJECT = "DATABASE";

    /** Get the ID of the application that created this object */
    public String getApplicationId();

    /** Set the ID of the application that created this object */
    public void setApplicationId(String applicationId);

    /** Get the type identification string */
    public String getTypeString();

    /** Set the type identification string */
    public void setTypeString(String typeString);
}