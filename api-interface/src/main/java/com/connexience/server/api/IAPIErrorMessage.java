/*
 * IAPIErrorMessage.java
 */

package com.connexience.server.api;

/**
 * This class defines the functionality of an error message that can be returned
 * from the server when something goes wrong.
 * @author nhgh
 */
public interface IAPIErrorMessage extends IObject {
    /** XML Name for object */
    public static final String XML_NAME = "APIErrorMessage";

    /** Get the error message */
    public String getErrorMessage();

    /** Set the error message */
    public void setErrorMessage(String errorMessage);

}