/*
 * WorkflowEngineRecord.java
 */
package com.connexience.server.model.workflow.control;

import com.connexience.server.util.RegistryUtil;
import java.io.*;
import java.util.*;

/**
 * This class represents a workflow engine that has been seen at least once by
 * the system. It can either point to a direct IP address or to a workflow engine
 * ID if it is located within the sevice host
 * @author hugo
 */
public class WorkflowEngineRecord implements Serializable {
    /** Database ID of the engine */
    private long id;
    
    /** ID of the engine */
    private String engineId;
    
    public WorkflowEngineRecord() {
    }

    public void setEngineId(String engineId) {
        this.engineId = engineId;
    }

    public String getEngineId() {
        return engineId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public boolean isStandalone(){
        if(engineId!=null){
            if(engineId.startsWith("IP:")){
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
    
    public String getIPAddress(){
        // Engine is running standalone
        int index = engineId.indexOf(":");
        if(index!=-1){
            return engineId.substring(index + 1);
        } else {
            return "UnknownIP";
        }        
    }
}
