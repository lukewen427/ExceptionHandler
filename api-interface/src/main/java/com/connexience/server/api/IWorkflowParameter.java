/*
 * IWorkflowParameter.java
 */

package com.connexience.server.api;

/**
 * This interface defines a parameter that can be passed to a workflow
 * @author hugo
 */
public interface IWorkflowParameter extends IObject {
    /** XML Document name */
    public static String XML_NAME = "WorkflowParameter";

    /** Get the block name this parameter refers to */
    public String getBlockName();

    /** Set the block name this parameter refers to */
    public void setBlockName(String blockName);

    /** Get the name of this parameter */
    public String getName();

    /** Set the name of this parameter */
    public void setName(String name);

    /** Get the value of this parameter */
    public String getValue();

    /** Set the value of this parameter */
    public void setValue(String value);
}