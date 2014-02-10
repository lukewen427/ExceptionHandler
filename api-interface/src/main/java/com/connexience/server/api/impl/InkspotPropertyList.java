/*
 * InkspotPropertyList.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;

import java.util.*;

/**
 * This class provides an implementation of the property list interface
 * @author nhgh
 */
public class InkspotPropertyList extends InkspotObjectList implements IPropertyList {

    public InkspotPropertyList() {
        super();
        putProperty("objectid", "");
    }

    public void add(IPropertyItem property) {
        super.add(property);
    }

    public IPropertyItem get(String name) {
        IPropertyItem property;
        for (IObject item : objects){
            property = (IPropertyItem)item;
            if(property.getName().equals(name)){
                return property;
            }
        }
        return null;
    }

    public List<IPropertyItem> getAll(String name){
        ArrayList<IPropertyItem> list = new ArrayList<IPropertyItem>();
        IPropertyItem property;
        for (IObject item : objects){
            property = (IPropertyItem)item;
            if(property.getName().equals(name)){
                list.add(property);
            }
        }
        return list;
    }

    /** Get a property by index */
    public IPropertyItem get(int index) {
        return (IPropertyItem)getObject(index);
    }

    public String getObjectId() {
        return getPropertyString("objectid");
    }

    public Iterator<IPropertyItem> properties (){
        ArrayList<IPropertyItem> list = new ArrayList<IPropertyItem>();
        for(IObject obj : objects){
            list.add((IPropertyItem)obj);
        }
        return list.iterator();
    }
    
    public void clear(){
        objects.clear();
    }
    
    public void remove(String name) {
        IPropertyItem item = get(name);
        if(item!=null){
            objects.remove(item);
        }
    }

    public void remove(IPropertyItem property) {
        super.remove(property);
    }

    public void setObjectId(String objectId) {
        putProperty("objectid", objectId);
    }

    public void set(String name, String value) {
        IPropertyItem item = get(name);
        if(item!=null){
            item.setValue(value);
        } else {
            item = new InkspotPropertyItem();
            item.setName(name);
            item.setValue(value);
            objects.add(item);
        }
    }

    @Override
    public void set(String name, Date value) {
        IPropertyItem item = get(name);
        if(item!=null){
            item.setType(IPropertyItem.PROPERTY_TYPE_DATE);
            item.setValue(Long.toString(value.getTime()));
        } else {
            item = new InkspotPropertyItem();
            item.setName(name);
            item.setType(IPropertyItem.PROPERTY_TYPE_DATE);
            item.setValue(Long.toString(value.getTime()));
            objects.add(item);
        }
    }

    @Override
    public void set(String name, boolean value) {
        IPropertyItem item = get(name);
        if(item!=null){
            item.setValue(Boolean.toString(value));
            item.setType(IPropertyItem.PROPERTY_TYPE_BOOLEAN);
        } else {
            item = new InkspotPropertyItem();
            item.setName(name);
            item.setValue(Boolean.toString(value));
            item.setType(IPropertyItem.PROPERTY_TYPE_BOOLEAN);
            objects.add(item);
        }
    }

    @Override
    public void set(String name, double value) {
        IPropertyItem item = get(name);
        if(item!=null){
            item.setValue(Double.toString(value));
            item.setType(IPropertyItem.PROPERTY_TYPE_DOUBLE);
        } else {
            item = new InkspotPropertyItem();
            item.setName(name);
            item.setType(IPropertyItem.PROPERTY_TYPE_DOUBLE);
            item.setValue(Double.toString(value));
            objects.add(item);
        }
    }

    @Override
    public void set(String name, int value) {
        IPropertyItem item = get(name);
        if(item!=null){
            item.setType(IPropertyItem.PROPERTY_TYPE_INTEGER);
            item.setValue(Integer.toString(value));
        } else {
            item = new InkspotPropertyItem();
            item.setName(name);
            item.setType(IPropertyItem.PROPERTY_TYPE_INTEGER);
            item.setValue(Integer.toString(value));
            objects.add(item);
        }    
    }

    @Override
    public boolean booleanValue(String name, boolean defaultValue) {
        if(propertyExists(name)) {
            IPropertyItem property = get(name);
            return Boolean.parseBoolean(property.stringValue());
        } else {
            return defaultValue;
        }
    }

    @Override
    public Date dateValue(String name, Date defaultValue) {
        if(propertyExists(name)) {
            IPropertyItem property = get(name);
            long time = Long.parseLong(property.stringValue());
            return new Date(time);
        } else {
            return defaultValue;
        }
    }

    @Override
    public double doubleValue(String name, double defaultValue) {
        if(propertyExists(name)) {
            IPropertyItem property = get(name);
            return Double.parseDouble(property.stringValue());
        } else {
            return defaultValue;
        }
    }

    @Override
    public int intValue(String name, int defaultValue) {
        if(propertyExists(name)) {
            IPropertyItem property = get(name);
            return Integer.parseInt(property.stringValue());
        } else {
            return defaultValue;
        }
    }

    @Override
    public String stringValue(String name, String defaultValue) {
        if(propertyExists(name)) {
            IPropertyItem property = get(name);
            return property.stringValue();
        } else {
            return defaultValue;
        }
    }

    public boolean propertyExists(String name) {
        if(get(name)!=null){
            return true;
        } else {
            return false;
        }
    }
}