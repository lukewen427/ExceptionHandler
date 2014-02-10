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

package org.pipeline.core.xmlstorage.xmldatatypes;
import org.pipeline.core.xmlstorage.*;
import org.w3c.dom.*;

/**
 * This class allows an integer to be stored in an XmlDataStore
 * @author  hugo
 */
public class XmlIntegerDataObject extends XmlDataObject {
    static final long serialVersionUID = 5369085125681439154L;
    
    /** Integer value of this data object */
    private int value;
    
    /** Creates a new instance of XmlIntegerDataObject */
    public XmlIntegerDataObject() {
        super();
        value = 0;
    }
    
    /** Creates a new instance of XmlIntegerDataObject */
    public XmlIntegerDataObject(String name) {
        super(name);
        value = 0;
    }
    
    /** Creates a new instance of XmlIntegerDataObject */
    public XmlIntegerDataObject(String name, int value) {
        super(name);
        this.value = value;
    }
        
    /** Add value to XML documnet */
    public void appendToXmlElement(Document xmlDocument, Element xmlElement, boolean includeDescription) throws XmlStorageException {
        try{
            Element element = getBasicXmlElement(xmlDocument, includeDescription);
            element.setAttribute("Value", Integer.toString(value));
            xmlElement.appendChild(element);
            
        } catch (Exception e){
            throw new XmlStorageException("Error adding XmlIntegerDataObject to XML document: " + e.getMessage());
        }    
    }
    
    /** Recreate from XML document */
    public void buildFromXmlElement(Element xmlElement) throws XmlStorageException {
        try{
            // Set basic properties. This will throw an Exception if the
            // element is incorrectly formed
            setBasicPropertiesFromXmlElement(xmlElement);
            
            // Extract red, green and blue values
            value = Integer.parseInt(xmlElement.getAttribute("Value"));
            
        } catch (Exception e){
            throw new XmlStorageException("Error recreating XmlIntegerDataObject: " + e.getMessage());
        }                        
    }
    
    /** Return an exact copy of this object */
    public XmlDataObject getCopy() {
        XmlDataObject copy = new XmlIntegerDataObject(getName(), value);
        copy.setDescription(getDescription());
        return copy;         
    }
    
    /** Get the data type label */
    public String getTypeLabel() {
        return "Integer";
    }
    
    /** Return object version of this data object */
    public Object getValue() {
        return new Integer(value);
    }
    
    /** Set value as an object. In this case, any Number type will do */
    public void setValue(Object objectValue) throws XmlStorageException {
        if(objectValue instanceof Number){
            value = ((Number)objectValue).intValue();
        } else {
            throw new XmlStorageException("Number type required for setValue in XmlIntegerDataObject");
        }
    }
    
    /** Get the integer value of this object */
    public int intValue(){
        return value;
    }
    
    /** Set the integer value of this object */
    public void setIntValue(int value){
       this.value = value;
    }
    
    /** Does this object support string parsing */
    public static boolean canParseString() {
        return true;
    }
    
    /** Parse a string value */
    public static XmlDataObject parseString(String stringValue) throws XmlStorageException {
        try{
            int newValue = (int)Double.parseDouble(stringValue);
            
            XmlIntegerDataObject newObject = new XmlIntegerDataObject();
            newObject.setIntValue(newValue);
            return newObject;
            
        } catch (Exception e){
            throw new XmlStorageException("Cannot parse integer: " + e.getMessage());
        }
    }
    
}
