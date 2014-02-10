/*
 * StreamDumper.java
 */

package com.connexience.server.workflow.util;

import java.io.*;

/**
 * This class dumps data from one stream to another
 * @author nhgh
 */
public class StreamDumper extends Thread {
    /** Source stream */
    private InputStream source;

    /** Target stream */
    private OutputStream target;

    public StreamDumper(InputStream source, OutputStream target) {
        this.source = source;
        this.target = target;
        setDaemon(true);
        start();
    }

    public void run(){
        int data;
        try {
            while((data = source.read())!=-1){
                target.write(data);
            }
            target.flush();
            
        } catch (IOException ioe){
            System.out.println("Stream dumper error: " + ioe.getMessage());
        }
    }
}