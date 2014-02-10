/*
 * ConnexienceExceptionDialog.java
 */

package com.connexience.server.util;
import com.connexience.server.*;
import javax.swing.*;
import java.awt.*;
/**
 * This class provides a simplified way of displaying an error dialog
 * for a connexience exception.
 * @author hugo
 */
public abstract class ConnexienceExceptionDialog {
    public static void showDialog(Component parent, ConnexienceException e){
        e.printStackTrace();
        JOptionPane.showMessageDialog(parent, e.getMessage(), "Connexience Error", JOptionPane.ERROR_MESSAGE);
    }
}
