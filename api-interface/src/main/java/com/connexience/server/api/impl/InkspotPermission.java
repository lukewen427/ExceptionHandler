/*
 * InkspotPermission.java
 */

package com.connexience.server.api.impl;
import com.connexience.server.api.*;
/**
 * This class provides an implementation of a permission object
 * @author hugo
 */
public class InkspotPermission extends InkspotObject implements IPermission {

    public InkspotPermission() {
        super();
        putProperty("principalid", "");
        putProperty("permissiontype", "");
    }

    public String getPermissionType() {
        return getPropertyString("permissiontype").toString();
    }

    public String getPrincipalId() {
        return getPropertyString("principalid").toString();
    }

    public void setPermissionType(String permissionType) {
        putProperty("permissiontype", permissionType);
    }

    public void setPrincipalId(String principalId) {
        putProperty("principalid", principalId);
    }
}