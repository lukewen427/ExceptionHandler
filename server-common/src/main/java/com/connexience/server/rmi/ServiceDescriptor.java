/*
 * ServiceDescriptor.java
 */

package com.connexience.server.rmi;

import java.io.*;

/**
 * This class provides a simple descriptor that describes a process on a service
 * host.
 * @author hugo
 */
public class ServiceDescriptor implements Serializable {
    /** Service Name */
    private String name;

    /** Name that the service process has been registered under in the RMI server */
    private String rmiName;

    /** Local RMI port that can be used to locate objects */
    private int rmiRegistryPort = -1;

    /** Is this service running */
    private boolean running;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRmiName() {
        return rmiName;
    }

    public void setRmiName(String rmiName) {
        this.rmiName = rmiName;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    public int getRmiRegistryPort() {
        return rmiRegistryPort;
    }

    public void setRmiRegistryPort(int rmiRegistryPort) {
        this.rmiRegistryPort = rmiRegistryPort;
    }
    
}