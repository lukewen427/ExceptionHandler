/*
 * ExternalXmlDataObjectEditor.java
 */

package org.pipeline.gui.xmlstorage.table;

import org.pipeline.core.xmlstorage.*;
import org.pipeline.core.xmlstorage.xmldatatypes.*;
import javax.swing.*;
import java.awt.*;

/**
 * This object provides an external editor for an XmlStorableDataObject.
 * 
 * @author nhgh
 */
public abstract class ExternalXmlDataObjectEditor {
    
    /** Create an editor dialog box */
    public ExternalXmlDataObjectDialog getEditorDialog(Frame parent, XmlStorableDataObject dataObject){
        XmlDataObjectEditorPanel editor = getEditorPanel();
        editor.setObject(dataObject);
        ExternalXmlDataObjectDialog dialog = new ExternalXmlDataObjectDialog(parent, true, editor);
        return dialog;
    }
    
    /** Get an editor panel */
    public abstract XmlDataObjectEditorPanel getEditorPanel();
        
    /** Editor panel interface */
    public abstract class XmlDataObjectEditorPanel extends JPanel {

        /** Object being edited */
        private XmlStorableDataObject dataObject;
        
        /** Set the object */
        public void setObject(XmlStorableDataObject dataObject) {
            this.dataObject = dataObject;
        }
        
        /** Get the object being edited */
        public XmlStorableDataObject getObject(){
            return dataObject;
        }
        
        /** Update the edited object */
        public abstract void updateObject();

        /** Cancel the edit */
        public abstract void terminateEdit();
    }
}