/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.connexience.server.api.test;

import com.connexience.server.api.*;
import com.connexience.server.api.impl.*;
import com.connexience.server.api.util.*;

/**
 *
 * @author nhgh
 */
public class ListTest {
    public static void main(String[] args){
        try {
            InkspotTypeRegistration.register();

            IPropertyList properties = new InkspotPropertyList();

            IPropertyItem item1 = new InkspotPropertyItem();
            item1.setName("Length");
            item1.setValue("40");
            properties.add(item1);

            IPropertyList list2 = new InkspotPropertyList();
            IPropertyItem item2 = new InkspotPropertyItem();
            item2.setName("Size");
            item2.setValue("10");
            list2.add(item2);
            properties.add(list2);
            

            ObjectBuilder.writeObjectToStream(properties, System.out);
            
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
