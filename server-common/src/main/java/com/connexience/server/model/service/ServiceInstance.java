/*
 * ServiceInstance.java
 */

package com.connexience.server.model.service;
import java.io.*;

/**
 * This class represents a record of a service instance that is installed
 * on a service host.
 * @author hugo
 */
public class ServiceInstance implements Serializable {
    /** Database ID */
    private long id;

    /** Is the instance running. This is only an indication - it depends on
    * whether the service host managed to set the status correctly. */
    private boolean running;

    /** RMI Server port */
    private int rmiPort;

    /** Service name */
    private String name;

    /** IP of the service host that is running this process */
    private String hostId;

    /** Registered IP address */
    private String registeredIpAddress;

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRmiPort() {
        return rmiPort;
    }

    public void setRmiPort(int rmiPort) {
        this.rmiPort = rmiPort;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setRegisteredIpAddress(String registeredIpAddress) {
        this.registeredIpAddress = registeredIpAddress;
    }

    public String getRegisteredIpAddress() {
        return registeredIpAddress;
    }

    
}