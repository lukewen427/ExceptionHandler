/*
 * ServiceHostShellList.java
 */
package com.connexience.server.rmi;
import com.connexience.server.*;
import com.connexience.server.ejb.util.EJBLocator;
import com.connexience.server.model.security.*;
import com.connexience.server.model.service.ServiceHostMachine;
import com.connexience.server.util.RegistryUtil;

import java.util.*;
import javax.servlet.http.HttpSession;

/**
 * This class maintains a list of open service host shells
 * @author hugo
 */
public class ServiceHostShellList implements Runnable {
    private HashMap<String,IServiceHostShell> clients = new HashMap<String, IServiceHostShell>();
    private Thread checkThread;

    public ServiceHostShellList() {
        checkThread = new Thread(this);
        checkThread.setDaemon(true);
        checkThread.start();
    }
        
    /** Open a debug client to a workflow invocation */
    public IServiceHostShell getShellConnection(Ticket ticket, HttpSession session, String hostId) throws ConnexienceException {
        IServiceHostShell shell = null;
        
        
        if(!clients.containsKey(session.getId() + "-" + hostId)){

            ServiceHostMachine hostMachine = null;
            try {
                hostMachine = EJBLocator.lookupServiceBean().getHostById(ticket, hostId);
            } catch (Exception e){
                throw new ConnexienceException("Error connecting to workflow engine: " + e.getMessage(), e);
            }

            if(hostMachine==null){
                throw new ConnexienceException("No such machine");
            }

            IServiceHost host = null;
            try {
                host = (IServiceHost)RegistryUtil.lookup(hostMachine.getIpAddress(), "ServiceHost");
            } catch (Exception e){
                throw new ConnexienceException("Error opening connection to service host machine", e);
            }
            
            IServiceHostConnection connection = null;
            try {
                connection = host.openConnection(null, null);
            } catch (Exception e){
                throw new ConnexienceException("Error opening connection instance", e);
            }
            
            try {
                shell = connection.openShell(ticket);
            } catch (Exception e){
                throw new ConnexienceException("Error opening shell connection: " + e.getMessage(), e);
            }
            
            if(shell!=null){
                clients.put(session.getId() + "-" + hostId, shell);
            } else {
                throw new ConnexienceException("Server did not return a valid shell");
            }

        } else {
            shell = clients.get(session.getId() + "-" + hostId);
        }
        
        if(shell!=null){
            return shell;
        } else {
            throw new ConnexienceException("Cannot obtain debugger");
        }
    }
    
    
    /** Remove a debugger */
    public void removeShellConnection(Ticket ticket, HttpSession session, String hostId) throws ConnexienceException {
        if(clients.containsKey(session.getId() + "-" + hostId)){
            IServiceHostShell shell = null;
            try {
                shell = clients.get(session.getId() + "-" + hostId);
                shell.close();
            } catch (Exception e){
                throw new ConnexienceException("Error closing debug connection: " + e.getMessage(), e);
            } finally {
                shell = null;
                clients.remove(session.getId() + "-" + hostId);
            }
        }
    }    
    
    @Override
    public void run() {
        boolean run = true;
        while(run){
            Iterator<IServiceHostShell> i = clients.values().iterator();
            ArrayList<IServiceHostShell> clientsToRemove = new ArrayList<IServiceHostShell>();
            IServiceHostShell client;
            while(i.hasNext()){
                client = i.next();
                try {
                    if(!client.isRunning()){
                        clientsToRemove.add(client);
                    }
                } catch (Exception e){
                    clientsToRemove.add(client);
                }
            }
            
            for(IServiceHostShell c : clientsToRemove){
                clients.values().remove(c);
            }
            
            try {
                Thread.sleep(10000);
            } catch(InterruptedException e){
                run = false;
            }
        }        
    }
}