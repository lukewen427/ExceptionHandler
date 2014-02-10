/*
 * IXmlMetaData.java
 */

package com.connexience.server.api;

/**
 * This class provides a wrapper for the Xml Metadata proxy object that is
 * used to store metadata within the database. Accessing this object just
 * provides information on the metadata document. Separate GETs / POSTs are
 * required in order to get to the actual data
 * @author nhgh
 */
public interface IXmlMetaData extends ISecuredObject {
    /** Name in XML object list document */
    public static final String XML_NAME = "MetaData";

    /** Get the ID of the actual metadata document */
    public String getMetaDataId();

    /** Set the ID of the actual metadata document */
    public void setMetadataId(String metaDataId);
}