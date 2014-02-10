/*
 * InstrumentedInputStream.java
 */

package com.connexience.server.workflow.util;

import java.io.*;

/**
 * This class provides an InputStream that can be used to notify listeners
 * of data transfers.
 * @author hugo
 */
public class InstrumentedInputStream extends InputStream {
    /** Stream supplying data */
    private InputStream sourceStream;

    /** Total bytes transferred */
    private long bytesTransferred = 0;

    /** Listener object */
    private InstrumentedInputStreamListener listener;

    public InstrumentedInputStream(InputStream sourceStream, InstrumentedInputStreamListener listener) {
        this.sourceStream = sourceStream;
        this.listener = listener;
    }

    /** Set the listener */
    public void setInstrumentedInputStreamListener(InstrumentedInputStreamListener listener){
        this.listener = listener;
    }
    
    /** Get the total number of bytes read */
    public long getBytesTransferred(){
        return bytesTransferred;
    }

    @Override
    public int read() throws IOException {
        int bt = sourceStream.read();
        if(listener!=null && bt!=-1){
            listener.bytesRead(1);
        }

        if(bt!=-1){
            bytesTransferred++;
        }
        return bt;
    }
}