/*
 * DataProcessorServiceFetcher.java
 */

package com.connexience.server.workflow.service;

/**
 * This interface defines an object that can supply DataProcessorServiceDefinition
 * objects from a remote machine. In the editor, this is the DragAndDrop handler,
 * in the runtime it is the WorkflowInvocation which is responsible for being
 * able to supply service definition objects when the block executes.
 * @author nhgh
 */
public interface DataProcessorServiceFetcher {
    /** Get the latest version of a service definition by ID */
    public DataProcessorServiceDefinition getServiceDefinition(String serviceId) throws DataProcessorException;

    /** Get a specific version of a service definition */
    public DataProcessorServiceDefinition getServiceDefinition(String serviceId, String versionId) throws DataProcessorException;
}
