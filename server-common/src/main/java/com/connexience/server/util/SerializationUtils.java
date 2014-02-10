/*
 * SerializationUtils.java
 */

package com.connexience.server.util;

import java.io.*;
import java.beans.*;

/**
 * This class provides basic serialization and deserialization utility methods.
 * @author hugo
 */
public class SerializationUtils {
    /** Serialize an object to a byte array */
    public static byte[] serialize(Serializable object) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(buffer);
        stream.writeObject(object);
        stream.flush();
        stream.close();
        return buffer.toByteArray();
    }
    
    /** Deserialize an object from a byte array */
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream buffer = new ByteArrayInputStream(data);
        ObjectInputStream stream = new ObjectInputStream(buffer);
        Object obj = stream.readObject();
        stream.close();
        return obj;
    }

    /** Write an object to a file */
    public static void serialize(Serializable object, File outFile) throws IOException {
        FileOutputStream fileStream = new FileOutputStream(outFile);
        ObjectOutputStream objStream = new ObjectOutputStream(fileStream);
        objStream.writeObject(object);
        objStream.flush();
        fileStream.flush();
        fileStream.getFD().sync();
        objStream.close();
        fileStream.close();
    }

    /** Read an object from file */
    public static Object deserialize(File inFile) throws ClassNotFoundException, IOException {
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream(inFile));
        Object obj = stream.readObject();
        stream.close();
        return obj;
    }

    /** Serialize an object to an XML file */
    public static void XmlSerialize(Serializable object, File outFile) throws IOException {
        FileOutputStream fileStream = new FileOutputStream(outFile);
        XMLEncoder encoder = new XMLEncoder(fileStream);
        encoder.writeObject(object);
        encoder.flush();
        fileStream.flush();
        fileStream.getFD().sync();
        encoder.close();
        fileStream.close();
    }

    /** Deserialize an object from an XML file */
    public static Object XmlDeserialize(File inFile) throws ClassNotFoundException, IOException {
        FileInputStream inStream = null;
        XMLDecoder decoder = null;
        try {
            inStream = new FileInputStream(inFile);
            decoder = new XMLDecoder(inStream);
            Object result = decoder.readObject();
            return result;
        } finally {
            if(decoder!=null){
                decoder.close();
            }
            if(inStream!=null){
                inStream.close();
            }
        }
    }
}