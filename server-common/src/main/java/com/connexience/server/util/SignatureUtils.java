/*
 * SignatureUtils.java
 */

package com.connexience.server.util;

import com.connexience.server.model.security.*;

import java.io.*;
import java.util.*;
import java.security.*;
import java.security.cert.*;

/**
 * This class contains utilities for signing and verifying objects and data files
 * @author nhgh
 */
public abstract class SignatureUtils {
    /** Calculate the MD5 hash of a certificate */
    public static byte[] calculateCertificateHash(X509Certificate cert) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return md.digest(cert.getEncoded());         
    }
    
    /** Are two certificates the same as each other */
    public static boolean certificatesMatch(X509Certificate cert1, X509Certificate cert2) throws Exception {
        byte[] enc1 = cert1.getEncoded();
        byte[] enc2 = cert2.getEncoded();
        
        if(enc1.length==enc2.length){
            for(int i=0;i<enc1.length;i++){
                if(enc1[i]!=enc2[i]){
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    /** Sign a String */
    public static byte[] signString(PrivateKey key, String data) throws Exception {
        Signature sig = Signature.getInstance(KeyData.sigAlg);
        sig.initSign(key);
        sig.update(data.getBytes());
        return sig.sign();
    }
    
    /** Verify the signature of a String */
    public static boolean verifyString(X509Certificate cert, String data, byte[] signatureData) throws Exception {
        Signature sig = Signature.getInstance(KeyData.sigAlg);
        sig.initVerify(cert);
        sig.update(data.getBytes());
        return sig.verify(signatureData);
    }
    
    /** Get a certificate from a byte[] array */
    public static X509Certificate loadCertificate(byte[] encodedCertificate) throws Exception {
        if(encodedCertificate!=null){
            ByteArrayInputStream stream = new ByteArrayInputStream(encodedCertificate);
            return (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(stream);        
        } else {
            return null;
        }
    }
    
    /** Sign the contents of a byte array */
    public static byte[] signByteArray(PrivateKey key, byte[] data) throws Exception {
        Signature sig = Signature.getInstance(KeyData.sigAlg);
        sig.initSign(key);
        sig.update(data);
        return sig.sign();
    }
    
    /** Sign the contents of an InputStream */
    public static byte[] signInputStream(PrivateKey key, InputStream inStream) throws Exception {
        Signature sig = Signature.getInstance(KeyData.sigAlg);
        sig.initSign(key);
        byte[] buffer = new byte[4096];
        BufferedInputStream stream = new BufferedInputStream(inStream);
        int len = stream.read(buffer);
        while(len!=-1){
            sig.update(buffer, 0, len);
            len = stream.read(buffer);
        }
        return sig.sign();        
    }
    
    /** Sign a file */
    public static byte[] signFile(PrivateKey key, File file) throws Exception {
        Signature sig = Signature.getInstance(KeyData.sigAlg);
        sig.initSign(key);
        byte[] buffer = new byte[4096];
        BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
        int len = stream.read(buffer);
        while(len!=-1){
            sig.update(buffer, 0, len);
            len = stream.read(buffer);
        }
        return sig.sign();
    }
    
    /** Verify a file */
    public static boolean verifyFile(X509Certificate cert, File file, byte[] signatureData) throws Exception {
        Signature sig = Signature.getInstance(KeyData.sigAlg);
        sig.initVerify(cert);
        byte[] buffer = new byte[4096];
        BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
        int len = stream.read(buffer);
        while(len!=-1){
            sig.update(buffer, 0, len);
            len = stream.read(buffer);
        }        
        return sig.verify(signatureData);
    }
    
    /** Get the issuer ID from a certificate. This is used when forming partnerships 
     * because the issuer ID is the database ID of the target organisation. IDs
     * are in the format: 
     * DC=Connexience, OU=ff80818112d7be750112d7bee70c0005, DNQ=Connexience Issuer
     */
    public static String getIssuerId(X509Certificate cert) throws Exception {
        StringTokenizer tokens = new StringTokenizer(cert.getIssuerX500Principal().toString(), ",");
        String token;
        while(tokens.hasMoreTokens()){
            token = tokens.nextToken().trim();
            if(token.startsWith("OU=")){
                // This is the correct part of the name
                return token.substring(3, token.length());
            }
        }
        return "";
    }
    
    /** Get a specific part of the DN String for a certificate owner */
    public static String getOwnerId(X509Certificate cert) throws Exception {
        StringTokenizer tokens = new StringTokenizer(cert.getSubjectX500Principal().toString(), ",");
        String token;
        while(tokens.hasMoreTokens()){
            token = tokens.nextToken().trim();
            if(token.startsWith("CN=")){
                return token.substring(3, token.length());
            }
        }
        return "";
    }

    /** Sign a string. This string is usually the normalised list of request parameters */
    public static String signString(String data, String key) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] signature = digest.digest(data.getBytes("UTF-8"));
        return Base64.encodeBytes(signature);
    }
}