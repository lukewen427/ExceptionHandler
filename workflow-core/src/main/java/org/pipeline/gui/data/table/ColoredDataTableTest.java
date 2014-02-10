/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pipeline.gui.data.table;
import java.awt.*;
import javax.swing.*;
import org.pipeline.core.data.*;
import org.pipeline.core.data.io.*;
import java.io.*;
/**
 *
 * @author hugo
 */
public class ColoredDataTableTest extends JFrame {

    public ColoredDataTableTest() {
        try {
            setLayout(new BorderLayout());
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            DelimitedTextDataImporter importer = new DelimitedTextDataImporter();
            importer.setForceTextImport(true);
            Data data = importer.importFile(new File("/Users/hugo/Desktop/weetabix.csv"));
            ColoredDataTable table = new ColoredDataTable();
            getContentPane().add(table, BorderLayout.CENTER);
            table.setData(data);
            setSize(800, 600);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        ColoredDataTableTest test = new ColoredDataTableTest();
        test.setVisible(true);
    }

}
