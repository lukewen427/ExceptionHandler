/*
 * InkspotDynamicWorkflowLibrary.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;

/**
 * This class provides the default implementation of the dynamic workflow library
 * API wrapped object.
 * @author nhgh
 */
public class InkspotDynamicWorkflowLibrary extends InskpotDocument implements IDynamicWorkflowLibrary {

    public InkspotDynamicWorkflowLibrary() {
        super();
        putProperty("libraryname", "");
        putProperty("currentversionsize", "0");
    }

    /** Get the human readable library name */
    public String getLibraryName() {
        return getPropertyString("libraryname");
    }

    /** Set the human readable library name */
    public void setLibraryName(String libraryName) {
        putProperty("libraryname", libraryName);
    }
    
    public long getCurrentVersionSize() {
        return Long.parseLong(getPropertyString("currentversionsize"));
    }

    public void setCurrentVersionSize(long size) {
        putProperty("currentversionsize", Long.toString(size));
    }    
}