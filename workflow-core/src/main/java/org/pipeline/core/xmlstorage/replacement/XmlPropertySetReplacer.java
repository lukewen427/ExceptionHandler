/*
 * XmlPropertySetReplacer.java
 */

package org.pipeline.core.xmlstorage.replacement;

import org.pipeline.core.xmlstorage.*;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 * This file loads an Xml document containing a list of properties to set. This
 * can then be applied to multiple XmlDataStore objects to modify their contents.
 * The format of the file is:
 *
 * <properties>
 *  <set name="storename">
 *      <propertyname>NewValue</propertyname>
 *      <propertyname>NewValue</propertyname>
 *      ...
 *  </set>
 * 
 *
 * </properties>
 *
 * @author nhgh
 */
public class XmlPropertySetReplacer {
    /** List of parsed properties. These will be set as string values in the
     * XmlDataStore objects that are processed */
    private Hashtable<String,Properties> parsedProperties = new Hashtable<String,Properties>();

    /** Parse an XmlInput stream */
    public void parseXmlString(InputStream stream) throws XmlStorageException {
        try {
            parsedProperties.clear();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setCoalescing(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(stream);

            Element root = doc.getDocumentElement();
            NodeList children = root.getChildNodes();
            Element setElement;
            for(int i=0;i<children.getLength();i++){
                setElement = (Element)children.item(i);
                if(setElement.getNodeName().equalsIgnoreCase("set")){
                    parseSet(setElement);
                }
            }
            
        } catch (Exception e){
            throw new XmlStorageException("Error parsing XML stream: " + e.getMessage());
        }
    }

    /** Parse a set element */
    private void parseSet(Element setElement) throws Exception {
        String setName = setElement.getAttribute("name");
        Properties properties;
        if(parsedProperties.containsKey(setName)){
            properties = parsedProperties.get(setName);
        } else {
            properties = new Properties();
            parsedProperties.put(setName, properties);
        }

        NodeList children = setElement.getChildNodes();
        Element element;
        String name;
        String value;

        for(int i=0;i<children.getLength();i++){
            element = (Element)children.item(i);
            name = element.getNodeName().trim();
            value = element.getTextContent();
            properties.put(name, value);
        }
    }

    /** Replace the properties in a property store with a named set */
    public void replace(XmlDataStore store, String setName) throws XmlStorageException {
        if(parsedProperties.containsKey(setName)){
            Properties props = parsedProperties.get(setName);
            Enumeration names = props.propertyNames();
            String name;
            String value;
            String oldValue;

            XmlDataObject dataObject;

            while(names.hasMoreElements()){
                name = names.nextElement().toString();
                if(store.containsName(name)){
                    dataObject = store.get(name);
                    if(dataObject.canParseString()){
                        oldValue = store.getPropertyString(name);
                        value = props.getProperty(name, oldValue);
                        dataObject.parseString(value);
                    }
                }
            }
        }
    }
}
