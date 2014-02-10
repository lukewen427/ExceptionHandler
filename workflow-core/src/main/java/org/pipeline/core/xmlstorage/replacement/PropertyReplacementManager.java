/*
 * PropertyReplacementManager.java
 */

package org.pipeline.core.xmlstorage.replacement;

import org.pipeline.core.xmlstorage.*;
import org.pipeline.core.xmlstorage.xmldatatypes.*;

import java.util.*;
import java.text.*;

/**
 * This class can read strings and replace instances of ${propertyname} with
 * the value of the corresponding property in an XmlDataStore
 * @author nhgh
 */
public class PropertyReplacementManager {
    /** Data store containing replaced properties */
    XmlDataStore store;

    /** Date format to use when writing dates */
    private DateFormat format = DateFormat.getDateTimeInstance();

    public PropertyReplacementManager(XmlDataStore store) {
        this.store = store;
    }

    public DateFormat getDateFormat(){
        return format;
    }

    public void setDateFormat(DateFormat format){
        this.format = format;
    }

    public String replaceProperties(String inputString) throws XmlStorageException {
        Vector names = store.getNames();
        String name;
        String valueToReplace;
        XmlDataObject dataObject;

        String result = inputString;

        for(int i=0;i<names.size();i++){
            name = names.get(i).toString().trim();
            if(name!=null && !name.equals("")){
                valueToReplace = "${" + name + "}";
                if(result.contains(valueToReplace)){
                    dataObject = store.get(name);

                    if(dataObject instanceof XmlBooleanDataObject){
                        if(((XmlBooleanDataObject)dataObject).booleanValue()==true){
                            result = result.replace(valueToReplace, "true");
                        } else {
                            result = result.replace(valueToReplace, "false");
                        }

                    } else if(dataObject instanceof XmlDateDataObject){
                        result = result.replace(valueToReplace, format.format(((XmlDateDataObject)dataObject).dateValue()));

                    } else if(dataObject instanceof XmlDoubleDataObject){
                        result = result.replace(valueToReplace, Double.toString(((XmlDoubleDataObject)dataObject).doubleValue()));

                    } else if(dataObject instanceof XmlFileDataObject){
                        result = result.replace(valueToReplace, ((XmlFileDataObject)dataObject).fileValue().getPath());

                    } else if(dataObject instanceof XmlIntegerDataObject){
                        result = result.replace(valueToReplace, Integer.toString(((XmlIntegerDataObject)dataObject).intValue()));

                    } else if(dataObject instanceof XmlLongDataObject){
                        result = result.replace(valueToReplace, Long.toString(((XmlLongDataObject)dataObject).longValue()));

                    } else if(dataObject instanceof XmlStringDataObject){
                        result = result.replace(valueToReplace, ((XmlStringDataObject)dataObject).stringValue());
                    }
                }
            }
        }
        return result;
    }
}
