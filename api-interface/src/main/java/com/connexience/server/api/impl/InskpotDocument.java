/*
 * InkspotDocument.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;


/**
 * This class provides an Inkspot implementation of a Document object
 * @author hugo
 */
public class InskpotDocument extends InkspotSecuredObject implements IDocument {

    public InskpotDocument() {
        super();
        putProperty("currentversionsize", "0");
    }
    public long getCurrentVersionSize() {
        return Long.parseLong(getPropertyString("currentversionsize"));
    }

    public void setCurrentVersionSize(long size) {
        putProperty("currentversionsize", Long.toString(size));
    }
}
