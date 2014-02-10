/*
 * PermissionList.java
 */

package com.connexience.server.model.security;
import java.io.Serializable;
import java.util.*;

/**
 * This class contains a list of Permission objects and can be
 * queried for read / write / etc access
 * @author hugo
 */
public class PermissionList implements Serializable {
    /** List of permissions */
    private ArrayList permissionList = new ArrayList();
    
    /** Object that the list refers to */
    private String objectId = "";
    
    /** Creates a new instance of PermissionList */
    public PermissionList() {
    }
    
    /** Creates a new PermissionList with a list of existing permissions */
    public PermissionList(String objectId, List permissions){
        this.objectId = objectId;
        addPermissions(permissions);
    }
    
    /** Get the PermissionList array */
    public ArrayList getPermissionList(){
        return permissionList;
    }
    
    /** Set the PermissionList array */
    public void setPermissionList(ArrayList permissionList){
        this.permissionList = permissionList;
    }
    
    /** Add permissions from a list */
    public void addPermissions(List permissions){
        Permission permission;
        for(int i=0;i<permissions.size();i++){
            permission = (Permission)permissions.get(i);
            if(permission.getTargetObjectId().equals(objectId)){
                permissionList.add(permission);
            }
        }
    }
    
    /** Is there a specific permission allowing the specified action from a 
     * group of principals */
    public boolean permissionExists(String permissionType, List principalList){
        Permission permission;
        Iterator it = permissionList.iterator();
        while(it.hasNext()){
            permission = (Permission)it.next();
            if(principalList.contains(permission.getPrincipalId())){
                if(permission.getType().equals(permissionType)){
                    return true;
                }
            }
        }
        return false;
    }
    
    /** Does a specified permission exist */
    public boolean permissionExists(String permissionType){
        Iterator it = permissionList.iterator();
        while(it.hasNext()){
            if(((Permission)it.next()).getType().equals(permissionType)){
                return true;
            }
        }
        return false;
    }
    
    /** Does a specified permission exist for a single principal */
    public boolean permissionExists(String permissionType, String principalId){
        Permission permission;
        Iterator it = permissionList.iterator();
        while(it.hasNext()){
            permission = (Permission)it.next();
             if(permission.getType().equals(permissionType) && permission.getPrincipalId().equals(principalId)){
                return true;
            }
        }
        return false;
    }

    /** Get the size of this list */
    public final int getSize(){
        return permissionList.size();
    }
    
    /** Add a set of permissions */
    public void addPermissions(Permission[] p){
        for(int i=0;i<p.length;i++){
            addPermission(p[i]);
        }
    }
    
    /** Add a permission */
    public void addPermission(Permission p){
        if(p.getTargetObjectId().equals(objectId)){
            permissionList.add(p);
        }
    }
    
    /** Remove a permission */
    public void removePermission(Permission p){
        permissionList.remove(p);
    }
    
    /** Remove a set of permissions */
    public void removePermissions(Permission[] p){
        for(int i=0;i<p.length;i++){
            removePermission(p[i]);
        }
    }
    
    /** Get a specific permission */
    public Permission getPermission(int index){
        return (Permission)permissionList.get(index);
    }
    
    /** Get a list of permissions by index */
    public Permission[] getPermissions(int[] index){
        Permission[] results = new Permission[index.length];
        for(int i=0;i<index.length;i++){
            results[i] = getPermission(index[i]);
        }
        return results;
    }
    
    /** Get the object id that this list refers to */
    public String getObjectId() {
        return objectId;
    }

    /** Set the object id that this list refers to */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    
    /** Get all of the objectIds from this permission list */
    public String[] getAllPrincipalIds(){
        String[] ids = new String[permissionList.size()];
        int count = 0;
        Iterator i = permissionList.iterator();
        while(i.hasNext()){
            ids[count] = ((Permission)i.next()).getPrincipalId();
            count++;
        }
        return ids;
    }
    
    /** Create a copy for a new target object */
    public PermissionList createCopyForObject(String objectId){
        Iterator i = permissionList.iterator();
        Permission existing;
        Permission copy;
        PermissionList newList = new PermissionList();
        newList.setObjectId(objectId);
        
        while(i.hasNext()){
            existing = (Permission)i.next();
            copy = new Permission();
            copy.setTargetObjectId(objectId);
            copy.setPrincipalId(existing.getPrincipalId());
            copy.setType(existing.getType());
            newList.addPermission(copy);
        }
        return newList;
    }
}