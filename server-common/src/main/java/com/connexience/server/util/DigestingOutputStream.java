/*
 * DigestingOutputStream.java
 */
package com.connexience.server.util;

import java.io.*;
import java.security.*;

/**
 * This class extends an output stream to calculate the MD5 hash of everything
 * that goes through it.
 * @author hugo
 */
public class DigestingOutputStream extends FilterOutputStream {
    private MessageDigest md;
    
    public DigestingOutputStream(OutputStream out) throws NoSuchAlgorithmException {
        super(out);
        md = MessageDigest.getInstance("MD5");
    }
    
    @Override
    public void write(byte[] bytes) throws IOException {
        super.write(bytes);
        md.update(bytes);
    }

    @Override
    public void write(byte[] bytes, int i, int i1) throws IOException {
        super.write(bytes, i, i1);
        md.update(bytes, i, i1);
    }

    @Override
    public void write(int i) throws IOException {
        super.write(i);
        md.update((byte)i);
    }
    
    /** Get the hash */
    public String getHash(){
        return Base64.encodeBytes(md.digest());
    }
}