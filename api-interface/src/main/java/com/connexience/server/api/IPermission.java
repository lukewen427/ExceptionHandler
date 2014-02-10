/*
 * IPermission.java
 */

package com.connexience.server.api;

/**
 * This interface defines a permission that can be applied to an IObject
 * @author hugo
 */
public interface IPermission extends IObject {
    /** XML Name for object */
    public static final String XML_NAME = "Permission";

    /** Read permission for an object */
    public static final String READ_PERMISSION = "read";

    /** Write permission for an object */
    public static final String WRITE_PERMISSION = "write";

    /** Add permission for an object */
    public static final String ADD_PERMISSION = "add";

    /** Execute permission for an object */
    public static final String EXECUTE_PERMISSION = "execute";

    /** Get the ID of the principal */
    public String getPrincipalId();

    /** Set the ID of the principal */
    public void setPrincipalId(String principalId);

    /** Get the permission type */
    public String getPermissionType();

    /** Set the permission type */
    public void setPermissionType(String permissionType);
}