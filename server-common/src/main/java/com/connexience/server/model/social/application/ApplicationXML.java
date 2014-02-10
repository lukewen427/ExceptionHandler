/*
 * ApplicationXML.java
 */
package com.connexience.server.model.social.application;

import java.io.*;
import java.net.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
/**
 * This class can parse an application description xml file
 * @author hugo
 */
public class ApplicationXML {
    private String name;
    private String description;
    private String id;
    private String key;
    private String url;
    private String editUrl;
    private String iconUrl;
    private String mimeTypes;
    private ApplicationSubscription defaultSubscription;

    public ApplicationXML() {
    }
    
    /** Load from an http url */
    public void loadFromHttp(String sourceUrl) throws Exception {
        InputStream stream = null;
        try {
            URL url = new URL(sourceUrl);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(false);
            connection.setDoInput(true);
            stream = url.openStream();
            loadXmlStream(stream);
        } catch (Exception e){
            throw e; 
        } finally {
            if(stream!=null){
                stream.close();
            }
        }
    }
    
    /** Load from an XML stream */
    public void loadXmlStream(InputStream stream) throws Exception {
        // Read the document
        defaultSubscription = new ApplicationSubscription();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setCoalescing(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(stream);

        // Get the top level element
        Element documentElement = doc.getDocumentElement();

        // Get all of the child elements
        NodeList contentNodes = documentElement.getChildNodes();
        Node currentElement;
        for (int i = 0; i < contentNodes.getLength(); i++) {

            currentElement = (Node) contentNodes.item(i);

            // First search for standard properties
            String nodeName = currentElement.getNodeName().trim();
            if (nodeName.equalsIgnoreCase("name")) {
                name = currentElement.getTextContent().trim();
            } else if (nodeName.equalsIgnoreCase("description")) {
                description = currentElement.getTextContent().trim();
            } else if (nodeName.equalsIgnoreCase("requestedid")) {
                id = currentElement.getTextContent().trim();
            } else if (nodeName.equalsIgnoreCase("requestedkey")) {
                key = currentElement.getTextContent().trim();
            } else if (nodeName.equalsIgnoreCase("url")) {
                url = currentElement.getTextContent().trim();
            } else if (nodeName.equalsIgnoreCase("iconurl")) {
                iconUrl = currentElement.getTextContent().trim();
            } else if (nodeName.equalsIgnoreCase("editurl")) {
                editUrl = currentElement.getTextContent().trim();
            } else if (nodeName.equalsIgnoreCase("mimetypes")) {
                mimeTypes = currentElement.getTextContent().trim();
            } else if (nodeName.equalsIgnoreCase("permissions")) {
                // Process the required app permissions
                processPermissions(currentElement);
            }
        }
        defaultSubscription.setApplicationId(id);
    }    
    
    private void processPermissions(Node node) throws Exception {
        NodeList children = node.getChildNodes();
        Node inputNode;
        String name;
        String requiredValue;
        boolean required;
        NamedNodeMap attributeMap;

        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeName().equalsIgnoreCase("permission")) {
                inputNode = (Node) children.item(i);
                attributeMap = inputNode.getAttributes();
                name = attributeMap.getNamedItem("name").getTextContent().trim();
                requiredValue = attributeMap.getNamedItem("required").getTextContent();
                if(requiredValue!=null && requiredValue.isEmpty()==false){
                    if(requiredValue.equals("true") || requiredValue.equals("yes")){
                        required = true;
                    } else {
                        required = false;
                    }
                } else {
                    required = false;
                }

                if(name.equals("viewdetails")){
                    defaultSubscription.setAllowedToViewDetails(required);
                } else if(name.equals("viewfiles")){
                    defaultSubscription.setAllowedToViewFiles(required);
                } else if(name.equals("viewconnections")){
                    defaultSubscription.setAllowedToViewConnections(required);
                } else if(name.equals("postnews")){
                    defaultSubscription.setAllowedToPostNews(required);
                } else if(name.equals("executeworkflows")){
                    defaultSubscription.setAllowedToExecuteWorkflows(required);
                } else if(name.equals("uploadfiles")){
                    defaultSubscription.setAllowedToUploadFiles(required);
                } else if(name.equals("modifyfiles")){
                    defaultSubscription.setAllowedToModifyFiles(required);
                }
            }
        }
    }

    /** Get the default description that contains the desired permissions */
    public ApplicationSubscription getDefaultSubscription() {
        return defaultSubscription;
    }
    
    /** Get the actual application object */
    public ExternalApplication getExternalApplication(){
        ExternalApplication app = new ExternalApplication();
        app.setApplicationUrl(url);
        app.setCommunicationUrl("");
        app.setDocumentEditUrl(editUrl);
        app.setIconURL(iconUrl);
        app.setMimeTypeList(mimeTypes);
        app.setSecretKey(key);
        app.setName(name);
        app.setDescription(description);
        return app;
    }

    /** Get the requested application id */
    public String getId() {
        return id;
    }
}