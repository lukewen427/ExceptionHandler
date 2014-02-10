/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.connexience.server.api.test;

import com.connexience.server.api.*;
import com.connexience.server.api.impl.*;
import com.connexience.server.api.util.*;

import java.net.URL;
import java.util.*;
import java.io.*;

import org.w3c.dom.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

/**
 * @author nhgh
 */
public class TestAPI {

    public static void main(String[] args) {
        try {

            APIFactory factory = new APIFactory();
            factory.setApiClass(com.connexience.client.api.impl.HttpClientAPI.class);
            API api = factory.authenticateApplication(new URL("http://localhost:8080/APIServer"), "8a8e0e9535be2f5a0135bedfd8ba00d6", "dbff8ba5e703a92aa43fd21a898f2387");
            IUser user = api.authenticate("user1@user1.com", "user");

            System.out.println("user.getId() = " + user.getId());

          String email = "dummy17" + System.currentTimeMillis() + "@user.com";
            INewUser dummyUser = new InkspotNewUser();
            dummyUser.setFirstname("first");
            dummyUser.setSurname("last");
            dummyUser.setEmail(email);
            dummyUser.setPassword("dummy");
            IUser newUser = api.register(dummyUser);

            System.out.println("newUser.getId() = " + newUser.getId());

            IUser foundUser = api.findUserByEmail(email + "x");
            System.out.println("foundUser.getId() = " + foundUser.getId());


            InkspotTypeRegistration.register();

            user = new InkspotUser();
            user.setId("437562987465");
            user.setName("Hugo Hiden");
            user.setDescription("Test user");
            user.setFirstname("Hugo");
            user.setSurname("Hiden");

            IFolder folder = new InkspotFolder();
            folder.setId("7459782645208");
            folder.setName("Groups");
            folder.setDescription("Groups special folder");

            IDocument document = new InskpotDocument();
            document.setId("1213423");
            document.setName("smiles.csv");
            document.setDescription("Some SMILES data");

            IPermission p = new InkspotPermission();
            p.setPermissionType(IPermission.READ_PERMISSION);
            p.setPrincipalId(user.getId());

            ArrayList objects = new ArrayList();


            IPropertyList list = new InkspotPropertyList();
            IPropertyItem item1 = new InkspotPropertyItem();
            item1.setName("length");
            item1.setValue("45");
            list.add(item1);


            objects.add(user);
            objects.add(folder);
            objects.add(document);
            objects.add(p);
            objects.add(list);

            Document doc = ObjectBuilder.createDocument(objects);
            printDocument(doc);
            System.out.println(doc);

            List<IObject> recreated = ObjectBuilder.parseXml(doc);
            System.out.println(APISecurity.standardizeList(recreated));
            SequenceGenerator sequence = new SequenceGenerator();
            long sv = sequence.nextLongValue();
            System.out.println(APISecurity.signList(recreated, "8acde345face332", "jkfdhgerytpqrnfvbn", sv));

            String signature = APISecurity.signList(recreated, "8acde345face332", "jkfdhgerytpqrnfvbn", sv);
            System.out.println("Validate original list: " + APISecurity.validateList(recreated, "8acde345face332", "jkfdhgerytpqrnfvbn", sv, signature));

            ((ISecuredObject) recreated.get(0)).setName("Changed name");
            System.out.println("Validate modified list: " + APISecurity.validateList(recreated, "8acde345face332", "jkfdhgerytpqrnfvbn", sv, signature));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printDocument(Document doc) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(System.out);
            t.transform(source, result);
            System.out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
