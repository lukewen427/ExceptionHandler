/*
 * IWorkflow.java
 */

package com.connexience.server.api;

/**
 * This class defines a workflow object that subclasses the standard IDocument
 * object
 * @author nhgh
 */
public interface IWorkflow extends IDocument {
    /** XML Name for object */
    public static final String XML_NAME = "WorkflowDocument";

    /** Is external data supported by this workflow */
    public boolean isExternalDataSupported();

    /** Set whether this workflow supports external data */
    public void setExternalDataSupported(boolean externalDataSupported);

    /** Get the name of the external data source block */
    public String getExternalDataBlockName();

    /** Set the name of the external data source block */
    public void setExternalDataBlockName(String externalDataBlockName);
}