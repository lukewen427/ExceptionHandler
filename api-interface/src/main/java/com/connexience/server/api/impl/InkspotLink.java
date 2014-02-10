/*
 * InkspotLink.java
 */

package com.connexience.server.api.impl;
import com.connexience.server.api.*;

/**
 * This class provides an implmentation of the ILink interface
 * @author hugo
 */
public class InkspotLink extends InkspotSecuredObject implements ILink {

    public InkspotLink() {
        super();
        putProperty("sourceobjectid", "");
        putProperty("targetobjectid", "");
    }

    public String getSourceObjectId() {
        return getPropertyString("sourceobjectid");
    }

    public String getTargetObjectId() {
        return getPropertyString("targetobjectid");
    }

    public void setSourceObjectId(String sourceObjectId) {
        putProperty("sourceobjectid", sourceObjectId);
    }

    public void setTargetObjectId(String targetObjectId) {
        putProperty("targetobjectid", targetObjectId);
    }
}