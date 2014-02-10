/*
 * SharedDataFolderWrapper.java
 */

package com.connexience.server.workflow.xmlstorage;

import org.pipeline.core.xmlstorage.*;

/**
 * Wrapper class representing shared data from a user
 * @author nhgh
 */
public class SharedDataFolderWrapper extends FolderWrapper {
    /** User sharing data */
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = super.storeObject();
        store.add("UserID", userId);
        return store;
    }

    @Override
    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        super.recreateObject(store);
        userId = store.stringValue("UserID", "");
    }
}