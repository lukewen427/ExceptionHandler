/*
 * ISignatureData.java
 */

package com.connexience.server.api;

import java.security.cert.*;

/**
 * This class represents a signature as applied to an IDocumentVersion stored
 * on the server. It contains the signature byte array, the certificate
 * that can be used to verify the signature and also the ID of the user that
 * signed the file. This can be used to validate some data and also to verify that
 * the certificate contained in this signature data actually belongs to the user
 * that is claimed.
 * @author nhgh
 */
public interface ISignatureData extends IObject {
    /** XML Name of the object */
    public static final String XML_NAME = "SignatureData";
    
    /** Get the ID of the user that signed this file */
    public String getUserId();

    /** Set the ID of the user that signed this file */
    public void setUserId(String userId);

    /** Get the ID of the file that this signature refers to */
    public String getDocumentId();

    /** Set the ID of the file that this signature refers to */
    public void setDocumentId(String documentId);

    /** Get the ID of the version that this signature refers to */
    public String getVersionId();

    /** Set the ID of the version that this signature refers to */
    public void setVersionId(String versionId);

    /** Get the version number of the file */
    public int getVersionNumber();

    /** Set the version number of the file */
    public void setVersionNumber(int versionNumber);

    /** Get the signature data */
    public byte[] getSignatureData();

    /** Set the signature data */
    public void setSignatureData(byte[] signatureData);

    /** Get the certificate */
    public X509Certificate getCertificate() throws CertificateException;

    /** Set the certificate */
    public void setCertificate(X509Certificate certificate) throws CertificateEncodingException;

    /** Set the signing time as a string */
    public void setTimestamp(String timestamp);

    /** Get the signing time as a string */
    public String getTimestamp();
}