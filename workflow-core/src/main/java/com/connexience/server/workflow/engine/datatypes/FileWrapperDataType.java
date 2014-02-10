/*
 * FileWrapperDataType.java
 */

package com.connexience.server.workflow.engine.datatypes;

import org.pipeline.core.drawing.*;

/**
 * This data type wraps its contents as a set of file handles that allows arbitrary
 * data to be passed between services.
 * @author nhgh
 */
public class FileWrapperDataType extends DataType {

    public FileWrapperDataType() {
        super("file-wrapper", "Wraps a collection of files as names", FileWrapper.class);
    }
}