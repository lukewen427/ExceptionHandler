/*
 * Folder.java
 */

package com.connexience.server.model.folder;
import com.connexience.server.model.*;

/**
 * This is the base class for a folder that can store other
 * objects.
 * @author hugo
 */
public class Folder extends ServerObject {
    /** Is this folder publicly visible bypassing the security checks */
    private boolean publicVisible = false;

    /** Short name of folder */
    private String shortName = null;

    /** Creates a new instance of Folder */
    public Folder() {
        super();
    }
    
    /** Get whether this folder is publicly visible */
    public boolean isPublicVisible(){
        return publicVisible;
    }
    
    /** Set whether this folder is publicly visible */
    public void setPublicVisible(boolean publicVisible){
        this.publicVisible = publicVisible;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}