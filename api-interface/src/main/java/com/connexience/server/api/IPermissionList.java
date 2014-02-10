/*
 * IPermissionList.java
 */

package com.connexience.server.api;

/**
 * This interface defines a container for a list of object permissions
 * @author hugo
 */
public interface IPermissionList extends IObjectList {
    /** XML document name */
    public static final String XML_NAME = "PermissionList";

    /** Get the owner of the object */
    public String getOwnerId();

    /** Set the owner of the object */
    public void setOwnerId(String ownerId);
    
    /** Get the object ID */
    public String getObjectId();

    /** Set the object ID */
    public void setObjectId(String objectId);

    /** Get the number of permissions */
    public int size();

    /** Get a permission */
    public IPermission get(int index);

    /** Add a permission */
    public void add(IPermission permission);

    /** Does a permission exist */
    public boolean permissionExists(String principalId, String permissionType);
}