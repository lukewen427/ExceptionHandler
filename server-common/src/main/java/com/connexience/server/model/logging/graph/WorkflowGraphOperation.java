/*
 * WorkflowGraphOperation.java
 */

package com.connexience.server.model.logging.graph;

import java.io.*;
import java.util.*;

/**
 * This is the base operation class for events that are logged in the workflow
 * provenance graph.
 * @author hugo
 */
public class WorkflowGraphOperation extends GraphOperation  implements Serializable {

  /** Invocation ID of the workflow */
    private String invocationId;

  public String getInvocationId() {
        return invocationId;
    }

    public void setInvocationId(String invocationId) {
        this.invocationId = invocationId;
    }


}