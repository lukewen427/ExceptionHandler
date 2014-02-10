/*
 * EditServiceProperties.java
 */
package com.connexience.server.service.cmd;

import org.pipeline.core.xmlstorage.prefs.*;
import org.pipeline.gui.xmlstorage.prefs.*;

/**
 * This class shows a property editor window for a service
 * @author hugo
 */
public class EditServiceProperties {
    public static void main(String[] args){
        if(args.length==1){
            String serviceName = args[0];
            PreferenceManager.loadPropertiesFromHomeDir(".inkspot", serviceName + ".xml");
            PreferenceManagerEditor editor = new PreferenceManagerEditor();
            editor.setVisible(true);
        } else {
            System.out.println("Need to specify a service name");
        }
    }
}
