/*
 * HashableTransferObject.java
 */
package com.connexience.server.workflow.engine;

import org.pipeline.core.drawing.DrawingException;

/**
 * Transfer objects that implement this interface can supply an MD5 hash
 * of the data contained in them.
 * @author hugo
 */
public interface HashableTransferObject {
    /** Get the MD5 hash as a base 64 encoded string */
    public String getHash() throws DrawingException;
}