/*
 * InkspotSignatureData.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;
import com.connexience.server.api.util.*;

import java.security.cert.*;
import java.io.*;

/**
 * This class provides an implementation of the ISignatureData interface and
 * is used to transfer certificates from the server.
 * @author nhgh
 */
public class InkspotSignatureData extends InkspotObject implements ISignatureData {

    public InkspotSignatureData() {
        super();
        putProperty("encodedcertificate", "");
        putProperty("signaturedata", "");
        putProperty("documentid", "");
        putProperty("userid", "");
        putProperty("versionnumber", "0");
        putProperty("versionid", "");
        putProperty("timestamp", "");
    }

    @Override
    public String toString() {
        try {
            return getCertificate().toString();
        } catch (Exception e){
            return super.toString();
        }
    }


    /** Returns the decoded certificate */
    public X509Certificate getCertificate() throws CertificateException {
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        String encodedData = getPropertyString("encodedcertificate");
        if(encodedData!=null && !encodedData.equalsIgnoreCase("")){
            ByteArrayInputStream stream = new ByteArrayInputStream(Base64.decode(encodedData));
            Certificate cert = f.generateCertificate(stream);
            if(cert instanceof X509Certificate){
                return (X509Certificate)cert;
            } else {
                throw new CertificateException("No X509 data found");
            }
        } else {
            throw new CertificateException("No certificate data present");
        }
    }

    public String getDocumentId() {
        return getPropertyString("documentid");
    }

    /** Return the signature */
    public byte[] getSignatureData() {
        return Base64.decode(getPropertyString("signaturedata"));
    }

    public String getUserId() {
        return getPropertyString("userid");
    }

    public String getVersionId() {
        return getPropertyString("versionid");
    }

    public int getVersionNumber() {
        return Integer.parseInt(getPropertyString("versionnumber"));
    }

    /** Encodes the certificate */
    public void setCertificate(X509Certificate certificate) throws CertificateEncodingException {
        byte[] certificateData = certificate.getEncoded();
        putProperty("encodedcertificate", Base64.encodeBytes(certificateData));
    }

    public void setDocumentId(String documentId) {
        putProperty("documentid", documentId);
    }

    /** Set and encode the signature */
    public void setSignatureData(byte[] signatureData) {
        putProperty("signaturedata", Base64.encodeBytes(signatureData));
    }

    public void setUserId(String userId) {
        putProperty("userid", userId);
    }

    public void setVersionId(String versionId) {
        putProperty("versionid", versionId);
    }

    public void setVersionNumber(int versionNumber) {
        putProperty("versionnumber", Integer.toString(versionNumber));
    }

    public String getTimestamp() {
        return getPropertyString("timestamp");
    }

    public void setTimestamp(String timestamp) {
        putProperty("timestamp", timestamp);
    }


}