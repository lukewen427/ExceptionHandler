/*
 * ObjectBuilder.java
 */

package com.connexience.server.api.util;

import com.connexience.server.api.*;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import javax.xml.parsers.*;

import java.util.*;
import java.io.*;

/**
 * This class configures implementation objects using an XML elelemt containing
 * the object data.
 * @author nhgh
 */
public class ObjectBuilder {
    /** Hashtable of object types */
    private static Hashtable<String,Class> objectTypes = new Hashtable<String,Class>();

    /** Reverse hashtable of object types */
    private static Hashtable<Class,String> objectNames = new Hashtable<Class,String>();

    /** Register an object type so that it can be automatically created from and Xml document */
    public final static void registerObject(String xmlName, Class objectClass){
        objectTypes.put(xmlName.toLowerCase(), objectClass);
        objectNames.put(objectClass, xmlName);
    }

    /** Parse an InputStream to a List of objects */
    public static List<IObject> parseInputStream(InputStream stream) throws APIParseException, APIInstantiationException {
        DocumentBuilder db = null;
        DocumentBuilderFactory dbf = null;
        Document doc = null;

        try {
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setCoalescing(true);
            db = dbf.newDocumentBuilder();
            doc = db.parse(stream);
            return parseXml(doc);
        } catch (Exception e){
            throw new APIParseException("Error parsing XmlData: " + e.getMessage());
        }
    }
    
    /** Parse an Xml document and create a collection of objects from it */
    public static List<IObject> parseXml(Document document) throws APIParseException, APIInstantiationException {
        ArrayList results = new ArrayList();
        // Get the top level element
        Element documentElement = document.getDocumentElement();

        // Get all of the child elements
        NodeList contentNodes = documentElement.getChildNodes();
        Node currentElement;
        String objectName;
        IObject object;

        for(int i=0;i<contentNodes.getLength();i++){
            currentElement = (Node)contentNodes.item(i);
            objectName = currentElement.getNodeName();
            object = instantiateObject(objectName);
            object = configureObject(object, currentElement);
            results.add(object);
        }

        return results;
    }

    /** Create a Document for a list of objects */
    public static Document createDocument(List<IObject> objects) throws APIGenerateException {
        try {
            // Create the empty document
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            documentFactory.setNamespaceAware(true);
            DocumentBuilder builder = documentFactory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element objectElement;
            Element topElement = doc.createElement("InkspotObjects");
            doc.appendChild(topElement);
            String xmlName;

            Iterator<IObject> i = objects.iterator();
            IObject obj;
            while(i.hasNext()){
                objectElement = null;
                obj = i.next();
                xmlName = getXmlNameForObject(obj);
                objectElement = doc.createElement(xmlName);
                appendObject(obj, objectElement, doc, xmlName);
                topElement.appendChild(objectElement);
            }
            return doc;

        } catch (Exception e){
            e.printStackTrace();
            throw new APIGenerateException("Error constructing Document: " + e.getMessage());
        }
    }

    /** Write a single object to an output stream. This method wraps the object in
     * a list wrapper */
    public static void writeObjectToStream(IObject object, OutputStream stream) throws APIGenerateException {
        ArrayList<IObject>list = new ArrayList<IObject>();
        list.add(object);
        writeListToStream(list, stream);
    }

    /** Write a list of documents to an output stream */
    public static void writeListToStream(List<IObject>objects, OutputStream stream) throws APIGenerateException {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            DOMSource source = new DOMSource(createDocument(objects));
            StreamResult result = new StreamResult(stream);
            t.transform(source, result);
            stream.flush();
        } catch (Exception e){
            throw new APIGenerateException("Error writing objects to output stream");
        }
    }
    
    /** Configure an API object with an XML element */
    public final static IObject configureObject(IObject object, Node xmlNode) throws APIParseException, APIInstantiationException {
        // First check that the element refers to the type of object being configured
        //if(checkXmlElementName(xmlNode, object)){
            NodeList contentNodes = xmlNode.getChildNodes();
            Node currentElement;
            String name;
            String value;
            String typeName;

            IObject innerObject;
            object.getProperties().clear();
            for(int i=0;i<contentNodes.getLength();i++){
                currentElement = (Node)contentNodes.item(i);
                name = currentElement.getNodeName().trim();
                if(!name.equals("contents")){
                    Node listAttr = currentElement.getAttributes().getNamedItem("list");
                    Node typeAttr = currentElement.getAttributes().getNamedItem("type");
                    if(listAttr==null){
                        value = currentElement.getTextContent().trim();
                        object.getProperties().put(name, value);
                    } else {
                        typeName = typeAttr.getTextContent();
                        // Instantiate the inner object
                        innerObject = instantiateObject(typeName);
                        innerObject = configureObject(innerObject, currentElement);
                        object.getProperties().put(name, innerObject);
                    }
                }
            }

            // Search for a content node if this is an IObjectList
            if(object instanceof IObjectList){
                contentNodes = xmlNode.getChildNodes();
                Node contentNode = null;
                for(int i=0;i<contentNodes.getLength();i++){
                    if(((Node)contentNodes.item(i)).getNodeName().trim().equals("contents")){
                        contentNode = (Node)contentNodes.item(i);
                    }
                }
                if(contentNode!=null){
                    NodeList innerList = contentNode.getChildNodes();
                    for(int i=0;i<innerList.getLength();i++){
                        currentElement = (Node)innerList.item(i);
                        name = currentElement.getNodeName();
                        innerObject = instantiateObject(name);
                        innerObject = configureObject(innerObject, currentElement);
                        ((IObjectList)object).add(innerObject);
                    }
                } else {
                    throw new APIParseException("ObjectList did not have any contents");
                }
            }
            
        //} else {
        //    throw new APIParseException("Invalid object to XML mapping");
        //}
        
        return object;
    }

    /** Get the element name for an object */
    public static String getXmlNameForObject(IObject object) {
        return objectNames.get(object.getClass());
    }

    /** Instantiate an object using an Xml node name */
    public static IObject instantiateObject(String objectName) throws APIInstantiationException {
        try {
            if(objectTypes.containsKey(objectName.toLowerCase())){
                return (IObject)objectTypes.get(objectName.toLowerCase()).newInstance();
            } else {
                throw new Exception("Object type is not registered");
            }
        } catch (Exception e){
            throw new APIInstantiationException("Error instantiating object: " + objectName + ": " + e.getMessage());
        }
    }

    /** Check to see if the XML name matches the object type */
    private static boolean checkXmlElementName(Node xmlNode, IObject object){
        String elementName = xmlNode.getNodeName();
        String requiredName = getXmlNameForObject(object);
        if(requiredName.equalsIgnoreCase(elementName)){
            return true;
        } else {
            return false;
        }
    }

    /** Write all of the properties for an object to an Xml node */
    private static void appendObject(IObject object, Element container, Document doc, String xmlName){
        Hashtable<String,Object> properties = object.getProperties();
        String name;
        Object value;
        Element child;

        Enumeration<String> keys = properties.keys();
        while(keys.hasMoreElements()){
            name = keys.nextElement();
            value = properties.get(name);

            child = doc.createElement(name);
            if(value!=null){
                if(!(value instanceof IObjectList)){
                    // Standard property
                    child.setTextContent(value.toString());

                } else {
                    // One of the properties is a list
                    child.setAttribute("list", "true");
                    child.setAttribute("type", getXmlNameForObject((IObjectList)value));
                    appendObject((IObjectList)value, child, doc, getXmlNameForObject((IObjectList)value));
                }

            } else {
                child.setTextContent("");
            }
            container.appendChild(child);
        }

        // Append list contents if this is an IObjectList
        if(object instanceof IObjectList){
            IObjectList objList = (IObjectList)object;
            Element listElement = doc.createElement("contents");
            Element objectElement;
            String innerXmlName;
            for(IObject obj : objList.getObjects()){
                innerXmlName = getXmlNameForObject(obj);
                objectElement = doc.createElement(innerXmlName);
                appendObject(obj, objectElement, doc, innerXmlName);
                listElement.appendChild(objectElement);
            }
            container.appendChild(listElement);
        }
    }
    
    
    public static boolean areObjectsEqual(ISecuredObject object1, ISecuredObject object2){
        if(object1.getClass().equals(object2.getClass())){
            if(object1.getId().equals(object2.getId())){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}