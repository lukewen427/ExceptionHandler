/*
 * InkspotWorkflowParameter.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;

/**
 * This class provides an implementation of an IWorkflowParameter
 * @author hugo
 */
public class InkspotWorkflowParameter extends InkspotObject implements IWorkflowParameter {

    public InkspotWorkflowParameter() {
        super();
        putProperty("blockname", "");
        putProperty("name", "");
        putProperty("value", "");
    }

    public String getBlockName() {
        return getPropertyString("blockname");
    }

    public void setBlockName(String blockName) {
        putProperty("blockname", blockName);
    }

    public String getName() {
        return getPropertyString("name");
    }

    public void setName(String name) {
        putProperty("name", name);
    }

    public String getValue() {
        return getPropertyString("value");
    }

    public void setValue(String value) {
        putProperty("value", value);
    }
}