/*
 * IDynamicWorkflowLibrary.java
 */

package com.connexience.server.api;

/**
 * This interface defines a dynamic workflow library item that is automatically
 * deployed when calling dynamic services.
 * @author nhgh
 */
public interface IDynamicWorkflowLibrary  extends IDocument {

    public static final String XML_NAME = "DynamicWorkflowLibrary";

    /** Get the service category */
    public String getLibraryName();

    /** Set the service category */
    public void setLibraryName(String libraryName);
}