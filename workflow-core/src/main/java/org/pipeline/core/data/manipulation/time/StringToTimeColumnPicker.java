/*
 * StringToTimeColumnPicker.java
 *
 * Created on 25 June 2006, 13:42
 */

package org.pipeline.core.data.manipulation.time;
import org.pipeline.core.data.*;
import org.pipeline.core.data.manipulation.*;
import org.pipeline.core.data.columns.*;
import org.pipeline.core.xmlstorage.*;


import java.util.*;
import java.text.*;

/**
 * This class provides a column picker that converts a String column into
 * a Date column
 * @author hugo
 */
public class StringToTimeColumnPicker extends ColumnPicker {
    /** Text of the date format to use for parsing text columns */
    String dateFormat = "";
    
    /** Creates a new instance of StringToTimeColumnPicker */
    public StringToTimeColumnPicker() {
        super();
        setLimitColumnTypes(true);
        addSupportedColumnClass(StringColumn.class);        
        setCopyData(false);
    }
    
    /** Pick and convert the column */
    public Column pickColumn(Data data) throws IndexOutOfBoundsException, DataException {
        Column c = super.pickColumn(data);
        SimpleDateFormat format = null;
        try {
            format = new SimpleDateFormat(dateFormat);
        } catch (Exception e){
            throw new DataException("Invalid data format: " + e.getLocalizedMessage());
        }
        
        if(c instanceof StringColumn) {
            DateColumn newColumn = new DateColumn(c.getName());
            int size = c.getRows();
            String textValue;
            Date dateValue;
            
            for(int i=0;i<size;i++){
                try {
                    if(!c.isMissing(i)){
                        textValue = c.getStringValue(i);
                        dateValue = format.parse(textValue);
                        newColumn.appendDateValue(dateValue);
                    } else {
                        newColumn.appendObjectValue(new MissingValue());
                    }
                    
                } catch (Exception e){
                    newColumn.appendObjectValue(new MissingValue());
                }
            }
            
            return newColumn;
            
        } else {
            throw new DataException("Date parsing can only be applied to text columns");
        }
        
    }
    
    /** Get the date format text */
    public String getDateFormat(){
        return dateFormat;
    }
    
    /** Set the date format text */
    public void setDateFormat(String dateFormat){
        this.dateFormat = dateFormat;
    }
    
    /** Recreate from storage */
    public void recreateObject(XmlDataStore xmlDataStore) throws XmlStorageException {
        super.recreateObject(xmlDataStore);
        dateFormat = xmlDataStore.stringValue("DateFormat", "");
    }
    
    /** Save to storage */
    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = super.storeObject();
        store.add("DateFormat", dateFormat);
        return store;
    }        
}
