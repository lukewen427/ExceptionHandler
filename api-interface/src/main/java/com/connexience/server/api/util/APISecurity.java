/*
 * APISecurity.java
 */

package com.connexience.server.api.util;

import com.connexience.server.api.*;

import java.util.*;
import java.security.*;

/**
 * This class provides utility methods for dealing the the security required to
 * interact with the InkspotAPI
 * @author nhgh
 */
public class APISecurity {
    /** Turn an object into a standardised form for signing */
    public static String standardizeObject(IObject object) {
        StringBuffer standardForm = new StringBuffer();
        ArrayList<String> keys = new ArrayList<String>();
        keys.addAll(object.getProperties().keySet());
        Collections.sort(keys);
        String name;
        String value;
        Object objectValue;
        standardForm.append(ObjectBuilder.getXmlNameForObject(object));
        
        Iterator<String> i = keys.iterator();
        while(i.hasNext()){
            name = i.next();
            objectValue = object.getProperties().get(name);
            if(!(objectValue instanceof IObjectList)){
                value = object.getProperties().get(name).toString();
                standardForm.append(name);
                standardForm.append(value);
            } else {
                // Inner list
                standardForm.append(name);
                standardForm.append(standardizeObject((IObjectList)objectValue));
            }

            if(object instanceof IObjectList){

                for(IObject innerObject : ((IObjectList)object).getObjects()){
                    standardForm.append(standardizeObject(innerObject));
                }
            }
        }
        return standardForm.toString();
    }

    /** Standardise a list of objects for signing */
    public static String standardizeList(List<IObject> objects){
        StringBuffer standardForm = new StringBuffer();
        Iterator<IObject> i = objects.iterator();
        while(i.hasNext()){
            standardForm.append(standardizeObject(i.next()));
        }
        return standardForm.toString();
    }

    /** Standardise a URL Connection type map */
    public static String standardiseMap(Map<String, List<String>>properties) {
        Set<String>keySet = properties.keySet();

        ArrayList<String>keys = new ArrayList<String>();
        Iterator<String> i = keySet.iterator();

        while(i.hasNext()){
            keys.add(i.next());
        }
        Collections.sort(keys);
        i = keys.iterator();
        List<String>values;
        StringBuffer valueBuilder = new StringBuffer();
        String key;

        while(i.hasNext()){
            key = i.next();
            valueBuilder.append(key);
            values = properties.get(key);
            for(int j=0;j<values.size();j++){
                valueBuilder.append(values.get(j));
            }
        }

        return valueBuilder.toString();
    }

    /** Standardise a hashtable */
    public static String standardiseHashtable(Hashtable<String,String> data){
        Set<String> keySet = data.keySet();
        ArrayList<String>keys = new ArrayList<String>();
        Iterator<String> i = keySet.iterator();

        while(i.hasNext()){
            keys.add(i.next());
        }
        Collections.sort(keys);
        i = keys.iterator();

        String value;
        StringBuffer valueBuilder = new StringBuffer();
        String key;

        while(i.hasNext()){
            key = i.next();
            valueBuilder.append(key);
            value = data.get(key);
            valueBuilder.append(value);
        }

        return valueBuilder.toString();
    }

    /** Sign a string. This string is usually the normalised list of request parameters */
    public static String signString(String data, String key) throws APISecurityException {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] signature = digest.digest(data.getBytes("UTF-8"));
            return Base64.encodeBytes(signature);
        } catch (Exception e){
            throw new APISecurityException("Error sigining data: " + e.getMessage());
        }
    }

    /** Sign a list of objects using an API ID, a Key and a sequence number */
    public static String signList(List<IObject> objects, String apiId, String key, long sequenceNumber) throws APISecurityException {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            StringBuffer data = new StringBuffer();
            data.append(apiId);
            data.append(Long.toString(sequenceNumber));
            data.append(standardizeList(objects));
            data.append(key);
            byte[] signature = digest.digest(data.toString().getBytes("UTF-8"));
            return Base64.encodeBytes(signature);
        } catch (Exception e){
            throw new APISecurityException("Error sigining data: " + e.getMessage());
        }
    }

    /** Validate a list of objects using an API ID, a key and a sequence number */
    public static boolean validateList(List<IObject> objects, String apiId, String key, long sequenceNumber, String signature) throws APISecurityException {
        try {
            String testSignature = signList(objects, apiId, key, sequenceNumber);
            if(testSignature.equals(signature)){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            throw new APISecurityException("Error validating data: " + e.getMessage());
        }
    }
}