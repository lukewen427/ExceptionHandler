/*
 * DataProcessorServiceDefinition.java
 */

package com.connexience.server.workflow.service;
import com.connexience.server.workflow.xmlstorage.*;

import org.pipeline.core.xmlstorage.*;
import org.pipeline.core.xmlstorage.xmldatatypes.*;

import java.util.*;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * This class contains details of an invokable data processor service
 * @author hugo
 */
public class DataProcessorServiceDefinition implements XmlStorable, Serializable {
    /** String property */
    public static final String PROPERTY_TYPE_STRING = "String";

    /** Boolean property */
    public static final String PROPERTY_TYPE_BOOLEAN = "Boolean";

    /** Integer property */
    public static final String PROPERTY_TYPE_INTEGER = "Integer";

    /** Date property */
    public static final String PROPERTY_TYPE_DATE = "Date";

    /** Double property */
    public static final String PROPERTY_TYPE_DOUBLE = "Double";

    /** Connexience folder reference property */
    public static final String PROPERTY_TYPE_FOLDER_REFERENCE = "Folder";

    /** Connexience DocumentRecord reference property */
    public static final String PROPERTY_TYPE_DOCUMENT_RECORD = "Document";

    /** Array of Strings */
    public static final String PROPERTY_TYPE_STRING_ARRAY = "StringList";

    /** Two column list of strings */
    public static final String PROPERTY_TYPE_2COLUMN_STRING_ARRAY = "TwoColumnList";

    /** External object property */
    public static final String PROPERTY_TYPE_EXTERNAL_OBJECT = "ExternalObject";

    // Service type flags
    
    /** Internal RPC service */
    public static final int RPC_SERVICE = 0;
    
    /** Web Service */
    public static final int HTTP_SERVICE = 1;
    
    /** RMI Service */
    public static final int RMI_SERVICE = 2;

    /** Dynamically deployed cloud service */
    public static final int AUTODEPLOY_SERVICE = 3;
    
    /** Internally provisioned service. i.e. a Java web service or routine */
    public static final String NO_SCRIPT = "Internal";
    
    /** R-Interpreted service */
    public static final String R_SCRIPT = "R-Service";    

    // Streaming modes
    /** Service does not support streaming in any way */
    public static final String STREAM_NO_STREAM_MODE = "nostream";

    /** Data should be streamed in parallel from the input connections */
    public static final String STREAM_PARALLEL_MODE = "parallel";

    /** Data should be streamed sequentially from the input connections */
    public static final String STREAM_SQEUENTIAL_MODE = "sequential";

    // Connection types
    /** Streaming connection type */
    public static final String STREAMING_CONNECTION = "Streaming";

    /** Non-Streaming connection type */
    public static final String NON_STREAMING_CONNECTION = "NonStreaming";

    /** Input definitions */
    public Vector<DataProcessorIODefinition>inputs = new Vector<DataProcessorIODefinition>();
    
    /** Output definitions */
    public Vector<DataProcessorIODefinition>outputs = new Vector<DataProcessorIODefinition>();
    
    /** Editable properties for the service */
    private XmlDataStore serviceProperties = new XmlDataStore();
    
    /** Name of this item */
    private String name = "";
    
    /** Description of this item */
    private String description = "";
    
    /** Link to the website for this item */
    private String homepage = "";

    /** Location of the service that the pallete item refers to */
    private String serviceUrl = "";

    /** Specific routine within the service to call if there is one */
    private String serviceRoutine = "";
    
    /** Service type */
    private int serviceType = RPC_SERVICE;
    
    /** Back end type for this service */
    private String serviceBackend = NO_SCRIPT;
    
    /** ID Of the script resource for this service if there is one */
    private String scriptId = "";

    /** Streaming mode for this service */
    private String streamMode = STREAM_NO_STREAM_MODE;

    /** Service version number */
    private int versionNumber;

    /** Service category */
    private String category = "My Services";

    /** Is the service being called idempotent */
    private boolean idempotent = true;

    /** Is the service being called deterministic */
    private boolean deterministic = true;


    /** Create a DataProcessorServiceDefinition */
    public DataProcessorServiceDefinition() {
    }
    
    /** Create a DataProcessorServiceDefinition */
    public DataProcessorServiceDefinition(String name) {
        this.name = name;
    }

    /** Get the service category */
    public String getCategory() {
        return category;
    }

    /** Set the service category */
    public void setCategory(String category) {
        this.category = category;
    }


    /** Get the service version number */
    public int getVersionNumber(){
        return versionNumber;
    }

    /** Set the service version number */
    public void setVersionNumber(int versionNumber){
        this.versionNumber = versionNumber;
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

    /** Get the URL of the service that the block will invoke */
    public String getServiceUrl() {
        return serviceUrl;
    }

    /** Set the URL of the service that the block will invoke */
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    /** Get the item description */
    public String getDescription() {
        return description;
    }

    /** Set the item description */
    public void setDescription(String description) {
        this.description = description;
    }

    /** Get the homepage of this item for documentation etc if there is one */
    public String getHomepage() {
        return homepage;
    }

    /** Set the homepage of this item for documentation etc if there is one */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /** Get the service type */
    public int getServiceType(){
        return serviceType;
    }
    
    /** Set the service type */
    public void setServiceType(int serviceType){
        this.serviceType = serviceType;
    }
    
    /** Get the name of the specific routine within the service to call */
    public String getServiceRoutine(){
        return serviceRoutine;
    }
    
    /** Set the name of the specific routine within the service to call */
    public void setServiceRoutine(String serviceRoutine){
        this.serviceRoutine = serviceRoutine;
    }
    
    /** Get the service name */
    public String getName(){
        return name;
    }
    
    /** Set the service name */
    public void setName(String name){
        this.name = name;
    }

    /** Get the streaming mode */
    public String getStreamMode(){
        return streamMode;
    }

    /** Set the streaming mode */
    public void setStreamMode(String streamMode){
        String lsm = streamMode.toLowerCase();
        if(lsm.equals(STREAM_NO_STREAM_MODE) || lsm.equals(STREAM_PARALLEL_MODE) || lsm.equals(STREAM_SQEUENTIAL_MODE)){
            this.streamMode = lsm;
        }
    }

    public boolean isIdempotent() {
        return idempotent;
    }

    public void setIdempotent(boolean idempotent) {
        this.idempotent = idempotent;
    }

    public boolean isDeterministic() {
        return deterministic;
    }

    public void setDeterministic(boolean deterministic) {
        this.deterministic = deterministic;
    }

    /** Get the inputs **/
    public Vector<DataProcessorIODefinition> getInputs(){
        return inputs;
    }
    
    /** Get the outputs */
    public Vector<DataProcessorIODefinition> getOutputs(){
        return outputs;
    }
    
    /** Get the service properties */
    public XmlDataStore getServiceProperties(){
        return serviceProperties;
    }
    
    /** Add an input to this definition */
    public void addInput(String inputName, String dataTypeName, String mode){
        DataProcessorIODefinition def = new DataProcessorIODefinition();
        def.setType(DataProcessorIODefinition.INPUT_DEFINITION);
        def.setDataTypeName(dataTypeName);
        def.setName(inputName);
        def.setMode(mode);
        inputs.add(def);
    }
    
    /** Add an output to this defintion */
    public void addOutput(String outputName, String dataTypeName){
        DataProcessorIODefinition def = new DataProcessorIODefinition();
        def.setType(DataProcessorIODefinition.OUPUT_DEFINITION);
        def.setDataTypeName(dataTypeName);
        def.setName(outputName);
        def.setMode(DataProcessorIODefinition.NON_STREAMING_CONNECTION);
        outputs.add(def);
    }

    /** Load data from an XML string */
    public void loadXmlString(String xml) throws Exception {
        if(xml!=null){
            loadXmlDocument(readXmlDocumentFromString(xml));
        } else {
            throw new Exception("NULL xml data");
        }
    }

    /** Parse an XML document */
    public void loadXmlDocument(Document doc) throws Exception {
        // Get the top level element
        Element documentElement = doc.getDocumentElement();

        // Get all of the child elements
        NodeList contentNodes = documentElement.getChildNodes();
        Node currentElement;
        for(int i=0;i<contentNodes.getLength();i++){

            currentElement = (Node)contentNodes.item(i);

            // First search for standard properties
            String nodeName = currentElement.getNodeName().trim();
            if(nodeName.equalsIgnoreCase("name")){
                this.setName(currentElement.getTextContent().trim());

            } else if(nodeName.equalsIgnoreCase("description")){
                setDescription(currentElement.getTextContent().trim());

            } else if(nodeName.equalsIgnoreCase("homepage")){
                setHomepage(currentElement.getTextContent().trim());

            } else if(nodeName.equalsIgnoreCase("serviceroutine")){
                setServiceRoutine(currentElement.getTextContent().trim());

            } else if(nodeName.equalsIgnoreCase("serviceurl")){
                setServiceUrl(currentElement.getTextContent().trim());

            } else if(nodeName.equalsIgnoreCase("category")){
                category = currentElement.getTextContent().trim();
                
            } else if(nodeName.equalsIgnoreCase("servicetype")){
                String type = currentElement.getTextContent().trim();
                if(type.equalsIgnoreCase("rpc")){
                    setServiceType(RPC_SERVICE);
                } else if(type.equalsIgnoreCase("http")){
                    setServiceType(HTTP_SERVICE);
                } else if(type.equalsIgnoreCase("rmi")){
                    setServiceType(RMI_SERVICE);
                } else if(type.equalsIgnoreCase("auto")){
                    setServiceType(AUTODEPLOY_SERVICE);
                } else {
                    throw new DataProcessorException("Unrecognised service type: " + type);
                }

            } else if(nodeName.equalsIgnoreCase("externallyprovisioned")){
                if(currentElement.getTextContent().trim().equalsIgnoreCase("true")){
                    setServiceBackend(R_SCRIPT);
                } else {
                    setServiceBackend(NO_SCRIPT);
                }

            } else if(nodeName.equalsIgnoreCase("externalscriptid")){
                setScriptId(currentElement.getTextContent().trim());

            } else if(nodeName.equalsIgnoreCase("streammode")){
                setStreamMode(currentElement.getTextContent().trim());

            } else if(nodeName.equalsIgnoreCase("idempotent")){
                setIdempotent(Boolean.valueOf(currentElement.getTextContent().trim()));

            } else if(nodeName.equalsIgnoreCase("deterministic")){
                setDeterministic(Boolean.valueOf(currentElement.getTextContent().trim()));

            } else if(nodeName.equalsIgnoreCase("properties")){
                // Set the service property definitions
                processProperties(currentElement);

            } else if(nodeName.equalsIgnoreCase("inputs")){
                // Set the service input definitions
                processInputs(currentElement);

            } else if(nodeName.equalsIgnoreCase("outputs")){
                // Set the service output definitions
                processOutputs(currentElement);

            }
        }
    }

    /** Parse an XML document to create this service */
    public void loadXmlStream(InputStream stream) throws Exception {
        // Read the document
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setCoalescing(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(stream);
        loadXmlDocument(doc);
    }

    /** Process the service properties from a Node */
    private void processProperties(Node node) throws Exception {
        NodeList children = node.getChildNodes();
        Node propertyNode;
        serviceProperties.clear();
        String propertyName ;
        String type;
        String propertyDescription;
        String defaultValue;
        String category;
        XmlDataObject dataObject;

        NamedNodeMap attributeMap;

        for(int i=0;i<children.getLength();i++){
            if(children.item(i).getNodeName().equalsIgnoreCase("property")){
                propertyNode = (Node)children.item(i);
                attributeMap = propertyNode.getAttributes();
                propertyName = attributeMap.getNamedItem("name").getTextContent().trim();
                
                if(attributeMap.getNamedItem("type")!=null){
                    type = attributeMap.getNamedItem("type").getTextContent().trim();
                } else {
                    type = "String";
                }
                
                if(attributeMap.getNamedItem("description")!=null){
                    propertyDescription = attributeMap.getNamedItem("description").getTextContent().trim();
                } else {
                    propertyDescription = "";
                }
                
                if(attributeMap.getNamedItem("default")!=null){
                    defaultValue = attributeMap.getNamedItem("default").getTextContent().trim();
                } else {
                    defaultValue = "";
                }
                
                if(attributeMap.getNamedItem("category")!=null){
                    category = attributeMap.getNamedItem("category").getTextContent().trim();
                } else {
                    category = "Block";
                }

                dataObject = null;

                if(type.equals(PROPERTY_TYPE_STRING)){
                    dataObject = new XmlStringDataObject(propertyName);
                    ((XmlStringDataObject)dataObject).setStringValue(defaultValue);

                } else if(type.equals(PROPERTY_TYPE_INTEGER)){
                    dataObject = new XmlLongDataObject(propertyName);
                    try {
                        ((XmlLongDataObject)dataObject).setLongValue(Long.parseLong(defaultValue));
                    } catch (Exception e){
                        ((XmlLongDataObject)dataObject).setLongValue(0);
                    }

                } else if(type.equals(PROPERTY_TYPE_DATE)){
                    dataObject = new XmlDateDataObject(propertyName);
                    ((XmlDateDataObject)dataObject).setDateValue(new Date());

                } else if(type.equals(PROPERTY_TYPE_DOCUMENT_RECORD)){
                    dataObject = new XmlStorableDataObject(propertyName);
                    ((XmlStorableDataObject)dataObject).setXmlStorableValue(new DocumentRecordWrapper());

                } else if(type.equals(PROPERTY_TYPE_DOUBLE)){
                    dataObject = new XmlDoubleDataObject(propertyName);
                    try {
                        ((XmlDoubleDataObject)dataObject).setDoubleValue(Double.parseDouble(defaultValue));
                    } catch (Exception e){
                        ((XmlDoubleDataObject)dataObject).setDoubleValue(0.0);
                    }

                } else if(type.equals(PROPERTY_TYPE_FOLDER_REFERENCE)){
                    dataObject = new XmlStorableDataObject(propertyName);
                    ((XmlStorableDataObject)dataObject).setXmlStorableValue(new FolderWrapper());

                } else if(type.equals(PROPERTY_TYPE_BOOLEAN)){
                    dataObject = new XmlBooleanDataObject(propertyName);

                    if(defaultValue.equalsIgnoreCase("yes") || defaultValue.equalsIgnoreCase("true")){
                        ((XmlBooleanDataObject)dataObject).setBooleanValue(true);
                    } else {
                        ((XmlBooleanDataObject)dataObject).setBooleanValue(false);
                    }

                } else if(type.equals(PROPERTY_TYPE_STRING_ARRAY)){
                    dataObject = new XmlStorableDataObject(propertyName);
                    ((XmlStorableDataObject)dataObject).setXmlStorableValue(new StringListWrapper());

                } else if(type.equals(PROPERTY_TYPE_2COLUMN_STRING_ARRAY)){
                    dataObject = new XmlStorableDataObject(propertyName);
                    ((XmlStorableDataObject)dataObject).setXmlStorableValue(new StringPairListWrapper());

                } else if(type.equals(PROPERTY_TYPE_EXTERNAL_OBJECT)){
                    dataObject = new XmlStorableDataObject(propertyName);
                    ((XmlStorableDataObject)dataObject).setXmlStorableValue(new ExternalObjectWrapper());
                }

                if(dataObject!=null){
                    dataObject.setDescription(propertyDescription);
                    dataObject.setCategory(category);
                    serviceProperties.add(dataObject);
                }
            }
        }
    }

    /** Process the service input definitions */
    private void processInputs(Node node) throws Exception {
        NodeList children = node.getChildNodes();
        Node inputNode;

        String inputName;
        String inputType;
        String inputMode;
        NamedNodeMap attributeMap;
        DataProcessorIODefinition input;

        for(int i=0;i<children.getLength();i++){
            if(children.item(i).getNodeName().equalsIgnoreCase("input")){
                inputNode = (Node)children.item(i);
                attributeMap = inputNode.getAttributes();
                inputName = attributeMap.getNamedItem("name").getTextContent().trim();
                inputType = attributeMap.getNamedItem("type").getTextContent().trim();

                if(attributeMap.getNamedItem("streaming")!=null){
                    if(attributeMap.getNamedItem("streaming").getTextContent().trim().equalsIgnoreCase("true")){
                        inputMode = STREAMING_CONNECTION;
                    } else {
                        inputMode = NON_STREAMING_CONNECTION;
                    }
                } else {
                    inputMode = NON_STREAMING_CONNECTION;
                }

                if(inputName!=null && inputType!=null && inputMode!=null){
                    input = new DataProcessorIODefinition();
                    input.setDataTypeName(inputType);
                    input.setMode(inputMode);
                    input.setName(inputName);
                    input.setType(DataProcessorIODefinition.INPUT_DEFINITION);
                    inputs.add(input);
                }
            }
        }
 }

    /** Process the service output definitions */
    private void processOutputs(Node node) throws Exception {
        NodeList children = node.getChildNodes();
        Node outputNode;

        String outputName;
        String outputType;
        NamedNodeMap attributeMap;
        DataProcessorIODefinition output;

        for(int i=0;i<children.getLength();i++){
            if(children.item(i).getNodeName().equalsIgnoreCase("output")){
                outputNode = (Node)children.item(i);
                attributeMap = outputNode.getAttributes();
                outputName = attributeMap.getNamedItem("name").getTextContent().trim();
                outputType = attributeMap.getNamedItem("type").getTextContent().trim();

                if(outputName!=null && outputType!=null){
                    output = new DataProcessorIODefinition();
                    output.setMode(NON_STREAMING_CONNECTION);
                    output.setName(outputName);
                    output.setDataTypeName(outputType);
                    output.setType(DataProcessorIODefinition.OUPUT_DEFINITION);
                    outputs.add(output);
                }
            }
        }
    }

    /** Save this definition */
    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("DataProcecssorServiceDefinition");
        store.add("Description", description);
        store.add("Homepage", homepage);
        store.add("ServiceURL", serviceUrl);
        store.add("Name", name);
        store.add("InputCount", inputs.size());
        store.add("ServiceProperties", serviceProperties);
        store.add("ServiceType", serviceType);
        store.add("ServiceRoutine", serviceRoutine);
        store.add("ServiceBackend", serviceBackend);
        store.add("ScriptID", scriptId);
        store.add("StreamMode", streamMode);
        store.add("Idempotent", idempotent);
        store.add("Deterministic", deterministic);


        for(int i=0;i<inputs.size();i++){
            store.add("Input" + i, inputs.get(i));
        }
        
        store.add("OutputCount", outputs.size());
        for(int i=0;i<outputs.size();i++){
            store.add("Output" + i, outputs.get(i));
        }
        return store;
    }

    /** Load defintiion */
    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        description = store.stringValue("Description", "");
        homepage = store.stringValue("Homepage", "");
        serviceUrl = store.stringValue("ServiceURL", "");        
        name = store.stringValue("Name", "");
        serviceProperties = store.xmlDataStoreValue("ServiceProperties");
        serviceType = store.intValue("ServiceType", RPC_SERVICE);
        serviceRoutine = store.stringValue("ServiceRoutine", "");
        serviceBackend = store.stringValue("ServiceBackend", NO_SCRIPT);
        scriptId = store.stringValue("ScriptID", "");
        streamMode = store.stringValue("StreamMode", STREAM_NO_STREAM_MODE);
        idempotent = store.booleanValue("Idempotent", true);
        deterministic= store.booleanValue("Deterministic", true);

        inputs.clear();
        int size = store.intValue("InputCount", 0);
        for(int i=0;i<size;i++){
            inputs.add((DataProcessorIODefinition)store.xmlStorableValue("Input" + i));
        }
        
        outputs.clear();
        size = store.intValue("OutputCount", 0);
        for(int i=0;i<size;i++){
            outputs.add((DataProcessorIODefinition)store.xmlStorableValue("Output"+ i));
        }
    }

    /** Read an XML document from a String */
    public static Document readXmlDocumentFromString(String xml) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setCoalescing(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new ByteArrayInputStream(xml.getBytes()));
        return doc;
    }
}