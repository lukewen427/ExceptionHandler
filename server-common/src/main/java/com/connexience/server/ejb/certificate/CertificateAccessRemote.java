
package com.connexience.server.ejb.certificate;

import javax.ejb.Remote;
import com.connexience.server.*;
import com.connexience.server.model.security.*;

/**
 * This is the business interface for CertificateAccess enterprise bean.
 */
@Remote
public interface CertificateAccessRemote {
    /**
     * Get the signing certificate relating to a server object if one exists
     */
    java.security.cert.X509Certificate getCertificate(Ticket ticket, String objectId) throws ConnexienceException;

    /** Get a certificate for a User */
    java.security.cert.X509Certificate getUserCertificate(String userId) throws ConnexienceException;

    /**
     * Verify whether a certificate matches the database certificate for a server object
     */
    boolean certificateBelongsToObject(Ticket ticket, String objectId, java.security.cert.X509Certificate userCert) throws ConnexienceException;

    /**
     * Verify whether the MD5 hash of a certificate matches the database version
     */
    boolean validateCertificateMD5Hash(Ticket ticket, String objectId, byte[] md5Hash) throws ConnexienceException;

    /**
     * Get the KeyStore data for an object
     */
    byte[] getKeyStoreData(Ticket ticket, String objectId) throws ConnexienceException;
    
}
