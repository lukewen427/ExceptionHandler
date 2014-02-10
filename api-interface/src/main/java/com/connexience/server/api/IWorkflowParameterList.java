/*
 * IWorkflowParameterList.java
 */

package com.connexience.server.api;

/**
 * This interface defines a list of parameters that can be passed to a
 * workflow invocation to specifiy block parameters.
 * @author hugo
 */
public interface IWorkflowParameterList extends IObjectList {
    /** Name used in XML documents */
    public static final String XML_NAME = "WorkflowParameterList";

    /** Get the number of parameters in this list */
    public int size();

    /** Get a specific parameter */
    public IWorkflowParameter getParameter(int index);

    /** Add a workflow parameter */
    public void addParameter(IWorkflowParameter parameter);

    /** Add a workflow parameter */
    public void add(IWorkflowParameter parameter);

    /** Automatically add a parameter */
    public void add(String blockName, String parameterName, String parameterValue);

    /** Automatically add a parameter using the block.name = value syntax */
    public void add(String parameterId, String parameterValue);
}
