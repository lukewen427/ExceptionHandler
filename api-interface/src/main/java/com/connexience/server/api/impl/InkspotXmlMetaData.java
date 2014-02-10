/*
 * InkspotXmlMetaData.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;

/**
 * This class provides the default implementation of the IXmlMetaData interface
 * @author nhgh
 */
public class InkspotXmlMetaData extends InkspotSecuredObject implements IXmlMetaData {

    public InkspotXmlMetaData() {
        super();
        putProperty("metadataid", "");
    }

    public String getMetaDataId() {
        return getPropertyString("metadataid");
    }

    public void setMetadataId(String metaDataId) {
        putProperty("metadataid", metaDataId);
    }
}