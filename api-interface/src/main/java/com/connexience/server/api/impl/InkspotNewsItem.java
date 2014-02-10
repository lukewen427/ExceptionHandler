/*
 * InkspotNewsItem.java
 */

package com.connexience.server.api.impl;
import com.connexience.server.api.*;
/**
 * This class provides an implementation of a news item that can be used with
 * the REST interface.
 * @author nhgh
 */
public class InkspotNewsItem extends InkspotSecuredObject implements INewsItem {

    public InkspotNewsItem() {
        super();
        putProperty("timestamp", "");
        putProperty("label", "");
        putProperty("mainitemid", "");
    }

    public String getLabel() {
        return getPropertyString("label");
    }

    public void setLabel(String label) {
        putProperty("label", label);
    }

    public String getTimestamp() {
        return getPropertyString("timestamp");
    }

    public void setTimestamp(String timestamp) {
        putProperty("timestamp", timestamp);
    }

  public String getMainItemId() {
        return getPropertyString("mainitemid");
    }

    public void setMainItemId(String mainItemId) {
        putProperty("mainitemid", mainItemId);
    }


}