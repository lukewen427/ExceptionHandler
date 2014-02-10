/*
 * ServiceXML.java
 */

package com.connexience.server.model.workflow;

import com.connexience.server.util.*;

import java.io.*;
import org.w3c.dom.*;

/**
 * This class represents an XML service definition file that is stored in the
 * database. It is stored separately from the actual service because it needs
 * to be downloaded quickly when editing workflows. This service XML data is
 * attached to a specific document version of a WorkflowService.
 * @author nhgh
 */
public class ServiceXML implements Serializable {
    /** Database ID */
    private String id;

    /** ID of the service document */
    public String serviceId;

    /** ID of the workflow document version */
    public String versionId;

    /** XML Data as a string */
    public String xmlData;

    /** Get the object ID */
    public String getId() {
        return id;
    }

    /** Set the object ID */
    public void setId(String id) {
        this.id = id;
    }

    /** Get the id of the workflow service that this XML refers to */
    public String getServiceId() {
        return serviceId;
    }

    /** Set the id of the workflow service that this XML refers to */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /** Get the ID of the service data that this XML file refers to */
    public String getVersionId() {
        return versionId;
    }

    /** Set the ID of the service data that this XML file refers to */
    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    /** Get the actual XML data as a string */
    public String getXmlData() {
        return xmlData;
    }

    /** Set the actual XML data as a string */
    public void setXmlData(String xmlData) {
        this.xmlData = xmlData;
    }

    public String getCategory() throws Exception {
        Document doc = XmlUtils.readXmlDocumentFromString(xmlData);
        String category = null;
        Element top = doc.getDocumentElement();
        NodeList children = top.getChildNodes();
        Node child;
        
        for(int i=0;i<children.getLength();i++){
            child = children.item(i);
            if(child.getNodeName().equalsIgnoreCase("category")){
                category = child.getTextContent().trim();
            }
        }
        return category;
    }

    /** Change the category */
    public void changeCategory(String newCategory) throws Exception {
        String oldCategory = getCategory();
        xmlData = xmlData.replace("<Category>" + oldCategory + "</Category>", "<Category>" + newCategory.trim() + "</Category>");
    }
}