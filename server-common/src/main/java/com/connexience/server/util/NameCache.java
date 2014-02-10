/*
 * NameCache.java
 */

package com.connexience.server.util;
import com.connexience.server.ejb.util.*;
import com.connexience.server.model.security.*;
import com.connexience.server.*;
import java.util.*;
import java.io.*;

/**
 * This class caches object names and uses the object directory to look
 * up any unknown objects.
 * @author hugo
 */
public class NameCache implements Serializable {
    /** Lookup list */
    private HashMap<String,String> nameMap = new HashMap<String, String>();

    /** Age list */
    private ArrayList<String> ageMap = new ArrayList<String>();

    /** Maximum cache size */
    private int maxSize;

    public NameCache(int maxSize) {
        this.maxSize = maxSize;
    }

    /** Get the name of an object */
    public synchronized String getObjectName(Ticket ticket, String id) throws ConnexienceException {
        if(nameMap.containsKey(id)){
            return nameMap.get(id);
        } else {
            String name = EJBLocator.lookupObjectInfoBean().getObjectName(ticket, id);
            if(name!=null){
                if(nameMap.size()>=maxSize){
                    evictOldest();
                }
                nameMap.put(id, name);
                ageMap.add(id);
                return name;
            } else {
                return "UNKNOWN";
            }
        }
    }

    /** Evict the oldest entry */
    private void evictOldest(){
        int lastIndex = ageMap.size() - 1;
        if(lastIndex>0){
            String id = ageMap.get(lastIndex);
            ageMap.remove(lastIndex);
            nameMap.remove(id);
        }
    }

    /** Evict an in from the cache */
    public synchronized void evictId(String id){
        int index = ageMap.indexOf(id);
        if(index!=-1){
            ageMap.remove(index);
            nameMap.remove(id);
        }
    }
}