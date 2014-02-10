/*
 * DrawingParameterReplacer.java
 */

package com.connexience.server.workflow.engine.parameters;

import com.connexience.server.workflow.xmlstorage.*;
import com.connexience.server.api.*;

import org.pipeline.core.drawing.*;
import org.pipeline.core.drawing.model.*;
import org.pipeline.core.xmlstorage.*;
import org.pipeline.core.xmlstorage.xmldatatypes.*;

import java.util.*;

/**
 * This class takes an existing drawing and replaces selected parameters with
 * those specified in an XmlDataStore
 * @author hugo
 */
public class DrawingParameterReplacer {
    /** Drawing that will have parameters replaced */
    private DrawingModel drawing;
    
    /** API to fetch documents that have been set as external parameters */
    private API api;
     
    public DrawingParameterReplacer(DrawingModel drawing, API api) {
        this.drawing = drawing;
        this.api = api;
    }

    /** Replace parameters */
    public void replaceParameters(XmlDataStore store) throws XmlStorageException {
        Vector blockNames = store.getNames();
        String blockName;
        BlockModel block;
        XmlDataStore blockProperties;
        XmlDataStore replacementProperties;
        Vector parameterNames;
        String name;
        String className;
        XmlDataObject originalValue;
        String replacementValue;

        for(int i=0;i<blockNames.size();i++){
            blockName = blockNames.get(i).toString();
            block = drawing.getBlockByName(blockName);
            replacementProperties = store.xmlDataStoreValue(blockName);
            if(block instanceof DefaultBlockModel){
                blockProperties = ((DefaultBlockModel)block).getEditableProperties();
                parameterNames = replacementProperties.getNames();
                for(int j=0;j<parameterNames.size();j++){
                    name = parameterNames.get(j).toString();
                    originalValue = blockProperties.get(name);
                    replacementValue = replacementProperties.stringValue(name, null);
                    if(replacementValue!=null){
                        if(originalValue instanceof XmlStringDataObject){
                            ((XmlStringDataObject)originalValue).setStringValue(replacementValue);

                        } else if(originalValue instanceof XmlIntegerDataObject){
                            // Parse as a double first and then cast to int
                            try {
                                double temp = Double.parseDouble(replacementValue);
                                ((XmlIntegerDataObject)originalValue).setIntValue((int)Math.floor(temp));
                            } catch (Exception e){
                                throw new XmlStorageException("Error parsing replacement integer value: " + replacementValue);
                            }

                        } else if(originalValue instanceof XmlLongDataObject){
                            // Parse as a double first and then cast to long
                            try {
                                double temp = Double.parseDouble(replacementValue);
                                ((XmlLongDataObject)originalValue).setLongValue((long)Math.floor(temp));
                            } catch (Exception e){
                                throw new XmlStorageException("Error parsing replacement long value: " + replacementValue);
                            }

                        } else if(originalValue instanceof XmlDoubleDataObject){
                            ((XmlDoubleDataObject)originalValue).setDoubleValue(Double.parseDouble(replacementValue));

                        } else if(originalValue instanceof XmlBooleanDataObject){
                            if(replacementValue.equalsIgnoreCase("true") || replacementValue.equalsIgnoreCase("yes") || replacementValue.equalsIgnoreCase("1")){
                                ((XmlBooleanDataObject)originalValue).setBooleanValue(true);
                            } else {
                                ((XmlBooleanDataObject)originalValue).setBooleanValue(false);
                            }

                        } else if(originalValue instanceof XmlDateDataObject){
                            ((XmlDateDataObject)originalValue).setDateValue(new Date(Long.parseLong(replacementValue)));

                        } else if(originalValue instanceof XmlStorableDataObject){
                            className = ((XmlStorableDataObject)originalValue).getClassName();
                            if(className.equals("com.connexience.server.workflow.xmlstorage.DocumentRecordWrapper")){
                                // Set the ID of a document record
                                DocumentRecordWrapper doc = (DocumentRecordWrapper)((XmlStorableDataObject)originalValue).getValue();
                                
                                // Get document via api call
                                try {
                                    IDocument newDocument = api.getDocument(replacementValue);
                                    doc.copyData(newDocument);
                                    ((XmlStorableDataObject)originalValue).setValue(doc);
                                } catch (Exception e){
                                    throw new XmlStorageException("Cannot set value for property: " + originalValue.getName() + ": " + e.getMessage());
                                }
                                
                            } else if(className.equals("com.connexience.server.workflow.xmlstorage.FolderWrapper")){
                                // Set the ID of a folder
                                FolderWrapper folder = (FolderWrapper)((XmlStorableDataObject)originalValue).getValue();

                                // Get folder via api call
                                try {
                                    IFolder newFolder = api.getFolder(replacementValue);
                                    folder.copyData(newFolder);
                                    ((XmlStorableDataObject)originalValue).setValue(folder);
                                } catch(Exception e){
                                    throw new XmlStorageException("Cannot set value for folder property: " + originalValue.getName() + ": " + e.getMessage());
                                }
                                
                            }
                        }
                    }
                }
            }
        }
    }
}