/*
 * XmlSerializationUtils.java
 */

package com.connexience.server.workflow.util;

import org.pipeline.core.xmlstorage.*;
import org.pipeline.core.xmlstorage.io.*;
import java.io.*;
/**
 * This class provides utility methods for serializing and deserializing objects via
 * the XmlDataStore system
 * @author hugo
 */
public class XmlSerializationUtils {
   /** Deserialize an object from an XML file */
    public static void xmlDataStoreSerialize(File inFile, XmlStorable object) throws XmlStorageException{
        FileOutputStream outStream = null;

        try {
            outStream = new FileOutputStream(inFile);
            XmlDataStore store = new XmlDataStore();
            store.add("StoredObject", object);
            XmlDataStoreStreamWriter writer = new XmlDataStoreStreamWriter(store);
            writer.writeToOutputStream(outStream);

        } catch (FileNotFoundException e){
            throw new XmlStorageException("Error opening file: " + e.getMessage());
        } finally {
            try {
                outStream.flush();
                outStream.close();
            } catch (IOException ioe){
                System.out.println("Error saving file: " + ioe.getMessage());
            }
        }
    }

   /** Deserialize an object from an XML file */
    public static Object xmlDataStoreDeserialize(File inFile) throws XmlStorageException{
        FileInputStream inStream = null;

        try {
            inStream = new FileInputStream(inFile);
            XmlDataStoreStreamReader reader = new XmlDataStoreStreamReader(inStream);
            XmlDataStore containerData = reader.read();
            return containerData.xmlStorableValue("StoredObject");

        } catch (FileNotFoundException e){
            throw new XmlStorageException("Error opening file: " + e.getMessage());
        } finally {
        }
    }
}
