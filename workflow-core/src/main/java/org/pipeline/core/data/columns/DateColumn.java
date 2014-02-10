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

package org.pipeline.core.data.columns;
import org.pipeline.core.data.*;
import org.pipeline.core.xmlstorage.*;

import java.util.*;
import java.text.*;
import java.io.*;

/**
 * This column contains a collection of Date objects. NB Storage
 * functionality has been overridden in this column because it
 * does not do String parsing.
 * @author  hugo
 */
public class DateColumn extends Column implements Serializable {
    static final long serialVersionUID = 2893767845632154722L;
    
    /** Date display formatter */
    private static final DateFormat format = DateFormat.getInstance();
    
    /** Creates a new instance of DateColumn */
    public DateColumn() {
        super(Date.class);
    }
    
    /** Creates a new instance of DateColumn */
    public DateColumn(String name) {
        super(Date.class, name);
    }
    
    /** Create with an initial size */
    public DateColumn(int size) throws DataException {
        super(Date.class, size);
    }
    
    /** Append a date value */
    public void appendDateValue(Date value) throws DataException {
        appendObjectValue(value);
    }
    
    /** Get a date value */
    public Date dateValue(int index) throws IndexOutOfBoundsException, DataException {
        if(!isMissing(index)){
            return (Date)getObjectValue(index);
        } else {
            throw new DataException("Value is missing");
        }       
    }
    
    /** Get a date value */
    public Date getDateValue(int index) throws IndexOutOfBoundsException, DataException {
        return dateValue(index);
    }
    
    /** Return an empty copy of this column */
    public Column getEmptyCopy(){
        DateColumn column = new DateColumn();
        try {
            column.setName(getName());
        } catch (Exception e){}
        return column;
    }
    
    /** Copy an object value from this column */
    public Object copyObjectValue(int index) throws IndexOutOfBoundsException {
        if(index>=getRows()){
            throw new IndexOutOfBoundsException();
        } 
        
        if(getObjectValue(index) instanceof MissingValue){
            return new MissingValue();
        } else {
            try {
                return new Date(getDateValue(index).getTime());
            } catch (Exception e){
                return new MissingValue();
            }           
        }
    }  
    
    /** Append a text value */
    public void appendStringValue(String value) throws DataException {
        throw new DataException("Operation not supported");
    }
    
    /** Override the get string value to format the date */
    public String getStringValue(int index) throws IndexOutOfBoundsException {
        try {
            if(!isMissing(index)){
                return format.format(dateValue(index));
            } else {
                return MissingValue.MISSING_VALUE_TEXT;
            }
        } catch (Exception e){
            return MissingValue.MISSING_VALUE_TEXT;
        }
    }

    /** Append date in the standardised CxD format file */
    public void appendCxDFormatValue(String value) throws DataException {
        if(value!=null){
            try {
                long time = Long.parseLong(value);
                appendObjectValue(new Date(time));
            } catch (Exception e){
                throw new DataException("Error parsing date value: " + e.getMessage());
            }
            
        } else {
            appendObjectValue(new MissingValue());
        }
    }

    /** Return a value suitable for addition to a standardised CxD format file */
    public String getCxDFormatValue(int row) throws DataException {
        if(!isMissing(row)){
            return Long.toString(getDateValue(row).getTime());
        } else {
            return MissingValue.MISSING_VALUE_REPRESENTATION;
        }
    }
    
    
    /**
     * Recreate this object from an XmlDataStore
     */
    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        name = store.stringValue("Name", "");
        int size = store.intValue("Size", 0);
        long value;
        columnData.clear();
        
        for(int i=0;i<size;i++){
            value = store.longValue("R" + i, -1);
            if(value<0){
                // Missing
                try {
                    appendObjectValue(new MissingValue());
                } catch (Exception e){
                    throw new XmlStorageException("Error adding missing value");
                }
            } else {
                // Non-missing
                try {
                    appendObjectValue(new Date(value));
                } catch (Exception e){
                    throw new XmlStorageException("Error adding value: " + value);
                }
            }
        }
    }

    /**
     * Save this object to an XmlDataStore. This uses String values to load
     * and save data. Some subclasses may need to override this behavior.
     */
    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("Column");
        store.add("Name", getName());
        int size = columnData.size();
        store.add("Size", size);
        for(int i=0;i<size;i++){
            if(this.isMissing(i)){
                store.add("R" + i, MissingValue.MISSING_VALUE_REPRESENTATION);
            } else {
                try {
                    store.add("R" + i, getDateValue(i).getTime());
                } catch (Exception e){
                    store.add("R" + i, MissingValue.MISSING_VALUE_REPRESENTATION);
                }
            }
        }
        return store;
    }    
}
