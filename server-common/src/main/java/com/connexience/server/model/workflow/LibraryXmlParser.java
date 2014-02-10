/*
 * LibraryXmlParser.java
 */

package com.connexience.server.model.workflow;

import com.connexience.server.util.*;
import org.w3c.dom.*;
/**
 * This class parses the library.xml data in an uploaded service to identify
 * library / service type
 * @author nhgh
 */
public class LibraryXmlParser {
    /** Xml data to parse */
    private String xmlData;

    /** Name of the library */
    private String libraryName;

    /** Type of library / service */
    private String libraryType;

    public LibraryXmlParser(String xmlData) {
        this.xmlData = xmlData;
    }
    
    public void parse() throws Exception {
        Document doc = XmlUtils.readXmlDocumentFromString(xmlData);

        Element top = doc.getDocumentElement();
        NodeList children = top.getChildNodes();
        Node child;

        for(int i=0;i<children.getLength();i++){
            child = children.item(i);
            if(child.getNodeName().equalsIgnoreCase("name")){
                libraryName = child.getTextContent().trim();
            } else if(child.getNodeName().equalsIgnoreCase("type")){
                libraryType = child.getTextContent().trim();
            }
        }
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryType(String libraryType) {
        this.libraryType = libraryType;
    }
   
}
