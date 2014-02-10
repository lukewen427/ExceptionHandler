/*
 * SignatureOutputStream.java
 */

package com.connexience.server.util;

import java.io.*;
import java.security.*;

/**
 * This class signs all of the data in an output stream
 * @author nhgh
 */
public class SignatureOutputStream extends OutputStream {
    /** Signature object */
    private Signature sig;
    
    /** Signature data */
    private byte[] signatureData = new byte[0];
    
    /** Create with a private key */
    public SignatureOutputStream(PrivateKey key, String algorithm) throws NoSuchAlgorithmException, InvalidKeyException {
        sig = Signature.getInstance(algorithm);
        sig.initSign(key);
    }
    
    @Override
    public void write(int arg0) throws IOException {
        try {
            sig.update((byte)arg0);
        } catch (Exception e){
            throw new IOException("Signature error: " + e.getMessage());
        }
    }

    @Override
    public void write(byte[] arg0) throws IOException {
        try {
            sig.update(arg0);
        } catch (Exception e){
            throw new IOException("Signature error: " + e.getMessage());
        }
    }

    @Override
    public void write(byte[] arg0, int arg1, int arg2) throws IOException {
        try {
            sig.update(arg0, arg1, arg2);
        } catch (Exception e){
            throw new IOException("Signature error: " + e.getMessage());
        }
    }

    /** Get the signature */
    public byte[] getSignatureData(){
        return signatureData;
    }

    /** Calculate the signature on the flush method */
    @Override
    public void flush() throws IOException {
        try {
            signatureData = sig.sign();
        } catch (Exception e){
            throw new IOException("Signature error: " + e.getMessage());
        }
    }
}