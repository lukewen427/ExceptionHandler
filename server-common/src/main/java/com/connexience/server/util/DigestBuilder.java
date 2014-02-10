/*
 * DigestBuilder.java
 */
package com.connexience.server.util;

import java.io.*;
import java.security.*;

/**
 * This class builds a digest for a file on disk
 * @author hugo
 */
public class DigestBuilder {
    /** MD5 that gets updated for each file */
    private MessageDigest md5;

    public DigestBuilder() throws NoSuchAlgorithmException {
        md5 = MessageDigest.getInstance("MD5");
    }
    
    /** Update with a file */
    public void update(File target) throws IOException {
        BufferedInputStream inStream = null;
        try {
            inStream = new BufferedInputStream(new FileInputStream(target));
            byte[] buffer = new byte[256000];
            int len;
            while((len=inStream.read(buffer))!=-1){
                md5.update(buffer, 0, len);
            }             
        } catch (IOException ioe){
            throw ioe;
        } finally {
            if(inStream!=null){try{inStream.close();}catch(Exception e){}}
        }
    }
    
    /** Return the hash */
    public String getHash(){
        return Base64.encodeBytes(md5.digest());
    }
        
    /** Calculate the MD5 of a single file */
    public static String calculateMD5(File target) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[256000];
            BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(target));
            int len;
            while((len=inStream.read(buffer))!=-1){
                md.update(buffer, 0, len);
            }
            return Base64.encodeBytes(md.digest());
            
        } catch (Exception e){
            throw new Exception("Error calculating MD5 for file: " + e.getMessage(), e);
        }
        
    }
}