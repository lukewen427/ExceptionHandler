/*
 * IGroup.java
 */

package com.connexience.server.api;

/**
 * This interface represents a group of users within the system
 * @author hugo
 */
public interface IGroup extends ISecuredObject {
    /** XML Document name */
    public static final String XML_NAME = "Group";

    /** Does this group need admin permission to join */
    public boolean isAdminApproveJoin();

    /** Set whether this group needs admin permission to join */
    public void setAdminApproveJoin(boolean adminApproveJoin);

    /** Can non members list group members */
    public boolean isNonMembersList();

    /** Set whether non members can list group members */
    public void setNonMembersList(boolean nonMembersList);
}