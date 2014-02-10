/*
 * XmlDataStoreEditEvent.java
 */

package org.pipeline.gui.xmlstorage.table;

import org.pipeline.core.xmlstorage.XmlDataStore;

/**
 * This event is triggered when an XmlDataStore is modified by the 
 * editor panel
 * @author  hugo
 */
public class XmlDataStoreEditEvent {
    /** Object being edited */
	private XmlDataStore store = null;
	
    /** Creates a new instance of XmlDataStoreEditEvent */
    public XmlDataStoreEditEvent(XmlDataStore store) {
        this.store = store;
    }
    
    /** Return the data store */
    public XmlDataStore getDataStore(){
        return store;
    }
}