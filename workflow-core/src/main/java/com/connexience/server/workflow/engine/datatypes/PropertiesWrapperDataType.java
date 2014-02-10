/*
 * PropertiesWrapperDataType.java
 */

package com.connexience.server.workflow.engine.datatypes;
import org.pipeline.core.drawing.*;

/**
 * This data type defines a set of properties that can be passed between
 * blocks.
 * @author hugo
 */
public class PropertiesWrapperDataType extends DataType {

    public PropertiesWrapperDataType() {
        super("properties-wrapper", "Set of name-value properties", PropertiesWrapper.class);
    }
}
