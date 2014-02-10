/*
 * X509SigningInputStream.java
 */

package com.connexience.server.util;

import com.connexience.server.model.security.*;

import java.io.*;
import java.security.*;
import java.security.cert.*;

/**
 * This class provides an InputStream that contains an X509 key to sign
 * the data that passes through it
 * @author nhgh
 */
public class X509SigningInputStream extends InputStream {
    /** Private key to use for signing */
    private PrivateKey key;

    /** Signature object */
    private Signature sig;

    /** Number of bytes read */
    private long size = 0;

    /** Source stream */
    private InputStream source;

    /** Construct with an existing InputStream */
    public X509SigningInputStream(InputStream source, PrivateKey key) throws IOException {
        try {
            this.source = source;
            sig = Signature.getInstance(KeyData.sigAlg);
            sig.initSign(key);
        } catch (Exception e){
            throw new IOException("Error initialising signature: " + e.getMessage());
        }
    }

    @Override
    public int read() throws IOException {
        int val = source.read();
        try {
            if(val!=-1){
                sig.update((byte)val);
                size = size + 1;
            }
            return val;
        } catch (Exception e){
            throw new IOException("Error updating signature: " + e.getMessage());
        }
    }

    @Override
    public int read(byte[] b) throws IOException {
        try {
            int len = source.read(b);
            if(len>0){
                sig.update(b, 0, len);
                size = size + len;
            }
            return len;
        } catch (IOException ioe){
            throw ioe;
        } catch (Exception e){
            throw new IOException("Error updating signature: " + e.getMessage());
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        try {
            int byteLen = source.read(b, off, len);
            if(byteLen>0){
                sig.update(b, off, len);
                size = size + byteLen;
            }
            return byteLen;
        } catch (IOException ioe){
            throw ioe;
        } catch (Exception e){
            throw new IOException("Error updating signature: " + e.getMessage());
        }
    }

    
    @Override
    public synchronized void reset() throws IOException {
        super.reset();
        try {
            size = 0;
            sig.initSign(key);
        } catch (Exception e){
            throw new IOException("Error resetting signature: " + e.getMessage());
        }
    }

    /** Get the signature data */
    public byte[] getSignature() throws SignatureException {
        return sig.sign();
    }

    /** Get the number of bytes read */
    public long getBytesRead(){
        return size;
    }
}