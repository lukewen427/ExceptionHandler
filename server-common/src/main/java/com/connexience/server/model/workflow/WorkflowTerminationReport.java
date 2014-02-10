/*
 * WorkflowTerminationReport.java
 */
package com.connexience.server.model.workflow;

import java.io.*;
import java.util.*;

/**
 * This object contains a report of the termination status when a workflow is killed.
 * When a set of related workflows are killed this object contains a tree structure
 * with details of whether or not the various workflows in the chain were killed
 * correctly. If not, then the user can take action and attempt to re-kill
 * the failed steps or delete the invocations.
 * @author hugo
 */
public class WorkflowTerminationReport implements Serializable {
    static final long serialVersionUID = -5437816691004555473L;
    
    private WorkflowTerminationItem topItem;
    
    public WorkflowTerminationReport() {
        topItem = new WorkflowTerminationItem();
    }

    public WorkflowTerminationReport(String id, String name) {
        WorkflowTerminationItem item = new WorkflowTerminationItem();
        item.setId(id);
        item.setName(name);
        this.topItem = item;
    }

    public WorkflowTerminationItem getTopItem() {
        return topItem;
    }

    public void setTopItem(WorkflowTerminationItem topItem) {
        this.topItem = topItem;
    }

    public List<WorkflowTerminationItem> getFlatList(){
        ArrayList<WorkflowTerminationItem> list = new ArrayList<WorkflowTerminationItem>();
        topItem.addToFlatList(list);
        return list;
    }
}