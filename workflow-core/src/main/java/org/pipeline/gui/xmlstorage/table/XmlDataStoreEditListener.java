/*
 * XmlDataStoreEditListener.java
 */

package org.pipeline.gui.xmlstorage.table;


/**
 * Objects that implement this interface listen for XmlDataStore edit events
 * @author  hugo
 */
public interface XmlDataStoreEditListener {
    /** A values has been changed */
    public void dataStoreChanged(XmlDataStoreEditEvent event);
}