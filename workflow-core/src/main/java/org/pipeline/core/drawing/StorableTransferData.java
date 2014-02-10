/*
 * StorableTransferData.java
 */

package org.pipeline.core.drawing;

import java.io.*;

/**
 * This interface defines a storable piece of transfer data
 * that can be saved or loaded from InputStreams and OutputStreams.
 * @author hugo
 */
public interface StorableTransferData {
    /** Store the transfer data contents to an OutputStream */
    public void saveToOutputStream(OutputStream stream) throws DrawingException;
    
    /** Load the transfer data contents from an InputStream */
    public void loadFromInputStream(InputStream stream) throws DrawingException;
}