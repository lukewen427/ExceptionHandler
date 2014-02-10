/*
 * ObjectWrapperDataType.java 
 */

package com.connexience.server.workflow.engine.datatypes;

import org.pipeline.core.drawing.*;

/**
 * This class describes the serialized object data type
 * @author nhgh
 */
public class ObjectWrapperDataType extends DataType {
    public ObjectWrapperDataType() {
        super("object-wrapper", "Serialized object", ObjectWrapper.class);
    }
}