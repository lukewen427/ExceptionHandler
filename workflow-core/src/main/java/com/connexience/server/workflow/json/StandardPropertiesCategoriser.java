/*
 * StandardPropertiesCategoriser.java
 */
package com.connexience.server.workflow.json;

import org.pipeline.core.xmlstorage.XmlDataStore;
import org.pipeline.core.xmlstorage.XmlStorageException;
import org.pipeline.core.xmlstorage.replacement.CategoryReplacer;

/**
 * This class categorise properties into standard groups
 * @author hugo
 */
public class StandardPropertiesCategoriser extends CategoryReplacer {

    public StandardPropertiesCategoriser() {
        super();        
        setAutomaticDefaultReplacement(true);
        setOnlyNullReplaced(false);
        setDefaultCategory("Block");
    }

    @Override
    public void replaceCategories(XmlDataStore store) throws XmlStorageException {
        // Some well known properties for the replacer
        addReplacement("WaitForDebugConnection", "Debugging");
        addReplacement("DebugConnectionTimeout", "Debugging");
        addReplacement("DebugMode", "Debugging");
        addReplacement("DebugPort", "Debugging");
        addReplacement("DebugPort", "Debugging");
        addReplacement("DebugPort", "Debugging");
        addReplacement("StdOutSize", "Debugging");
        
        addReplacement("AllowRetriesOnTimeout", "Engine");
        addReplacement("StreamingChunkSize", "Engine");
        addReplacement("EnforceInvocationTimeout", "Engine");
        addReplacement("InvocationTimeout", "Engine");
        addReplacement("TimeoutRetries", "Engine");
        addReplacement("persistData", "Engine");
        addReplacement("ProgressUpdateInterval", "Engine");
        
        addReplacement("Name", "Block");
        
        addReplacement("Label", "Display");
        addReplacement("Caption", "Display");        
        super.replaceCategories(store);
    }
    
    
}