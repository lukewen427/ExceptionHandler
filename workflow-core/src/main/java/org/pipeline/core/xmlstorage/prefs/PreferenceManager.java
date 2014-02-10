/*
 * PreferenceManager.java
 */

package org.pipeline.core.xmlstorage.prefs;

import com.connexience.server.model.security.*;
import com.connexience.server.util.SignatureUtils;
import com.connexience.server.workflow.rpc.*;
import org.pipeline.core.xmlstorage.*;
import org.pipeline.core.xmlstorage.XmlDataStore;
import org.pipeline.core.xmlstorage.io.*;

import java.util.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;

/**
 * This class manages all of the preferences for the desktop application.
 * @author hugo
 */
public class PreferenceManager {
    /** Editable properties */
    private static Hashtable<String, XmlDataStore> editableProperties = new Hashtable<String,XmlDataStore>();

    /** Non-editable properties */
    private static Hashtable<String, XmlDataStore> systemProperties = new Hashtable<String,XmlDataStore>();

    /** Storage directory */
    private static File storageDir;

    /** Preference file name */
    private static String fileName;

    /** Private key for signing */
    private static PrivateKey privateKey = null;

    /** Certificate for validating */
    private static X509Certificate certificate = null;

    /** UserID extracted from the certificate */
    private static String certificateOwnerId = null;

    /** Get the storage directory */
    public static File getStorageDirectory(){
        return storageDir;
    }

    /** Store the last used directory */
    public static void storeLastDir(File directory){
        if(directory!=null && directory.exists() && directory.isDirectory()){
            getEditablePropertyGroup("Filesystem").add("WorkingDirectory", directory);
        }
    }

    /** Get the last used directory */
    public static File getLastDir(){
        File dir = getEditablePropertyGroup("Filesystem").fileValue("WorkingDirectory", System.getProperty("user.dir"));
        if(dir.exists()){
            return dir;
        } else {
            return new File(System.getProperty("user.home"));
        }
    }

    /** Use the RPC client to download a keystore file */
    public static boolean fetchKeystoreFromServer(String hostname, int port, File keystoreFile, String username, String password) {
        try {
            RPCClient client = new RPCClient("http://" + hostname + ":" + port + "/WorkflowServer/WorkflowServlet");
            CallObject call = new CallObject("SVCGetKeyData");
            call.getCallArguments().add("Username", username);
            call.getCallArguments().add("Password", password);
            client.setSessionIdRequired(false);
            client.syncCall(call);
            if(call.getStatus()==CallObject.CALL_EXECUTED_OK){
                byte[] keystoreData = call.getReturnArguments().byteArrayValue("KeyData");
                writeFile(keystoreData, keystoreFile);
                loadKeystoreFromByteArray(keystoreData);
                return true;
            } else {
                System.out.println("Call failed: " + call.getStatusMessage());
                return false;
            }
        } catch (Exception e){
            System.out.println("Error fetching keystore: " + e.getMessage());
            return false;
        }
    }

    /** Load the keystore from a directory */
    public static boolean loadKeystore(File directory, String keystoreName){
        storageDir = directory;
        File keystoreFile = new File(storageDir, keystoreName);
        if(keystoreFile.exists()){
            try {
                byte[] data = loadFile(keystoreFile);
                return loadKeystoreFromByteArray(data);
            } catch (Exception e){
                return false;
            }
        } else {
            return false;
        }
    }

    /** Load the keystore from a file */
    public static boolean loadKeystoreFromFile(File keystoreFile){
        return loadKeystore(keystoreFile.getParentFile(), keystoreFile.getName());
    }

    /** Load the keystore from a subdirectory of the home directory */
    public static boolean loadKeystoreFromHomeDir(String subdirectory, String keystoreName){
        File dir = new File(System.getProperty("user.home") + File.separator + subdirectory);
        return loadKeystore(dir, keystoreName);
    }

    /** Get a file from the home directory */
    public static File getFileFromHomeDir(String subdirectory, String fileName){
        return new File(new File(System.getProperty("user.home") + File.separator + subdirectory), fileName);
    }

    /** Read a single line file from the home directory */
    public static String readSingleLineFileFromHomeDir(String subdirectory, String fileName) throws IOException {
        File f = getFileFromHomeDir(subdirectory, fileName);
        if(f.exists()){
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String line = reader.readLine();
            if(line!=null){
                return line;
            } else {
                throw new IOException("File: " + fileName + " contains no data");
            }
        } else {
            throw new IOException("File: " + fileName + " not found");
        }
    }

    /** Create a single line file in the home directory */
    public static void createSingleLineFileInHomeDir(String subdirectory, String fileName, String contents) throws IOException {
        File f = getFileFromHomeDir(subdirectory, fileName);
        PrintWriter writer = new PrintWriter(f);
        writer.println(contents);
        writer.flush();
        writer.close();
    }

    /** Load the keystore from a byte array */
    public static boolean loadKeystoreFromByteArray(byte[] fileData){
        try {
            KeyData kd = new KeyData();
            kd.setKeyStoreData(fileData);
            privateKey = kd.getPrivateKey();
            certificate = kd.getCertificate();
            certificateOwnerId = SignatureUtils.getOwnerId(certificate);
            return true;

        } catch (Exception e){
            System.out.println("Error loading keystore: " + e.getMessage());
            return false;
        }
    }

    /** Get the private key */
    public static PrivateKey getPrivateKey(){
        return privateKey;
    }

    /** Get the certificate */
    public static X509Certificate getCertificate(){
        return certificate;
    }

    /** Get the id of the certificate owner */
    public static String getCertificateOwnerId(){
        return certificateOwnerId;
    }
    
    /** Load properties from a file */
    public static boolean loadPropertiesFromFile(File file){
        String path = file.getParent();
        String name = file.getName();
        return loadProperties(new File(path), name);
    }

    /** Load properties from a subdirectory in the users home directory */
    public static boolean loadPropertiesFromHomeDir(String subdirectory, String preferenceFileName) {
        File dir = new File(System.getProperty("user.home") + File.separator + subdirectory);
        return loadProperties(dir, preferenceFileName);
    }

    /** Load all of the properties from the specified file */
    public static boolean loadProperties(File storageDirectory, String preferenceFileName){
        storageDir = storageDirectory;
        fileName = preferenceFileName;
        if(!storageDir.exists()){
            if(!storageDir.mkdirs()){
                System.out.println("Cannot create property directory");
                return false;
            }
        }
        
        File propertiesFile = new File(storageDir, fileName);
        systemProperties.clear();
        editableProperties.clear();
        XmlDataStore props;

        if(propertiesFile.exists()){
            FileInputStream stream = null;
            try {
                stream = new FileInputStream(propertiesFile);
                XmlDataStoreStreamReader reader = new XmlDataStoreStreamReader(stream);
                XmlDataStore properties = reader.read();

                // Load the editable property groups
                Vector names;
                String name;

                if(properties.containsName("EditableProperties")){
                    XmlDataStore editableProps = properties.xmlDataStoreValue("EditableProperties");
                    names = editableProps.getNames();
                    for(int i=0;i<names.size();i++){
                        name = (String)names.get(i);
                        if(editableProps.containsName(name)){
                            props = editableProps.xmlDataStoreValue(name);
                            props.setAllowAddRemove(true);
                            editableProperties.put(name, props);
                        }
                    }
                }

                // Load the system property groups
                if(properties.containsName("SystemProperties")){
                    XmlDataStore systemProps = properties.xmlDataStoreValue("SystemProperties");
                    names = systemProps.getNames();
                    for(int i=0;i<names.size();i++){
                        name = (String)names.get(i);
                        if(systemProps.containsName(name)){
                            props = systemProps.xmlDataStoreValue(name);
                            props.setAllowAddRemove(true);
                            systemProperties.put(name, props);
                        }
                    }
                }

            } catch (Exception e){
                System.out.println("Cannot load properties file: " + e.getMessage());
            } finally {
                try {
                    stream.close();
                } catch (Exception e){}
            }

            return true;
        } else {
            return false;
        }
    }

    /** Save the properties to file */
    public static void saveProperties(){
        if(!storageDir.exists()){
            if(!storageDir.mkdirs()){
                System.out.println("Cannot create property directory");
                return;
            }
        }

        File propertiesFile = new File(storageDir, fileName);

        FileOutputStream stream = null;
        try {
            XmlDataStore propertyStore = getAllProperties();
            stream = new FileOutputStream(propertiesFile);
            XmlDataStoreStreamWriter writer = new XmlDataStoreStreamWriter(propertyStore);
            writer.writeToOutputStream(stream);
            
        } catch (Exception e){
            System.out.println("Error saving properties file: " + e.getMessage());
        } finally {
            try {
                stream.flush();
                stream.close();
            } catch (Exception e){}
        }
    }

    /** Print the properties to System.out */
    public static void debugPrint(){
        try {
            System.out.println();
            PrintWriter writer = new PrintWriter(System.out);
            getAllProperties().debugPrint(writer , 4);
            writer.flush();
            writer.close();
            System.out.println();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /** Get all of the properties as an XmlDataStore */
    public static XmlDataStore getAllProperties() throws Exception {
        XmlDataStore propertyStore = new XmlDataStore("Properties");

        // Save all of the editable properties
        Enumeration<String> keys = editableProperties.keys();
        XmlDataStore editableProps = new XmlDataStore("EditableProperties");
        String key;

        while(keys.hasMoreElements()){
            key = keys.nextElement();
            editableProps.add(key, editableProperties.get(key));
        }
        propertyStore.add("EditableProperties", editableProps);

        // Save all of the system properties
        keys = systemProperties.keys();
        XmlDataStore systemProps = new XmlDataStore("SystemProperties");
        while(keys.hasMoreElements()){
            key = keys.nextElement();
            systemProps.add(key, systemProperties.get(key));
        }
        propertyStore.add("SystemProperties", systemProps);
        return propertyStore;
    }

    /** Get all of the properties as a single XmlString */
    public static String getAllPropertiesAsXmlString() throws Exception {
        XmlDataStore propertyStore = getAllProperties();
        XmlDataStoreByteArrayIO writer = new XmlDataStoreByteArrayIO(propertyStore);
        return new String(writer.toByteArray());
    }

    /** Get an editable property group. This creates a new group if it doesn't
     * already exist */
    public static XmlDataStore getEditablePropertyGroup(String name){
        if(editableProperties.containsKey(name)){
            return editableProperties.get(name);
        } else {
            XmlDataStore store = new XmlDataStore(name);
            store.setAutoAddProperties(true);
            editableProperties.put(name, store);
            return store;
        }
    }
    
    /** Get an editable property group. This creates a new group if it doesn't
     * already exist */
    public static XmlDataStore getSystemPropertyGroup(String name){
        if(systemProperties.containsKey(name)){
            return systemProperties.get(name);
        } else {
            XmlDataStore store = new XmlDataStore(name);
            store.setAutoAddProperties(true);
            systemProperties.put(name, store);
            return store;
        }
    }

    /** List the system property group names */
    public static ArrayList<String> getSystemPropertyGroupNames(){
        ArrayList<String> results = new ArrayList<String>();
        Enumeration<String> i = systemProperties.keys();
        while(i.hasMoreElements()){
            results.add(i.nextElement());
        }
        return results;
    }

    /** List the editable property group names */
    public static ArrayList<String> getEditablePropertyGroupNames(){
        ArrayList<String> results = new ArrayList<String>();
        Enumeration<String> i = editableProperties.keys();
        while(i.hasMoreElements()){
            results.add(i.nextElement());
        }
        return results;
    }

    /** Load a file into a byte array */
    private static byte[] loadFile(File file) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
        byte[] buffer = new byte[1024];
        int bytes;
        while((bytes=stream.read(buffer))!=-1){
            outStream.write(buffer, 0, bytes);
        }
        outStream.flush();
        outStream.close();
        stream.close();
        return outStream.toByteArray();
    }

    /** Save a byte array into a file */
    private static void writeFile(byte[] data, File file) throws Exception {
        FileOutputStream outStream = new FileOutputStream(file);
        outStream.write(data);
        outStream.flush();
        outStream.close();
    }
}
