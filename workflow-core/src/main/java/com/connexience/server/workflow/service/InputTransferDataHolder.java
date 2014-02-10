/*
 * InputTransferDataHolder.java
 */

package com.connexience.server.workflow.service;

import org.pipeline.core.drawing.*;
import com.connexience.server.workflow.engine.*;
import java.io.*;

/**
 * This class holds a TransferData object for an input data set
 * @author nhgh
 */
public class InputTransferDataHolder {
    /** Data transfer object */
    private TransferData dataObject;

    /** Human readable name of the transfer data type */
    private String transferTypeName = "";

    /** Data source client that is used to load the data */
    private DataProcessorDataSource sourceClient;

    /** Stream being used to read data */
    private InputStream stream;

    /** Port that the transfer data is connected to */
    private String linkedPort;

    /** ID of the block that the transfer data is connected to */
    private String linkedContext;

    /** Workflow invocation ID */
    private String invocationId;

    /** Is this a streaming connection */
    private boolean streamingConnection = false;

    /** Chunk size for streaming */
    private int chunkSize = 1000;

    /** Create an input transfer data holder */
    public InputTransferDataHolder(String transferTypeName, String linkedPort, String linkedContext, DataProcessorDataSource sourceClient, String invocationId, boolean streamingConnection, int chunkSize) throws DataProcessorException {
        this.sourceClient = sourceClient;
        this.transferTypeName = transferTypeName;
        this.linkedContext = linkedContext;
        this.linkedPort = linkedPort;
        this.invocationId = invocationId;
        this.streamingConnection = streamingConnection;
        this.chunkSize = chunkSize;
        createTransferDataObject();
    }

    /** Create the transfer object */
    private void createTransferDataObject() throws DataProcessorException {
        try {
            boolean sizeKnown = false;
            long size = 0;
            if(sourceClient.allowsFileSystemAccess()){
                size = sourceClient.getInputDataLength(invocationId, linkedContext, linkedPort);
                sizeKnown = true;
            }

            stream = sourceClient.getInputDataStream(invocationId, linkedContext, linkedPort);
            dataObject = DataTypes.instantiateTransferData(transferTypeName, stream);

            // Set up if streamable transfer data
            if(dataObject instanceof StreamableTransferData){
                ((StreamableTransferData)dataObject).setStreaming(streamingConnection);
                ((StreamableTransferData)dataObject).setChunkSize(chunkSize);
                ((StreamableTransferData)dataObject).setTotalBytesKnown(sizeKnown);
                ((StreamableTransferData)dataObject).setTotalBytesToRead(size);
            }
            
        } catch (Exception e){
            throw new DataProcessorException("Cannot instantiate input data object: " + e.getMessage());
        } finally {
            // Stream should be closed if this is not a streaming connection
            if(!(dataObject instanceof StreamableTransferData) && stream!=null){
                try {
                    stream.close();
                } catch (Exception e){
                    System.out.println("Error closing transfer data stream: " + e.getMessage());
                }
            }
        }
    }

    /** Has the data reader finished reading everything */
    public boolean isFinished(){
        if(dataObject instanceof StreamableTransferData){
            return ((StreamableTransferData)dataObject).isFinished();
        } else {
            // True if not a streamable connection
            return true;
        }
    }
    
    /** Close the stream if this is a streamable object */
    public void close() throws DataProcessorException {
        if(dataObject instanceof StreamableTransferData){
            if(stream!=null) {
                try {
                     stream.close();
                } catch (Exception e){
                    throw new DataProcessorException("Cannot close stream for input linked to: " + linkedPort + ": " + e.getMessage());
                }
            }

        }
    }
    /** Get the transfer data object */
    public TransferData getTransferData(){
        return dataObject;
    }
}