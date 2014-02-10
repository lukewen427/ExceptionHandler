/*
 * IPropertyItem.java
 */

package com.connexience.server.api;

/**
 * This interface defines a single property that can be contained in a property
 * list.
 * @author nhgh
 */
public interface IPropertyItem extends IObject {
    /** XML Document name */
    public static final String XML_NAME = "Property";

    /** String property */
    public static final String PROPERTY_TYPE_STRING = "String";

    /** Boolean property */
    public static final String PROPERTY_TYPE_BOOLEAN = "Boolean";

    /** Integer property */
    public static final String PROPERTY_TYPE_INTEGER = "Integer";

    /** Date property */
    public static final String PROPERTY_TYPE_DATE = "Date";

    /** Double property */
    public static final String PROPERTY_TYPE_DOUBLE = "Double";

    /** Connexience folder reference property */
    public static final String PROPERTY_TYPE_FOLDER_REFERENCE = "Folder";

    /** Connexience DocumentRecord reference property */
    public static final String PROPERTY_TYPE_DOCUMENT_RECORD = "Document";

    /** Array of Strings */
    public static final String PROPERTY_TYPE_STRING_ARRAY = "StringList";

    /** Two column list of strings */
    public static final String PROPERTY_TYPE_2COLUMN_STRING_ARRAY = "TwoColumnList";

    /** Get the name of the property */
    public String getName();

    /** Set the name of the property */
    public void setName(String name);

    /** Get the value of the property as a string */
    public String stringValue();

    /** Set the value of the property as a string */
    public void setValue(String value);

    /** Get the property type */
    public String getType();

    /** Set the property type */
    public void setType(String type);

    /** Get the default property value */
    public String getDefaultValue();

    /** Set the default property value */
    public void setDefaultValue(String defaultValue);

    /** Get the property description */
    public String getDescription();

    /** Set the property description */
    public void setDescription(String description);

    /** Get the ID */
    public long getId();

    /** Set the ID */
    public void setId(long id);
}