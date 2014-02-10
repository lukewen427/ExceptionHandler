/*
 * WorkflowUtils.java
 */
package com.connexience.server.util;

import com.connexience.server.ConnexienceException;
import com.connexience.server.ejb.util.EJBLocator;
import com.connexience.server.model.security.*;
import com.connexience.server.model.service.*;
import com.connexience.server.model.workflow.*;
import com.connexience.server.model.workflow.control.*;

import java.util.*;

/**
 * Utilites to deal with workflows
 * @author hugo
 */
public class WorkflowUtils {
    public static IWorkflowEngineControl connectToWorkflowEngine(Ticket ticket, WorkflowEngineRecord engine) throws Exception {
        return connectToWorkflowEngine(ticket, engine.getEngineId());
    }
    
    public static IWorkflowEngineControl connectToWorkflowEngine(Ticket ticket, WorkflowInvocationFolder invocation) throws Exception {
        return connectToWorkflowEngine(ticket, invocation.getEngineId());
    }
    
    /** Connect to a remote invocation. This method checks the host id property of the invocation. If it starts with IP: it is a direct connection. Otherwise the
     * ID will be looked up in the service database */
    public static IWorkflowEngineControl connectToWorkflowEngine(Ticket ticket, String engineId) throws Exception {
        if(engineId!=null){
            if(engineId.startsWith("IP:")){
                // Engine is running standalone
                int index = engineId.indexOf(":");
                if(index!=-1){
                    String engineIp = engineId.substring(index + 1);
                    IWorkflowEngine engine = (IWorkflowEngine)RegistryUtil.lookup(engineIp, "CloudWorkflowEngine");
                    return engine.openControlConnection();
                } else {
                    throw new Exception("Error parsing workflow engine IP address from engine ID");
                }
            } else {
                // Engine is running on a service host
                ServiceHostMachine host = EJBLocator.lookupServiceBean().getHostById(ticket, engineId);
                ServiceInstance svc = EJBLocator.lookupServiceBean().findRunningServiceInstanceOnHostId("WorkflowEngineService", engineId);
                IWorkflowEngine engine = (IWorkflowEngine)RegistryUtil.lookup(host.getIpAddress(), svc.getRmiPort(), "CloudWorkflowEngine");
                return engine.openControlConnection(ticket);
            }

        } else {
            throw new ConnexienceException("Cannot identify machine hosting workflow");
        }        
    }    
}