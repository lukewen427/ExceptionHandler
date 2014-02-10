/*
 * DataProcessorCallMessage.java
 */

package com.connexience.server.workflow.service;

import org.pipeline.core.xmlstorage.*;

import org.w3c.dom.*;

import java.util.*;
import javax.xml.parsers.*;
import java.io.*;

/**
 * This class represents the message that is used to initiate a remote
 * data processor. It contains links to the source DataTransfer objects
 * and also the names of the required DataTransfer response objects.
 * @author hugo
 */
public class DataProcessorCallMessage implements Serializable, XmlStorable {
    /** Workflow invocation id */
    private String invocationId = "";

    /** Namespace for the XML document version of this message */
    public static final String XML_NAMESPACE = "http://www.connexience.com/namespace/dataprocessorcallmessage/v1";
    /** Invocation context id. 
     * This refers to the part of the workflow  that triggered the
     * call. Typically this is the GUID of the workflow block */
    private String contextId = "";

    /** List of modes for the data sources */
    private String[] dataSourceModes = new String[0];

    /** List of source data objects */
    private String[] dataSources = new String[0];
    
    /** List of types for data sources */
    private String[] dataSourceTypes = new String[0];
    
    /** List of the ports connected to the data sources. These are
     * needed to get the correct data from the server */
    private String[] dataSourceConnections = new String[0];
    
    /** List of the blocks connected to the data sources */
    private String[] dataSourceConnectionContexts = new String[0];
    
    /** List of required return objects */
    private String[] dataOutputs = new String[0];
    
    /** List of types for return data objects */
    private String[] dataOutputTypes = new String[0];
    
    /** Routine to call within the service */
    private String serviceRoutine = "";
    
    /** URL of the server */
    private String serviceUrl = "";
    
    /** URL of the data storage location */
    private String storageUrl = "";
    
    /** Type of data store */
    private String dataTransferType = DataProcessorDataSource.FILE_DATA_SOURCE;
    
    /** List of service property values */
    private XmlDataStore properties = new XmlDataStore();
    
    /** Security ticket data */
    private byte[] ticketData = new byte[0];
    
    /** Back end type for this service */
    private String serviceBackend = DataProcessorServiceDefinition.NO_SCRIPT;

    /** Streaming Mode */
    private String streamMode = DataProcessorServiceDefinition.STREAM_NO_STREAM_MODE;
    
    /** ID Of the script resource for this service if there is one */
    private String scriptId = "";

    /** Service ID for dynamic services */
    private String serviceId = "";

    /** Version ID for versioned service */
    private String versionId = "";

    /** Is the latest version to be used */
    private boolean useLatest = true;

    /** ID of the workflow document that contains this service */
    private String workflowId = null;

    /** ID of the workflow version */
    private String workflowVersionId = null;
    
    /** Maximum number of standard output data to buffer */
    private int maxStdOutBufferSize = 4096;

    /** Should the process freeze and wait for a debugger to attach */
    private boolean debugEnabled = false;

    /** Debugger port to start on */
    private int debugPort = 5005;

    /** Is the service being called idempotent */
    private boolean idempotent = true;

    /** Is the service being called deterministic*/
    private boolean deterministic = true;

    /** Is it ok to retry this message */
    private boolean okToRetry = false;
    
    /** Maximum number of permitted retry attempts */
    private int retryAttempts = 0;
    
    /** Create an empty response message */
    public DataProcessorCallMessage(){
        
    }
    
    /** Create with invocation and context ids */
    public DataProcessorCallMessage(String invocationId, String contextId){
        this.contextId = contextId;
        this.invocationId = invocationId;
    }

    /** Get the maximumum number of bytes to store from the standard output of the process used to execute this service */
    public int getStdOutBufferSize(){
        return maxStdOutBufferSize;
    }

    /** Set the maximumum number of bytes to store from the standard output of the process used to execute this service */
    public void setMaxStdOutBufferSize(int maxStdOutBufferSize) {
        if(maxStdOutBufferSize>=0){
            this.maxStdOutBufferSize = maxStdOutBufferSize;
        }
    }

    /** Get the streaming mode */
    public String getStreamMode(){
        return streamMode;
    }

    /** Set the streaming mode */
    public void setStreamMode(String streamMode){
        String lsm = streamMode.toLowerCase();
        if(lsm.equals(DataProcessorServiceDefinition.STREAM_NO_STREAM_MODE) || lsm.equals(DataProcessorServiceDefinition.STREAM_PARALLEL_MODE) || lsm.equals(DataProcessorServiceDefinition.STREAM_SQEUENTIAL_MODE)){
            this.streamMode = lsm;
        }
    }

    /** Get the ID of the workflow document */
    public String getWorkflowId(){
        return workflowId;
    }

    /** Set the ID of the workflow document */
    public void setWorkflowId(String workflowId){
        this.workflowId = workflowId;
    }

    /** Set the ID of the workflow version */
    public void setWorkflowVersionId(String workflowVersionId) {
        this.workflowVersionId = workflowVersionId;
    }

    /** Get the ID of the workflow version */
    public String getWorkflowVersionId() {
        return workflowVersionId;
    }

    /** Get the service ID. This is used for dynamically deployed services */
    public String getServiceId(){
        return serviceId;
    }

    /** Set the service ID. This is used for dynamically deployed services */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /** Does a dynamic version use the latest service version */
    public boolean usesLatestVersion(){
        return useLatest;
    }

    /** Set whether a dynamic version use the latest service version */
    public void setUsesLatest(boolean useLatest){
        this.useLatest = useLatest;
    }

    /** Get the specific ID of a dynamic service to call */
    public String getVersionId(){
        return versionId;
    }

    /** Get the specific ID of a dynamic service to call */
    public void setVersionId(String versionId){
        this.versionId = versionId;
    }
    
    /** Get the ID of the script file that provides this service */
    public String getScriptId(){
        return scriptId;
    }

    /** Set the ID of the script file that provides this service */
    public void setScriptId(String scriptId){
        this.scriptId = scriptId;
    }
    
    /** Get the provisioning back end type for this service. This will
     * either be a Java provisioned internal service or an interpreted service
     * such as an R-Script or JavaScript file */
    public String getServiceBackend(){
        return serviceBackend;
    } 
    
    /** Get the provisioning back end type for this service. This will
     * either be a Java provisioned internal service or an interpreted service
     * such as an R-Script or JavaScript file */
    public void setServiceBackend(String serviceBackend){
        this.serviceBackend = serviceBackend;
    } 

    /** Set the transfer modes of the inputs */
    public void setDataSourceModes(String[] dataSourceModes){
        this.dataSourceModes = dataSourceModes;
    }

    /** Get the transfer modes of the inputs */
    public String[] getDataSourceModes(){
        return dataSourceModes;
    }

    /** Set the data source names */
    public void setDataSources(String[] dataSources){
        this.dataSources = dataSources;
    }
    
    /** Get the data source names */
    public String[] getDataSources(){
        return dataSources;
    }
    
    /** Get a list of data source type names */
    public String[] getDataSourceTypes(){
        return dataSourceTypes;
    }
    
    /** Get a list of the ports connected to the inputs */
    public String[] getDataSourceConnections(){
        return dataSourceConnections;
    }
    
    /** Set the list of ports connected to the inputs */
    public void setDataSourceConnections(String[] dataSourceConnections){
        this.dataSourceConnections = dataSourceConnections;
    }
    
    /** Get a list of the block contexts attached to each of the inputs */
    public String[] getDataSourceConnectionContexts(){
        return dataSourceConnectionContexts;
    }
    
    /** Set the list of the block contexts attached to each of the inputs */
    public void setDataSourceConnectionContexts(String[] dataSourceConnectionContexts){
        this.dataSourceConnectionContexts = dataSourceConnectionContexts;
    }
    
    /** Get the source data type for a specific input */
    public String getDataSourceType(String inputName) {
        for(int i=0;i<dataSources.length;i++){
            if(dataSources[i].equals(inputName)){
                return dataSourceTypes[i];
            }
        }
        return null;
    }
    
    /** Get the position of a source within the source list */
    public int getDataSourceIndex(String inputName){
        for(int i=0;i<dataSources.length;i++){
            if(dataSources[i].equals(inputName)){
                return i;
            }
        }
        return -1;
    }

    /** Get the mode of an input */
    public String getDataSourceMode(String inputName){
        int index = getDataSourceIndex(inputName);
        if(index!=-1){
            if(index>=0 && index<dataSourceModes.length){
                return dataSourceModes[index];
            } else {
                return DataProcessorIODefinition.NON_STREAMING_CONNECTION;
            }
        } else {
            return DataProcessorIODefinition.NON_STREAMING_CONNECTION;
        }
    }
    
    /** Set the data source types */
    public void setDataSourceTypes(String[] dataSourceTypes){
        this.dataSourceTypes = dataSourceTypes;
    }
    
    /** Get the data outputs */
    public String[] getDataOutputs(){
        return dataOutputs;
    }
    
    /** Set the data outputs */
    public void setDataOutputs(String[] dataOutputs){
        this.dataOutputs = dataOutputs;
    }
    
    /** Get a list of data output type names */
    public String[] getDataOutputTypes(){
        return dataOutputTypes;
    }
    
    /** Get the source data type for a specific output */
    public String getDataOutputType(String outputName) {
        for(int i=0;i<dataOutputs.length;i++){
            if(dataOutputs[i].equals(outputName)){
                return dataOutputTypes[i];
            }
        }
        return null;
    }
    
    /** Set the data output type names */
    public void setDataOutputTypes(String[] dataOutputTypes){
        this.dataOutputTypes = dataOutputTypes;
    }
    
    /** Set the invocation ID */
    public void setInvocationId(String invocationId){
        this.invocationId = invocationId;
    }
    
    /** Set the contextID */
    public void setContextId(String contextId){
        this.contextId = contextId;
    }
    
    /** Get the invocation ID */
    public String getInvocationId(){
        return invocationId;
    }
    
    /** Get the context ID */
    public String getContextId(){
        return contextId;
    }    
    
    /** Set the name of the routine within the service to call */
    public void setServiceRoutine(String serviceRoutine){
        this.serviceRoutine = serviceRoutine;
    }
    
    /** Get the name of the routine within the service to call */
    public String getServiceRoutine(){
        return serviceRoutine;
    }
    
    /** Get the URL of the service that the block will invoke */
    public String getServiceUrl() {
        return serviceUrl;
    }

    /** Set the URL of the service that the block will invoke */
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
    
    /** Set the storage URL */
    public void setStorageUrl(String storageUrl){
        this.storageUrl = storageUrl;
    }
    
    /** Get the storage URL */
    public String getStorageUrl(){
        return storageUrl;
    }
    
    /** Get the data transfer type. This specifies how the service should
     * retrieve and upload data */
    public String getDataTransferType(){
        return dataTransferType;
    }
    
    /** Get the data transfer type. This specifies how the service should
     * retrieve and upload data */
    public void setDataTransferType(String dataTransferType){
        this.dataTransferType = dataTransferType;
    }
    
    /** Get the service properties object */
    public XmlDataStore getProperties(){
        return properties;
    }
    
    /** Set the ticket data */
    public void setTicketData(byte[] ticketData){
        this.ticketData = ticketData;
    }
    
    /** Get the ticket data */
    public byte[] getTicketData(){
        return ticketData;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    public int getDebugPort() {
        return debugPort;
    }

    public void setDebugPort(int debugPort) {
        this.debugPort = debugPort;
    }

    public boolean isIdempotent()
    {
      return idempotent;
    }

    public void setIdempotent(boolean idempotent)
    {
      this.idempotent = idempotent;
    }

    public boolean isDeterministic()
    {
      return deterministic;
    }

    public void setDeterministic(boolean deterministic)
    {
      this.deterministic = deterministic;
    }

    public void setOkToRetry(boolean okToRetry) {
        this.okToRetry = okToRetry;
    }

    public boolean isOkToRetry() {
        return okToRetry;
    }

    public void setRetryAttempts(int retryAttempts) {
        this.retryAttempts = retryAttempts;
    }

    public int getRetryAttempts() {
        return retryAttempts;
    }

    
  /** Save this object to storage */
    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("DataProcessorMessage");
        store.add("InvocationID", invocationId);
        store.add("ContextID", contextId);     
        store.add("ServiceRoutine", serviceRoutine);
        store.add("ServiceURL", serviceUrl);
        store.add("SourceCount", dataSources.length);
        store.add("StorageURL", storageUrl);
        store.add("DataTransferType", dataTransferType);
        store.add("Properties", properties);
        store.add("TicketData", ticketData);
        store.add("ScriptID", scriptId);
        store.add("ServiceBackend", serviceBackend);
        store.add("StreamMode", streamMode);
        store.add("ServiceID", serviceId);
        store.add("UseLatestServiceVersion", useLatest);
        store.add("VersionID", versionId);
        store.add("MaxStdOutBufferSize", maxStdOutBufferSize);
        store.add("DebugEnabled", debugEnabled);
        store.add("DebugPort", debugPort);
        store.add("WorkflowID", workflowId);
        store.add("WorkflowVersionID", workflowVersionId);
        store.add("Idempotent", idempotent);
        store.add("Deterministic", deterministic);
        store.add("OKToRetry", okToRetry);
        store.add("RetryAttempts", retryAttempts);
        for(int i=0;i<dataSources.length;i++){
            store.add("DataSource" + i, dataSources[i]);
            store.add("DataSourceType" + i, dataSourceTypes[i]);
            store.add("DataSourceConnection" + i, dataSourceConnections[i]);
            store.add("DataSourceConnectionContext" + i, dataSourceConnectionContexts[i]);
            store.add("DataSourceMode" + i, dataSourceModes[i]);
        }
        
        store.add("OutputCount", dataOutputs.length);
        for(int i=0;i<dataOutputs.length;i++){
            store.add("DataOutput" + i, dataOutputs[i]);
            store.add("DataOutputType" + i, dataOutputTypes[i]);
        }
        return store;
    }

    /** Recreate this object from storage */
    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        this.invocationId = store.stringValue("InvocationID", "");
        this.contextId = store.stringValue("ContextID", contextId);        
        serviceRoutine = store.stringValue("ServiceRoutine", "");
        serviceUrl = store.stringValue("ServiceURL", ""); 
        storageUrl = store.stringValue("StorageURL", "");
        scriptId = store.stringValue("ScriptID", "");
        serviceBackend = store.stringValue("ServiceBackend", DataProcessorServiceDefinition.NO_SCRIPT);
        streamMode = store.stringValue("StreamMode", DataProcessorServiceDefinition.STREAM_NO_STREAM_MODE);
        serviceId = store.stringValue("ServiceID", "");
        versionId = store.stringValue("VersionID", "");
        useLatest = store.booleanValue("UseLatestServiceVersion", true);
        maxStdOutBufferSize = store.intValue("MaxStdOutBufferSize", 4096);
        debugEnabled = store.booleanValue("DebugEnabled", false);
        debugPort = store.intValue("DebugPort", 5005);
        workflowId = store.stringValue("WorkflowID", null);
        workflowVersionId = store.stringValue("WorkflowVersionID", null);
        idempotent = store.booleanValue("Idempotent", true);
        deterministic = store.booleanValue("Deterministic", true);
        okToRetry = store.booleanValue("OKToRetry", false);
        retryAttempts = store.intValue("RetryAttempts", 0);

        int size = store.intValue("SourceCount", 0);
        dataTransferType = store.stringValue("DataTransferType", DataProcessorDataSource.FILE_DATA_SOURCE);
        properties = store.xmlDataStoreValue("Properties");
        ticketData = store.byteArrayValue("TicketData");
        dataSources = new String[size];
        dataSourceTypes = new String[size];
        dataSourceConnections = new String[size];
        dataSourceConnectionContexts = new String[size];
        dataSourceModes = new String[size];
        
        for(int i=0;i<size;i++){
            dataSources[i] = store.stringValue("DataSource" + i, "");
            dataSourceTypes[i] = store.stringValue("DataSourceType" + i, "");
            dataSourceConnections[i] = store.stringValue("DataSourceConnection" + i, "");
            dataSourceConnectionContexts[i] = store.stringValue("DataSourceConnectionContext" + i, "");
            dataSourceModes[i] = store.stringValue("DataSourceMode" + i, DataProcessorIODefinition.NON_STREAMING_CONNECTION);
        }
        
        size = store.intValue("OutputCount", 0);
        dataOutputs = new String[size];
        dataOutputTypes = new String[size];
        for(int i=0;i<size;i++){
            dataOutputs[i] = store.stringValue("DataOutput" + i, "");
            dataOutputTypes[i] = store.stringValue("DataOutputType" + i, "");
        }
    }

    /** Save this call message to an XML Document */
    public Document toXmlDocument() throws Exception {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        documentFactory.setNamespaceAware(true);
        DocumentBuilder builder = documentFactory.newDocumentBuilder();
        Document doc = builder.newDocument();

        // Root node
        Element rootElement = doc.createElementNS(XML_NAMESPACE, "Message");

        // Standard properties

        // ContextID
        Element contextIdElement = doc.createElementNS(XML_NAMESPACE, "ContextID");
        contextIdElement.setAttribute("value", contextId);
        contextIdElement.appendChild(doc.createTextNode(contextId));
        rootElement.appendChild(contextIdElement);

        // Data transfer type
        Element dataTransferTypeElement = doc.createElementNS(XML_NAMESPACE, "DataTransferType");
        dataTransferTypeElement.setAttribute("value", dataTransferType);
        dataTransferTypeElement.appendChild(doc.createTextNode(dataTransferType));
        rootElement.appendChild(dataTransferTypeElement);

        // Invocation ID
        Element invocationIdElement = doc.createElementNS(XML_NAMESPACE, "InvocationID");
        invocationIdElement.setAttribute("value", invocationId);
        invocationIdElement.appendChild(doc.createTextNode(invocationId));
        rootElement.appendChild(invocationIdElement);

        // ScriptID
        Element scriptIdElement = doc.createElementNS(XML_NAMESPACE, "ScriptID");
        scriptIdElement.setAttribute("value", scriptId);
        scriptIdElement.appendChild(doc.createTextNode(scriptId));
        rootElement.appendChild(scriptIdElement);

        // WorkflowID
        Element workflowIdElement = doc.createElementNS(XML_NAMESPACE, "WorkflowID");
        workflowIdElement.setAttribute("value", workflowId);
        workflowIdElement.appendChild(doc.createTextNode(workflowId));
        rootElement.appendChild(workflowIdElement);

        // Workflow version ID
        Element workflowVersionIdElement = doc.createElementNS(XML_NAMESPACE, "WorkflowVersionID");
        workflowVersionIdElement.setAttribute("value", versionId);
        workflowVersionIdElement.appendChild(doc.createTextNode(versionId));
        rootElement.appendChild(workflowVersionIdElement);

        // Service Backend type
        Element serviceBackendElement = doc.createElementNS(XML_NAMESPACE, "ServiceBackend");
        serviceBackendElement.setAttribute("value", serviceBackend);
        scriptIdElement.appendChild(doc.createTextNode(serviceBackend));
        rootElement.appendChild(serviceBackendElement);

        // Service Routine
        Element serviceRoutineElement = doc.createElementNS(XML_NAMESPACE, "ServiceRoutine");
        serviceRoutineElement.setAttribute("value", serviceRoutine);
        serviceBackendElement.appendChild(doc.createTextNode(serviceRoutine));
        rootElement.appendChild(serviceRoutineElement);

        // Service URL
        Element serviceUrlElement = doc.createElementNS(XML_NAMESPACE, "ServiceURL");
        serviceUrlElement.setAttribute("value", serviceUrl);
        serviceUrlElement.appendChild(doc.createTextNode(serviceUrl));
        rootElement.appendChild(serviceUrlElement);

        // Storage URL
        Element storageUrlElement = doc.createElementNS(XML_NAMESPACE, "StorageURL");
        storageUrlElement.setAttribute("value", storageUrl);
        storageUrlElement.appendChild(doc.createTextNode(storageUrl));
        rootElement.appendChild(storageUrlElement);

        // Streaming mode
        Element streamModeElement = doc.createElementNS(XML_NAMESPACE, "StreamMode");
        streamModeElement.setAttribute("value", streamMode);
        streamModeElement.appendChild(doc.createTextNode(streamMode));
        rootElement.appendChild(streamModeElement);

        // Service ID node
        Element serviceIdElement = doc.createElementNS(XML_NAMESPACE, "ServiceID");
        serviceIdElement.setAttribute("value", serviceId);
        serviceIdElement.appendChild(doc.createTextNode(serviceId));
        rootElement.appendChild(serviceIdElement);

        // Buffer size
        Element stdOutBufferElement = doc.createElementNS(XML_NAMESPACE, "MaxStdOutBufferSize");
        stdOutBufferElement.setAttribute("value", Integer.toString(maxStdOutBufferSize));
        serviceIdElement.appendChild(doc.createTextNode(Integer.toString(maxStdOutBufferSize)));
        rootElement.appendChild(stdOutBufferElement);
        
        // Use latest flag node
        String value;
        if(useLatest){
            value = "true";
        } else {
            value = "false";
        }
        Element useLatestVersionElement = doc.createElementNS(XML_NAMESPACE, "UseLatestServiceVersion");
        useLatestVersionElement.setAttribute("value", value);
        useLatestVersionElement.appendChild(doc.createTextNode(value));
        rootElement.appendChild(useLatestVersionElement);

        // Version ID node
        Element versionIdElement = doc.createElementNS(XML_NAMESPACE, "VersionID");
        versionIdElement.setAttribute("value", versionId);
        versionIdElement.appendChild(doc.createTextNode(versionId));
        rootElement.appendChild(versionIdElement);

        // Debug mode
        Element debugEnabledElement = doc.createElementNS(XML_NAMESPACE, "DebugEnabled");
        if(debugEnabled){
            debugEnabledElement.setAttribute("value", "true");
            debugEnabledElement.appendChild(doc.createTextNode("true"));
        } else {
            debugEnabledElement.setAttribute("value", "false");
            debugEnabledElement.appendChild(doc.createTextNode("false"));
        }
        rootElement.appendChild(debugEnabledElement);

        // Debug port
        Element debugPortElement = doc.createElementNS(XML_NAMESPACE, "DebugPort");
        debugEnabledElement.setAttribute("value", Integer.toString(debugPort));
        debugEnabledElement.appendChild(doc.createTextNode(Integer.toString(debugPort)));
        rootElement.appendChild(debugPortElement);

      // Idempotent

        if(idempotent){
            value = "true";
        } else {
            value = "false";
        }
        Element idempotentElement = doc.createElementNS(XML_NAMESPACE, "Idempotent");
        idempotentElement.setAttribute("value", value);
        idempotentElement.appendChild(doc.createTextNode(value));
        rootElement.appendChild(idempotentElement);

      // Use latest flag node
        if(deterministic){
            value = "true";
        } else {
            value = "false";
        }
        Element deterministicElement = doc.createElementNS(XML_NAMESPACE, "Deterministic");
        deterministicElement .setAttribute("value", value);
        deterministicElement .appendChild(doc.createTextNode(value));
        rootElement.appendChild(deterministicElement );
        
        // Input connections and associated linked blocks
        Element inputsElement = doc.createElementNS(XML_NAMESPACE, "Inputs");
        Element input;
        for(int i=0;i<dataSourceConnections.length;i++){
            input = doc.createElementNS(XML_NAMESPACE, "Input");
            input.setAttribute("SourceConnection", dataSourceConnections[i]);
            input.setAttribute("SourceConnectionContext", dataSourceConnectionContexts[i]);
            input.setAttribute("SourceType", dataSourceTypes[i]);
            input.setAttribute("Source", dataSources[i]);
            input.setAttribute("Mode", dataSourceModes[i]);

            Element sc = doc.createElementNS(XML_NAMESPACE, "SourceConnection");
            sc.appendChild(doc.createTextNode(dataSourceConnections[i]));
            input.appendChild(sc);

            Element scc = doc.createElementNS(XML_NAMESPACE, "SourceConnectionContext");
            scc.appendChild(doc.createTextNode(dataSourceConnectionContexts[i]));
            input.appendChild(scc);

            Element st = doc.createElementNS(XML_NAMESPACE, "SourceType");
            st.appendChild(doc.createTextNode(dataSourceTypes[i]));
            input.appendChild(st);

            Element s = doc.createElementNS(XML_NAMESPACE, "Source");
            s.appendChild(doc.createTextNode(dataSources[i]));
            input.appendChild(s);

            Element m = doc.createElementNS(XML_NAMESPACE, "Mode");
            m.appendChild(doc.createTextNode(dataSourceModes[i]));
            input.appendChild(m);
            
            inputsElement.appendChild(input);
        }
        rootElement.appendChild(inputsElement);

        // Output data targets
        Element outputsElement = doc.createElementNS(XML_NAMESPACE, "Outputs");
        Element output;
        for(int i=0;i<dataOutputs.length;i++){
            output = doc.createElementNS(XML_NAMESPACE, "Output");
            output.setAttribute("Output", dataOutputs[i]);
            output.setAttribute("OutputType", dataOutputTypes[i]);

            Element n = doc.createElementNS(XML_NAMESPACE, "Name");
            n.appendChild(doc.createTextNode(dataOutputs[i]));
            output.appendChild(n);

            Element t = doc.createElementNS(XML_NAMESPACE, "OutputType");
            t.appendChild(doc.createTextNode(dataOutputTypes[i]));
            output.appendChild(t);
            
            outputsElement.appendChild(output);
        }
        rootElement.appendChild(outputsElement);

        // Additional properties
        Enumeration e = properties.elements();
        Element propertiesElement = doc.createElementNS(XML_NAMESPACE, "Properties");

        XmlDataObject property;
        while(e.hasMoreElements()){
            property = (XmlDataObject)e.nextElement();
            property.appendToXmlElement(doc, propertiesElement, false);
        }
        rootElement.appendChild(propertiesElement);
        doc.appendChild(rootElement);
        return doc;
    }

    /** Recreate this call message from an XML Document */
    public void parseXmlDocument(Document doc) throws Exception {
        Element rootElement = doc.getDocumentElement();

        NodeList children = rootElement.getChildNodes();
        Element element;
        String nodeName;

        for(int i=0;i<children.getLength();i++){
            element = (Element)children.item(i);
            nodeName = element.getNodeName();

            if(nodeName.equalsIgnoreCase("ContextID")){
                contextId = element.getAttribute("value").trim();

            } else if(nodeName.equalsIgnoreCase("DataTransferType")){
                dataTransferType = element.getAttribute("value").trim();

            } else if(nodeName.equalsIgnoreCase("InvocationID")){
                invocationId = element.getAttribute("value").trim();
                
            } else if(nodeName.equalsIgnoreCase("ScriptID")){
                scriptId = element.getAttribute("value").trim();

            } else if(nodeName.equalsIgnoreCase("WorkflowID")){
                workflowId = element.getAttribute("value").trim();
                
            } else if(nodeName.equalsIgnoreCase("ServiceBackend")){
                serviceBackend = element.getAttribute("value").trim();

            } else if(nodeName.equalsIgnoreCase("ServiceRoutine")){
                serviceRoutine = element.getAttribute("value").trim();

            } else if(nodeName.equalsIgnoreCase("ServiceURL")){
                serviceUrl = element.getAttribute("value").trim();

            } else if(nodeName.equalsIgnoreCase("StorageURL")){
                storageUrl = element.getAttribute("value").trim();

            } else if(nodeName.equalsIgnoreCase("StreamMode")){
                setStreamMode(element.getAttribute("value").trim());
                
            } else if(nodeName.equalsIgnoreCase("Inputs")){
                // Parse the inputs

            } else if(nodeName.equalsIgnoreCase("Outputs")){
                // Parse the outputs

            } else if(nodeName.equalsIgnoreCase("Properties")){
                // Parse the properties
                
            }
        }
        

    }
}