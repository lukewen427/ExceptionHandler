/*
 * InkspotPermissionList.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;

/**
 * This class provides a permission list implementation.
 * @author hugo
 */
public class InkspotPermissionList extends InkspotObjectList implements IPermissionList {
    public InkspotPermissionList() {
        super();
        putProperty("objectid", "");
        putProperty("ownerid", "");
    }

    public void add(IPermission permission) {
        super.add(permission);
    }

    public String getOwnerId() {
        return getPropertyString("ownerid");
    }

    public void setOwnerId(String ownerId) {
        putProperty("ownerid", ownerId);
    }


    public IPermission get(int index) {
        return (IPermission)super.getObject(index);
    }

    public String getObjectId() {
        return getPropertyString("objectid");
    }

    public void setObjectId(String objectId) {
        putProperty("objectid", objectId);
    }

    public boolean permissionExists(String principalId, String permissionType) {
        if(principalId.equals(getOwnerId())){
            // Always works for the owner
            return true;
            
        } else {
            IPermission permission;
            for (IObject item : objects){
                permission = (IPermission)item;
                if(permission.getPrincipalId().equals(principalId) && permission.getPermissionType().equals(permissionType)){
                    return true;
                }
            }
            return false;
        }
    }
}