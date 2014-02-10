/*
 * InkspotGroup.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;

/**
 * This object provides a default group implementation
 * @author hugo
 */
public class InkspotGroup extends InkspotSecuredObject implements IGroup {

    public InkspotGroup() {
        super();
        putProperty("adminapprovejoin", "true");
        putProperty("nonmemberslist", "false");
    }

    public void setAdminApproveJoin(boolean adminApproveJoin) {
        if(adminApproveJoin){
            putProperty("adminapprovejoin", "true");
        } else {
            putProperty("adminapprovejoin", "false");
        }
    }

    public boolean isAdminApproveJoin() {
        if(getPropertyString("adminapprovejoin").equalsIgnoreCase("true")){
            return true;
        } else {
            return false;
        }
    }

    public void setNonMembersList(boolean nonMembersList) {
        if(nonMembersList){
            putProperty("nonmemberslist", "true");
        } else {
            putProperty("nonmemberslist", "false");
        }
    }

    public boolean isNonMembersList() {
        if(getPropertyString("nonmemberslist").equalsIgnoreCase("true")){
            return true;
        } else {
            return false;
        }
    }
}