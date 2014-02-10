/*
 * DataProcessorService.java
 */
package com.connexience.server.workflow.service;

import com.connexience.server.workflow.engine.*;
import com.connexience.server.workflow.engine.datatypes.*;
import com.connexience.server.workflow.api.*;
import com.connexience.server.workflow.xmlstorage.*;
import com.connexience.server.api.*;
import com.connexience.server.api.impl.InkspotDocument;
import com.connexience.server.util.*;
import com.connexience.server.model.security.*;
import com.connexience.server.workflow.service.clients.DataProcessorDataSourceFileClient;
import org.pipeline.core.data.io.CSVDataExporter;
import org.pipeline.core.data.io.DataExportException;
import org.pipeline.core.drawing.*;
import org.pipeline.core.xmlstorage.*;
import org.pipeline.core.data.*;
import java.io.*;
import java.util.*;
import org.pipeline.core.xmlstorage.io.XmlDataStoreReadWriter;
import org.pipeline.core.xmlstorage.io.XmlDataStoreStreamReader;
import org.pipeline.core.xmlstorage.io.XmlFileIO;

/**
 * This class defines a data processor that takes in one or more TransferData
 * objects and returns one or more result TransferData objects. It should be implemented
 * by class that provide data processing facilities.
 * @author hugo
 */
public abstract class DataProcessorService {

    /** CallMessage thread local object */
    private DataProcessorCallMessage callMessage;

    /** Response message destination */
    private DataProcessorResponseMessageHandler responder;

    /** Captured output text */
    private String outputData = "";

    /** Name of the XML resource if there is one */
    private String xmlDefinitionResource = "";

    /** Registered name in the service endpoint */
    private String registeredName = "";

    /** Maximum length of output data to capture */
    private int outputDataLimit = 4096;

    /** Streaming chunk size */
    private int chunkSize = 1000;

    /** Input data transfer objects */
    private Hashtable<String, InputTransferDataHolder> inputDataTransferObjects = new Hashtable<String, InputTransferDataHolder>();

    /** Output data transfer objects */
    private Hashtable<String, OutputTransferDataHolder> outputDataTranferObjects = new Hashtable<String, OutputTransferDataHolder>();

    /** Streamed input if there is one */
    private Vector<InputTransferDataHolder> streamedInputs = new Vector<InputTransferDataHolder>();

    /** Has this service been called yet. This is implied when the isStreamingFinished method is called */
    private boolean serviceCalled = false;

    /** API Link to access the server */
    private API apiLink;

    /** Workflow API link */
    private WorkflowAPI wfApi = null;

    /** Workflow lock created for any sub-workflows */
    private WorkflowLockWrapper lock = null;

    /** Has the lock been used */
    private boolean lockUsed = false;
    
    /** Global properties that workflow services can read and write to */
    private XmlDataStore globalProperties = new XmlDataStore();
    
    /** Set this service to use a streaming chunk size */
    public void streamInChunksOf(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    /** Set the workflow api link */
    public void setWorkflowApi(WorkflowAPI wfAPi) {
        this.wfApi = wfAPi;
    }

    /** Get the API link */
    public API getApiLink() {
        return apiLink;
    }

    /** Set the API link */
    public void setApiLink(API apiLink) {
        this.apiLink = apiLink;
    }

    /** Does this processor contain a streamed input */
    public boolean containsStreamedInput() {
        if (streamedInputs.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /** Get the call properties */
    public XmlDataStore getProperties() {
        return getCallMessage().getProperties();
    }

    /** Get the call properties. This method is provided to help source
     * code compatibility with the existing blocks */
    public XmlDataStore getEditableProperties() {
        return getCallMessage().getProperties();
    }

    /** Set the the command output data */
    public void setCommandOutputData(String outputData) {
        this.outputData = outputData;
    }

    /** Get the command output data */
    public String getCommandOutputData() {
        return outputData;
    }

    /** Get the output data limit */
    public int getOutputDataLimit() {
        return outputDataLimit;
    }

    /** Set the output data limit */
    public void setOutputDataLimit(int outputDataLimit) {
        this.outputDataLimit = outputDataLimit;
    }

    /** Set the registered name */
    public void setRegisteredName(String registeredName) {
        this.registeredName = registeredName;
    }

    /** Get the registered name */
    public String getRegisteredName() {
        return registeredName;
    }

    /** Set the call message */
    public void setCallMessage(DataProcessorCallMessage callMessage) {
        this.callMessage = callMessage;
    }

    /** Get the call message */
    public DataProcessorCallMessage getCallMessage() {
        return callMessage;
    }

    /** Set the response message destination */
    public void setResponseDestination(DataProcessorResponseMessageHandler responder) {
        this.responder = responder;
    }

    /** Get the response message destination */
    public DataProcessorResponseMessageHandler getResponseDestination() {
        return responder;
    }

    /** Get the security ticket */
    public Ticket getTicket() {
        if (callMessage != null) {
            try {
                return (Ticket) SerializationUtils.deserialize(getCallMessage().getTicketData());
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /** Create a response message. Sends an OK message */
    public void sendResponseMessage() throws DataProcessorException {
        DataProcessorCallMessage call = getCallMessage();
        DataProcessorResponseMessage message = new DataProcessorResponseMessage(call.getInvocationId(), call.getContextId());
        message.setStatus(DataProcessorResponseMessage.SERVICE_EXECUTION_OK);
        message.setStatusMessage("");
        message.setCommandOutput(getCommandOutputData());

        // Set lock status
        if(lockUsed && lock!=null){
            message.setWaitingForLock(true);
            message.setLockId(lock.getId());
        } else {
            message.setWaitingForLock(false);
            message.setLockId(0);
        }

        getResponseDestination().sendResponseMessage(message);
    }

    /** Send a response message containing an error */
    public void sendErrorResponseMessage(String statusMessage) {
        try {
            DataProcessorCallMessage call = getCallMessage();
            DataProcessorResponseMessage message = new DataProcessorResponseMessage(call.getInvocationId(), call.getContextId());
            message.setStatus(DataProcessorResponseMessage.SERVICE_EXECUTION_ERROR);
            message.setStatusMessage(statusMessage);
            getResponseDestination().sendResponseMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Create a data source client for a call message */
    private DataProcessorDataSource createSourceClient(DataProcessorCallMessage message) throws DataProcessorException {
        // Create the correct type of data client
        if (message.getDataTransferType().equals(DataProcessorDataSource.FILE_DATA_SOURCE)) {
            return new DataProcessorDataSourceFileClient(message.getStorageUrl());

        } else if (message.getDataTransferType().equals(DataProcessorDataSource.WEB_DATA_SOURCE)) {
            throw new DataProcessorException("Web client not implemented yet");
        } else {
            throw new DataProcessorException("Could not create data access client");
        }
    }

    /** Get an input data transfer object */
    public TransferData getInputData(String inputName) throws DataProcessorException {
        if (inputDataTransferObjects.containsKey(inputName)) {
            return inputDataTransferObjects.get(inputName).getTransferData();
        } else {
            throw new DataProcessorException("Cannot find input: " + inputName);
        }
    }

    /** Send some output data to one of the return connections */
    public void setOutputData(String outputName, TransferData data) throws DataProcessorException {
        if (!(data instanceof StreamableTransferData)) {
            if (outputDataTranferObjects.containsKey(outputName)) {
                outputDataTranferObjects.get(outputName).setDataObject(data);
            } else {
              if(getEditableProperties().booleanValue("ErrorsForNonExistentPorts", true))
              {
                throw new DataProcessorException("Cannot find output: " + outputName);
              }
            }
        } else {
            if (getEditableProperties().booleanValue("ErrorsForNonExistentPorts", true)) {
                throw new DataProcessorException("Cannot perform setOutputData on a streaming connection");
            }
        }
    }

    /** Pass a data set to an output */
    public void setOutputDataSet(String outputName, Data data) throws DataProcessorException {
        if (outputDataTranferObjects.containsKey(outputName)) {
            OutputTransferDataHolder holder = outputDataTranferObjects.get(outputName);
            if (holder.isStreamingObject() && holder.getTransferData() instanceof DataWrapper) {
                try {
                    ((DataWrapper) holder.getTransferData()).writeChunk(data);
                } catch (Exception e) {
                  if(getEditableProperties().booleanValue("ErrorsForNonExistentPorts", true))
                  {
                    throw new DataProcessorException("Error writing data block to output: " + outputName + ": " + e.getMessage());
                  }
                }
            }
        } else {
            if (getEditableProperties().booleanValue("ErrorsForNonExistentPorts", true)) {
                throw new DataProcessorException("Cannot find output: " + outputName);
            }
        }
    }

    /** Get some input data as a Data set object */
    public Data getInputDataSet(String inputName) throws DataProcessorException {
        TransferData holder = getInputData(inputName);
        if (holder instanceof DataWrapper) {
            DataWrapper dataWrapper = (DataWrapper) holder;
            try {
                // Check streaming mode
                if (getCallMessage().getStreamMode().equals(DataProcessorServiceDefinition.STREAM_SQEUENTIAL_MODE)) {
                    // Check to see that the other inputs defined before this one have finished
                    boolean okToStream = true;
                    InputTransferDataHolder inp = inputDataTransferObjects.get(inputName);
                    int pos = streamedInputs.indexOf(inp);
                    if (pos > 0) {
                        // Not the first input, check that everything up to this point
                        // is ok to stream
                        for (int i = 0; i < pos; i++) {
                            if (!streamedInputs.get(i).isFinished()) {
                                okToStream = false;
                            }
                        }
                    }

                    if (okToStream) {
                        Data data = dataWrapper.readChunk();
                        return data;
                    } else {
                        // Not ready to stream - return null
                        return null;

                    }

                } else {
                    // Send data as is
                    Data data = dataWrapper.readChunk();
                    return data;
                }

            } catch (Exception e) {
                throw new DataProcessorException("Error reading data block: " + e.getMessage());
            }
        } else {
            throw new DataProcessorException("Input: " + inputName + " is not a data connection");
        }
    }

    /** Get an empty data set for an input with the correct layout of rows */
    public Data getEmptyInputDataSet(String inputName) throws DataProcessorException {
        if (inputDataTransferObjects.containsKey(inputName)) {
            InputTransferDataHolder holder = inputDataTransferObjects.get(inputName);
            if (holder.getTransferData() instanceof DataWrapper) {
                try {
                    return ((DataWrapper) holder.getTransferData()).getEmptyData();
                } catch (DrawingException de) {
                    throw new DataProcessorException("Error creating empty data set: " + de.getMessage());
                }
            } else {
                throw new DataProcessorException("Cannot create empty data from a non-data connection");
            }
        } else {
            throw new DataProcessorException("Input: " + inputName + " does not exist");
        }
    }

    /** Set the resource name of the Xml service definition */
    public void setXmlDefinitionResource(String xmlDefinitionResource) {
        this.xmlDefinitionResource = xmlDefinitionResource;
    }

    /** Get the service definition object */
    public String getServiceXml() {
        try {
            InputStream stream = getClass().getResourceAsStream(xmlDefinitionResource);
            ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = stream.read(buffer)) != -1) {
                bufferStream.write(buffer, 0, len);
            }

            return new String(bufferStream.toByteArray());

        } catch (Exception e) {
            System.out.println("Cannot locate service definition: " + e.getMessage());
            return null;
        }
    }

    /** Get the temporary storage directory of the data source */
    public File getWorkingDirectory() throws DataProcessorException {
        DataProcessorCallMessage message = getCallMessage();
        DataProcessorDataSource source = createSourceClient(message);
        if (source.allowsFileSystemAccess()) {
            return new File(source.getStorageDirectory(message.getInvocationId()));
        } else {
            throw new DataProcessorException("Data transport does not allow file system access");
        }
    }

    /** Close all of the service IOs */
    public void closeIOs() {
        Enumeration<InputTransferDataHolder> inputs = inputDataTransferObjects.elements();
        InputTransferDataHolder input;
        OutputTransferDataHolder output;

        while (inputs.hasMoreElements()) {
            input = inputs.nextElement();
            try {
                input.close();
            } catch (Exception e) {
                System.out.println("Could not close input: " + input);
            }
        }

        Enumeration<OutputTransferDataHolder> outputs = outputDataTranferObjects.elements();
        while (outputs.hasMoreElements()) {
            output = outputs.nextElement();
            try {
                output.close();
            } catch (Exception e) {
                System.out.println("Could not close output: " + output);
            }
        }
    }

    /** Return the total number of bytes to be streamed for inputs
     * that support this information */
    public long getTotalBytesToStream() {
        if (streamedInputs.size() > 0) {
            InputTransferDataHolder holder;
            StreamableTransferData transferData;
            long total = 0;

            for (int i = 0; i < streamedInputs.size(); i++) {
                holder = streamedInputs.get(i);
                if (holder.getTransferData() instanceof StreamableTransferData) {
                    transferData = (StreamableTransferData) holder.getTransferData();
                    if (transferData.isTotalBytesKnown()) {
                        total = total + transferData.getTotalBytesToRead();

                    }
                }
            }
            return total;
        } else {
            return 0;
        }
    }

    /** Return the total number of bytes streamed */
    public long getTotalBytesStreamed() {
        if (streamedInputs.size() > 0) {
            InputTransferDataHolder holder;
            StreamableTransferData transferData;
            long total = 0;

            for (int i = 0; i < streamedInputs.size(); i++) {
                holder = streamedInputs.get(i);
                if (holder.getTransferData() instanceof StreamableTransferData) {
                    transferData = (StreamableTransferData) holder.getTransferData();
                    if (transferData.isTotalBytesKnown()) {
                        total = total + transferData.getActualBytesRead();
                    }
                }
            }
            return total;
        } else {
            return 0;
        }
    }
    
    /** Get the global properties object */
    public XmlDataStore getGlobalProperties(){
        return globalProperties;
    }
    
    /** Load the global properties into this block */
    public void loadGlobalProperties() throws DataProcessorException {
        File dir = getWorkingDirectory();
        File globalPropertiesFile = new File(dir, "_globalProperties.xml");
        if(globalPropertiesFile.exists()){
            try {
                XmlFileIO reader = new XmlFileIO(globalPropertiesFile);
                globalProperties = reader.readFile();
            } catch (Exception e){
                throw new DataProcessorException("Error loading global properties: " + e.getMessage(), e);
            }
        } else {
            globalProperties = new XmlDataStore("GlobalProperties");
        }
    }
    
    public void saveGlobalProperties() throws DataProcessorException {
        File dir = getWorkingDirectory();
        File globalPropertiesFile = new File(dir, "_globalProperties.xml");
        try {
            XmlFileIO writer = new XmlFileIO(globalProperties);
            writer.writeFile(globalPropertiesFile);
        } catch (Exception e){
            throw new DataProcessorException("Error loading global properties: " + e.getMessage(), e);
        }
    }

    /** Initialise all of the inputs and outputs. This opens the files
     * ready for reading / writing and sets any chunking requirements */
    public void initialiseIOs() throws DataProcessorException {
        // Create a data source client
        inputDataTransferObjects.clear();
        outputDataTranferObjects.clear();
        DataProcessorCallMessage message = getCallMessage();
        DataProcessorDataSource source = createSourceClient(message);
        streamedInputs.clear();

        // Create the inputs
        String dataType;
        String inputName;
        String outputName;
        int sourceIndex;

        // Create a data store to contain the overridden properties
        XmlDataStore propertyOverrides = new XmlDataStore();
        
        for (int i = 0; i < message.getDataSources().length; i++) {
            inputName = message.getDataSources()[i];
            dataType = message.getDataSourceType(inputName);

            if (dataType == null) {
                throw new DataProcessorException("Cannot locate data type for input: " + inputName);
            }

            // Find the block that supplies data to this input
            sourceIndex = message.getDataSourceIndex(inputName);
            if (sourceIndex != -1) {
                String linkedPort = message.getDataSourceConnections()[sourceIndex];
                String linkedContext = message.getDataSourceConnectionContexts()[sourceIndex];

                // Create the correct type of data client
                source = createSourceClient(message);

                if (source != null) {
                    InputTransferDataHolder holder;

                    // Is this the streaming connection
                    if (message.getDataSourceMode(inputName).equals(DataProcessorIODefinition.STREAMING_CONNECTION)) {
                        // Streaming input
                        holder = new InputTransferDataHolder(dataType, linkedPort, linkedContext, source, message.getInvocationId(), true, chunkSize);
                        streamedInputs.add(holder);
                    } else {
                        // Non-streaming input
                        holder = new InputTransferDataHolder(dataType, linkedPort, linkedContext, source, message.getInvocationId(), false, chunkSize);
                        
                        // If this is a properties input object, copy the properties into the property overrides store
                        if(holder.getTransferData() instanceof PropertiesWrapper){
                            try {
                                PropertiesWrapper props = (PropertiesWrapper)holder.getTransferData();
                                propertyOverrides.copyProperties(props.properties());
                            } catch (Exception e){
                                throw new DataProcessorException("Could not load properties from input: " + inputName, e);
                            }
                        }
                    }

                    inputDataTransferObjects.put(inputName, holder);
                } else {
                    throw new DataProcessorException("Could not create data access client");
                }
            } else {
                throw new DataProcessorException("Cannot locate input: " + inputName);
                // NOTES>>>
                // Currently creating input objects ready to be read.
                // Need to have a streaming definition in the message / service definition
                // For non-streaming inputs read immediately on instantiation
                // Need to check closing etc when there are exceptions
                // Chunk size etc set in message + system property for when to switch into chunking mode

            }
        }

        // Override / add any properties contained in the property overrides
        if(propertyOverrides.size()>0){
            try {
                message.getProperties().copyProperties(propertyOverrides);
            } catch (Exception e){
                throw new DataProcessorException("Error copying properties into message properties: " + e.getMessage(), e);
            }
        }
        
        // Create the outputs
        for (int i = 0; i < message.getDataOutputs().length; i++) {
            outputName = message.getDataOutputs()[i];
            dataType = message.getDataOutputType(outputName);

            if (dataType == null) {
                throw new DataProcessorException("Cannot locate data type for output: " + outputName);
            }

            source = createSourceClient(message);
            if (source != null) {
                OutputTransferDataHolder holder = new OutputTransferDataHolder(dataType, outputName, message.getContextId(), source, message.getInvocationId());
                outputDataTranferObjects.put(outputName, holder);
            } else {
                throw new DataProcessorException("Could not create output data access client");
            }
        }
    }

    /** Is the streaming finished */
    public boolean isStreamingFinished() {
        if (streamedInputs.size() > 0) {
            for (int i = 0; i < streamedInputs.size(); i++) {
                // An input is still running, so return false
                if (!streamedInputs.get(i).isFinished()) {
                    return false;
                }
            }
            return true;

        } else {
            if (inputDataTransferObjects.size() > 0) {
                // Some inputs, although not streaming. Allow service to be called once
                if (serviceCalled) {
                    return true;
                } else {
                    serviceCalled = true;
                    return false;
                }

            } else {
                // If there are no inputs, allow the service to be called one time only
                if (serviceCalled) {
                    return true;
                } else {
                    // First time, always return false
                    serviceCalled = true;
                    return false;
                }
            }
        }

    }

    /** Create a temporary file in the invocation directory. Name is incorporated into the generated
     * file in an unspecified way. */
    public File createTempFile(String name) throws Exception {
        File workingDir = getWorkingDirectory();
        File tempFile = new File(workingDir, callMessage.getContextId() + "-" + name + ".tmp");
        return tempFile;
    }

    /** Execute the service. This will be called each time a chunk of data is processed.
     * A call to isStreaming() will determine if the service is actually streaming data. */
    public abstract void execute() throws Exception;

    /** This method is call when a service is about to be started. It is called once
     * regardless of whether or not the service is streaming data. */
    public void executionAboutToStart() throws Exception {
    }

    /** All of the data has been passed through the service. Services that need to
     * see all of the data should now set their outputs */
    public void allDataProcessed() throws Exception {

    }

    /** Pre-close tidyup. This method is called before the block process exists. It is used to setup any locks etc */
    public void preCloseTidyup() throws Exception {
        // If there is a lock and it hasn't been used, delete it
        if(wfApi!=null && lock!=null){
            if(lockUsed==false){
                wfApi.removeWorkflowLock(lock.getId());
            } else {
                wfApi.setWorkflowLockStatus(lock.getId(), WorkflowLockWrapper.LOCK_WAITING);
            }
        }
    }

    /** Create a workflow lock for this service */
    public WorkflowLockWrapper createWorkflowLock() throws Exception {
        if (wfApi != null) {
            if (lock == null) {
                lock = wfApi.createWorkflowLock(getCallMessage().getInvocationId(), getCallMessage().getContextId());
                lockUsed = false;
                return lock;
            } else {
                throw new Exception("Services can only create a single workflow lock");
            }
        } else {
            throw new Exception("No API to create lock with");
        }
    }

    /** Execute a workflow and attach it to a lock */
    public IWorkflowInvocation executeWorkflowWithLock(IWorkflow workflow, IWorkflowParameterList params, WorkflowLockWrapper wfLock, String folderName) throws Exception {
        if (wfApi != null) {
            if (this.lock != null) {
                if (wfLock.getId() == this.lock.getId()) {
                    IWorkflowInvocation invocation = wfApi.executeWorkflow(workflow, params, lock.getId(), folderName);
                    lockUsed = true;
                    return invocation;
                } else {
                    throw new Exception("Wrong lock");
                }
            } else {
                throw new Exception("No lock created");
            }
        } else {
            throw new Exception("No API to execute workflow with");
        }
    }

    /** Execute a workflow and attach it to a lock */
    public IWorkflowInvocation executeWorkflow(IWorkflow workflow, IWorkflowParameterList params, String folderName) throws Exception {
        if (wfApi != null) {
            IWorkflowInvocation invocation = wfApi.executeWorkflow(workflow, params, -1, folderName);
            return invocation;
        } else {
            throw new Exception("No API to execute workflow with");
        }
    }
    
    public void persistDataToServer() throws Exception
    {
      try
      {
        if (apiLink != null)
        {
          IWorkflowInvocation inv = apiLink.getWorkflowInvocation(callMessage.getInvocationId());

          for (String outputDataName : outputDataTranferObjects.keySet())
          {
            OutputTransferDataHolder transferHolder = outputDataTranferObjects.get(outputDataName);

            if (transferHolder.getTransferTypeName().equals("file-wrapper"))
            {
              //files are stored as a list of files in a file
              String transferFilename = outputDataName + "-" + callMessage.getContextId() + ".dat";
              BufferedReader input = new BufferedReader(new FileReader(new File(transferFilename)));
              try
              {
                String line;

                //read each file referenced and upload
                while ((line = input.readLine()) != null)
                {
                  File dataFile = new File(line);
                  IDocument doc = new InkspotDocument();
                  doc.setName(dataFile.getName());
                  doc.setContainerId(inv.getId());
                  doc.setDescription("Data persisted from workflow invocation");
                  doc = apiLink.saveDocument(inv, doc);

                  apiLink.upload(doc, new FileInputStream(dataFile));
                }
              }
              finally
              {
                input.close();
              }
            }
            else if (transferHolder.getTransferTypeName().equals("data-wrapper"))
            {
                //TODO: Fix as data is null at present - why?!
//              DataWrapper wrapper = (DataWrapper) transfer.getTransferData();
//              Data data = wrapper.readChunk();
//              CSVDataExporter exporter = new CSVDataExporter(data);
//              String filename = outputDataName + "_" + callMessage.getContextId() + "_transfer.csv";
//              exporter.writeFile(new File(filename));
//
//              IDocument doc = new InkspotDocument();
//              doc.setName(filename);
//              doc.setContainerId(inv.getId());
//              doc.setDescription("Data persisted from workflow invocation");
//              doc = apiLink.saveDocument(inv, doc);
//
//              apiLink.upload(doc, new FileInputStream(new File(filename)));
            }
          }
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
        throw e;
      }
    }
    
    private Collection<File> existingFiles = new TreeSet<File>(new Comparator<File>() {

        @Override
        public int compare(File file, File file1) {
            return file.getName().compareTo(file1.getName());
        }
    });

    /**
     *  Get a list of new files in the directory since the last snapshot.  Does not include files that have been modified, only files that have been added.
     * @return A List of file objects that have been added to the directory since the last call to snapshot
     * @throws DataProcessorException if the workflow is executing on a node that does not allow file system access.
     */
    public Collection<File> snapshot() throws DataProcessorException {
        File workingDir = getWorkingDirectory();
        Collection<File> newFiles = new TreeSet<File>();

        File[] children = workingDir.listFiles();
        if (children != null) {
            for (File child : children) {
                if (!existingFiles.contains(child)) {
                    newFiles.add(child);
                    existingFiles.add(child);
                }
            }
        }

        return newFiles;
    }
}
