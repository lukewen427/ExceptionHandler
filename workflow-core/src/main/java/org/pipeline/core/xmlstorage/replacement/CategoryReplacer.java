/*
 * CategoryReplacer.java
 */
package org.pipeline.core.xmlstorage.replacement;

import org.pipeline.core.xmlstorage.*;
import java.util.*;

/**
 * This class replaces the categories in a data store with one of the alternatives
 * contained in this class.
 * @author hugo
 */
public class CategoryReplacer {
    /** Replacement map */
    private HashMap<String,String> replacements = new HashMap<String, String>();
    
    /** Should the replacer only fix properties with a null category */
    private boolean onlyNullReplaced = false;
    
    /** Default category for properties that are not categorised */
    private String defaultCategory = "";
    
    /** Are empty categories replaced with the default value */
    private boolean automaticDefaultReplacement = false;
    
    public void addReplacement(String propertyName, String newCategory){
        replacements.put(propertyName, newCategory);
    }

    public void setOnlyNullReplaced(boolean onlyNullReplaced) {
        this.onlyNullReplaced = onlyNullReplaced;
    }

    public boolean isOnlyNullReplaced() {
        return onlyNullReplaced;
    }

    public void setAutomaticDefaultReplacement(boolean automaticDefaultReplacement) {
        this.automaticDefaultReplacement = automaticDefaultReplacement;
    }

    public boolean isAutomaticDefaultReplacement() {
        return automaticDefaultReplacement;
    }

    public void setDefaultCategory(String defaultCategory) {
        this.defaultCategory = defaultCategory;
    }

    public String getDefaultCategory() {
        return defaultCategory;
    }
    
    /** Assign a set of replacements for all of the names in a data store */
    public void addReplacementForAllProperties(XmlDataStore store, String category){
        Enumeration e = store.elements();
        XmlDataObject obj;
        
        while(e.hasMoreElements()){
            obj = (XmlDataObject)e.nextElement();
            if(obj.getCategory()==null || obj.getCategory().isEmpty()){
                addReplacement(obj.getName(), category);
            }
        }
    }
    
    public void replaceCategories(XmlDataStore store) throws XmlStorageException {
        Vector names = store.getNames();
        Enumeration e = names.elements();
        String name;
        XmlDataObject value;
        
        while(e.hasMoreElements()){
            name = e.nextElement().toString();
            value = store.get(name);
            
            if(onlyNullReplaced){
                if(value.getCategory()==null || value.getCategory().isEmpty()){
                    if(replacements.containsKey(name)){
                        value.setCategory(replacements.get(name));
                    }
                }
            } else {
                if(replacements.containsKey(name)){
                    value.setCategory(replacements.get(name));
                }                
            }
            
            // Is the category still null and are we replacing nulls with the default
            if(automaticDefaultReplacement && (value.getCategory()==null || value.getCategory().isEmpty())){
                value.setCategory(defaultCategory);
            }
        }
    }
}