/*
 * InkspotWorkflow.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;

/**
 * This class provides an implementation of the IWorkflow interface
 * @author nhgh
 */
public class InkspotWorkflow extends InskpotDocument implements IWorkflow {

    public InkspotWorkflow() {
        super();
        putProperty("supportsexternaldata", "false");
        putProperty("externaldatablockname", "");
        putProperty("currentversionsize", "0");
    }

    public String getExternalDataBlockName() {
        return getPropertyString("externaldatablockname");
    }

    public boolean isExternalDataSupported() {
        if(getPropertyString("supportsexternaldata").equalsIgnoreCase("true")){
            return true;
        } else {
            return false;
        }
    }

    public void setExternalDataBlockName(String externalDataBlockName) {
        putProperty("externaldatablockname", externalDataBlockName);
    }

    public void setExternalDataSupported(boolean externalDataSupported) {
        if(externalDataSupported){
            putProperty("supportsexternaldata", "true");
        } else {
            putProperty("supportsexternaldata", "false");
        }
    }
    
    public long getCurrentVersionSize() {
        return Long.parseLong(getPropertyString("currentversionsize"));
    }

    public void setCurrentVersionSize(long size) {
        putProperty("currentversionsize", Long.toString(size));
    }    
}