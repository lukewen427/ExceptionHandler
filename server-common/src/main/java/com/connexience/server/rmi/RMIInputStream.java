/*
 * RMIInputStream.java
 */

package com.connexience.server.rmi;

import java.rmi.*;
import java.io.*;

/**
 * Provides an InputStream implementation that connects to an IRMIInputStream
 * server
 * @author hugo
 */
public class RMIInputStream extends InputStream {
    /** Current buffer of data */
    private byte[] buffer;

    /** Position within the data buffer */
    private int bufferPos = 0;

    /** RMI Data source */
    private IRMIInputStream server;

    /** Is the transfer complete */
    private boolean complete = false;

    public RMIInputStream(IRMIInputStream server) {
        this.server = server;
    }

    @Override
    public synchronized int read() throws IOException {
        if(!complete){
            if(buffer==null){
                // Need to fetch the next chunk
                int size = fetchNextChunk();
                if(size==0){
                    complete = true;
                    return -1;
                }
            }

            if(buffer!=null){
                if(!complete){
                    if(bufferPos<buffer.length){
                        int value = buffer[bufferPos];
                        bufferPos++;
                        return value;
                    } else {
                        complete = true;
                        return -1;
                    }
                    
                } else {
                    complete = true;
                    return -1;
                }
                
            } else {
                throw new IOException("No data buffer available");
            }
        } else {
            return -1;
        }
    }

    /** Fetch the next chunk of data. This method returns the size of the data buffer */
    private synchronized int fetchNextChunk() throws IOException {
        try {
            if(server!=null){
                if(!server.finished()){
                    buffer = server.next();
                    bufferPos = 0;
                    complete = false;
                    return buffer.length;
                } else {
                    complete = true;
                    server.close();
                    return 0;
                }
            } else {
                throw new Exception("Server not defined");
            }

        } catch (Exception e){
            throw new IOException("Error fetching chunk: " + e.getMessage(), e);
        }
    }
}