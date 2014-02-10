/*
 * SerializedInvocationMessage.java
 */
package com.connexience.server.model.workflow;
import com.connexience.server.util.*;
import java.io.*;
/**
 * This class stores a serialized copy of a workflow invocation message so 
 * that it can be resent if the workflow fails.
 * @author hugo
 */
public class SerializedInvocationMessage implements Serializable {
    /** Database ID */
    private long id;
    
    /** ID of the invocation */
    private String invocationId;
    
    /** Byte array of serialized message */
    private byte[] serializedMessage;

    public SerializedInvocationMessage() {
    }

    /** Create from an invocation message */
    public SerializedInvocationMessage(WorkflowInvocationMessage msg) throws Exception {
        serializedMessage = SerializationUtils.serialize(msg);
        this.invocationId = msg.getInvocationId();
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInvocationId() {
        return invocationId;
    }

    public void setInvocationId(String invocationId) {
        this.invocationId = invocationId;
    }

    public byte[] getSerializedMessage() {
        return serializedMessage;
    }

    public void setSerializedMessage(byte[] serializedMessage) {
        this.serializedMessage = serializedMessage;
    }
    
    /** Deserialize and return the invocation message */
    public WorkflowInvocationMessage deserializeMessage() throws Exception {
        return (WorkflowInvocationMessage)SerializationUtils.deserialize(serializedMessage);
    }
}