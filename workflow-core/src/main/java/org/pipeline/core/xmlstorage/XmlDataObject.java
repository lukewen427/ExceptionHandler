/* =================================================================
 *                     conneXience Data Pipeline
 * =================================================================
 *
 * Copyright 2006 Hugo Hiden and Adrian Conlin
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details. 
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.pipeline.core.xmlstorage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.*;

/**
 * Base storage class that is stored within an XmlDataStore
 * @author  hugo
 */
public abstract class XmlDataObject implements Serializable {
    /** Parameter Name */
    private String name;

    /** Description */
    private String description = null;
    
    /** Category of this object */
    private String category = null;
    
    /** Document top level tag name */
    public static final String DOCUMENT_ROOT_NODE = "PipelineDocument";
    
    /** Creates a new instance of XmlDataObject */
    public XmlDataObject() {
        name = "NoName";
    }

    /** Creates a new instance of XmlDataObject */
    public XmlDataObject(String name) {
        this.name = name;
    }
    
    /** Set the description */
    public void setDescription(String description){
        this.description = description;
    }
    
    /** Get the description */
    public String getDescription(){
        if(description!=null){
            return description;
        } else {
            return "";
        }
    }

    /** Get the property category */
    public String getCategory() {
        return category;
    }

    /** Set the property category */
    public void setCategory(String category) {
        this.category = category;
    }
    
    /** Get this parameters name */
    public String getName(){
        return name;
    }
    
    /** Set the name of this parameter */
    protected void setName(String strName){
        name = strName;
    }
    
    /** Remove the description from this data object */
    public void flushDescription(){
        this.description = null;
    }

    @Override
    public String toString() {
        if(description!=null){
            return name + "=" + getValue().toString() + ": " + description;
        } else {
            return name + "=" + getValue().toString();
        }
    }
    
    /** Set value from an Object reference. This is used by custom XmlDataObjects
     * in the XmlDataObjectFactory */
    public abstract void setValue(Object objectValue) throws XmlStorageException;
    
    /** Return the value of this object */
    public abstract Object getValue();
    
    /** Return the data type label for this object */
    public abstract String getTypeLabel();
    
    /** Write the contents of this parameter to an Element within an XML document */
    public abstract void appendToXmlElement(Document xmlDocument, Element xmlElement, boolean includeDescription) throws XmlStorageException;
    
    /** Build this parameter from an xmlElement */
    public abstract void buildFromXmlElement(Element xmlElement) throws XmlStorageException;
    
    /** Return a copy of this object */
    public abstract XmlDataObject getCopy();
    
    /** Parse a String value */
    public static XmlDataObject parseString(String value) throws XmlStorageException {
        throw new XmlStorageException("No parseString method defined");
    }
    
    /** Does this property support string parsing */
    public static boolean canParseString(){
        return false;
    }
    
    /** Append the data contained in this parameter to an element of an XML document */
    public Element getBasicXmlElement(Document xmlDocument, boolean includeDescription){
        Element element = xmlDocument.createElement("Parameter");
        element.setAttribute("Name", getName());
        element.setAttribute("Type", getTypeLabel());
        
        if(category!=null){   
            element.setAttribute("Category", getCategory());
        }
        
        if(includeDescription && description!=null && (!description.equalsIgnoreCase(""))){
            element.setAttribute("Description", description);
        }
        return element;
    }
    
    /** Set basic properties from an XmlElement */
    public void setBasicPropertiesFromXmlElement(Element xmlElement) throws XmlStorageException {
        try{
            String strType = xmlElement.getAttribute("Type");
            String strName = xmlElement.getAttribute("Name");
            String desc = xmlElement.getAttribute("Description");
            String cat = xmlElement.getAttribute("Category");
            
            // Load the description if it exists
            if(desc!=null && (!desc.equalsIgnoreCase(""))){
                description = desc;
            } else {
                description = null;
            }
            
            // Load the category if it exists */
            if(cat!=null && !cat.equalsIgnoreCase("")){
                category = cat;
            } else {
                category = null;
            }
            if(strType!=null && strName!=null){
                if(strType.equals(getTypeLabel())){
                    name = strName;
                } else {
                    throw new XmlStorageException("Data type mismatch");
                }
                
            } else {
                throw new XmlStorageException("Missing XML attributes");
            }
            
        } catch (Exception e){
            throw new XmlStorageException("Error setting basic properties from Element: " + e.getMessage());
        }
    }    
    
    /** Customise the standard renderer object to display this property */
    public void cusomizeRenderer(Object renderer){
    }
    
    /** Get the custom cell editor object */
    public Object getCustomEditor(){
        return null;
    }
}
