/*
 * DivertingInputStream.java
 */
package com.connexience.server.util;
import java.io.*;

/**
 * This class diverts an input stream and allows other data to be inserted
 * @author hugo
 */
public class DivertingInputStream extends InputStream {
    /** Original source stream */
    private InputStream source;

    public DivertingInputStream(InputStream source) {
        this.source = source;
    }

    @Override
    public int read() throws IOException {
        return source.read();
    }
}