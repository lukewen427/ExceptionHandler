/*
 * InstrumentedInputStreamListener.java
 */

package com.connexience.server.workflow.util;

/**
 * This interface defines an object that can listen to instrumented input stream transfer
 * events.
 * @author hugo
 */
public interface InstrumentedInputStreamListener {
    /** A certain number of bytes have been read */
    public void bytesRead(int bytes);
}