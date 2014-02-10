/*
 * InkspotWorkflowParameterList.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;

/**
 * This class provides an implementation of the IWorkflowParameterList
 * @author hugo
 */
public class InkspotWorkflowParameterList extends InkspotObjectList implements IWorkflowParameterList {
    public InkspotWorkflowParameterList() {
        super();
    }

    public void addParameter(IWorkflowParameter parameter) {
        super.add(parameter);
    }

    public void add(IWorkflowParameter parameter) {
        super.add(parameter);
    }

    public IWorkflowParameter getParameter(int index) {
        return (IWorkflowParameter)getObject(index);
    }

    /** Automatically add a parameter */
    public void add(String blockName, String parameterName, String parameterValue) {
        IWorkflowParameter parameter = new InkspotWorkflowParameter();
        parameter.setBlockName(blockName);
        parameter.setName(parameterName);
        parameter.setValue(parameterValue);
        add(parameter);
    }

    /** Automatically add a parameter */
    public void add(String parameterId, String parameterValue){
        IWorkflowParameter parameter = new InkspotWorkflowParameter();
        int index = parameterId.indexOf(".");
        if(index!=-1){
            parameter.setBlockName(parameterId.substring(0, index));
            parameter.setName(parameterId.substring(index+1, parameterId.length()));
        } else {
            parameter.setBlockName("InvalidParameterID");
            parameter.setName("InvalidParameterID");

        }
        parameter.setValue(parameterValue);
        add(parameter);
    }
}