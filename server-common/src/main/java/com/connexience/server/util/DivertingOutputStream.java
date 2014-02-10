/*
 * DivertingOutputStream.java
 */
package com.connexience.server.util;

import java.io.*;
import java.util.*;

/**
 * This class provides an output stream pipe that can divert data to other
 * streams
 * @author hugo
 */
public class DivertingOutputStream extends OutputStream {
    /** Primary target stream */
    private OutputStream target;

    /** Extra output streams */
    private ArrayList<OutputStream> extraStreams = new ArrayList<OutputStream>();
    
    public DivertingOutputStream(OutputStream target) {
        this.target = target;
    }

    @Override
    public void write(int i) throws IOException {
        target.write(i);
        
        for(OutputStream s:extraStreams){
            try {
                s.write(i);
            } catch (Exception e){}
        }
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        target.write(bytes);
        
        for(OutputStream s:extraStreams){
            try {
                s.write(bytes);
            } catch (Exception e){}
        }
        
    }

    @Override
    public void write(byte[] bytes, int i, int i1) throws IOException {
        super.write(bytes, i, i1);
        
        for(OutputStream s:extraStreams){
            try {
                s.write(bytes, i, i1);
            } catch (Exception e){}
        }        
    }
    
    /** Add an output stream */
    public synchronized void addExtraStream(OutputStream stream){
        extraStreams.add(stream);
    }
    
    /** Remove an output stream */
    public synchronized void removeExtraStream(OutputStream stream){
        extraStreams.remove(stream);
    }
}