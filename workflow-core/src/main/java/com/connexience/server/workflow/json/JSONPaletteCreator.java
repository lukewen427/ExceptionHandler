/*
 * JSONPaletteCreator.java
 */

package com.connexience.server.workflow.json;

import com.connexience.server.model.security.*;
import com.connexience.server.model.workflow.*;
import com.connexience.server.ejb.util.*;

import org.json.*;
import java.util.*;

/**
 * This class creates a JSON object representing the drawing tools palette.
 * @author nhgh
 */
public class JSONPaletteCreator {
    /** Security ticket for user generating the palette */
    private Ticket ticket;

    public JSONPaletteCreator(Ticket ticket) {
        this.ticket = ticket;
    }

    /** Generate the pallete */
    public JSONObject getPalette() throws Exception {
        Hashtable<String,JSONObject> paletteMap = new Hashtable<String,JSONObject>();

        JSONObject categoryJson;
        JSONObject serviceJson;
        JSONArray serviceArray;

        String category;
        DynamicWorkflowService service;

        List services = WorkflowEJBLocator.lookupWorkflowManagementBean().listDynamicWorkflowServices(ticket);
        for(int i=0;i<services.size();i++){
            service = (DynamicWorkflowService)services.get(i);
            category = service.getCategory();

            // Get or create the category and service array
            if(paletteMap.containsKey(category)){
                categoryJson = paletteMap.get(category);
                serviceArray = categoryJson.getJSONArray("serviceArray");
            } else {
                categoryJson = new JSONObject();
                serviceArray = new JSONArray();
                categoryJson.put("name", category);
                categoryJson.put("serviceArray", serviceArray);
                paletteMap.put(category, categoryJson);
            }
            
            serviceJson = new JSONObject();
            serviceJson.put("category", service.getCategory());
            serviceJson.put("name", service.getName());
            serviceJson.put("description", service.getDescription());
            serviceJson.put("serviceId", service.getId());
            serviceJson.put("creatorId", service.getCreatorId());
            serviceArray.put(serviceJson);
        }

        JSONObject paletteJson = new JSONObject();
        JSONArray categoryArray = new JSONArray();
        Enumeration<JSONObject> categories = paletteMap.elements();

        List<JSONObject> catList = Collections.list(categories);
        Collections.sort(catList, new Comparator<JSONObject>()
        {
          @Override
          public int compare(JSONObject o1, JSONObject o2)
          {
            try
            {
              return (o1.get("name").toString().compareTo(o2.get("name").toString()));
            }
            catch (JSONException e)
            {
              e.printStackTrace();
              return 0;
            }
          }
        }
        );

        int count = 0;
        for (JSONObject catJson : catList)
        {
          categoryArray.put(catJson);
          count++;
        }

        paletteJson.put("categoryArray", categoryArray);
        paletteJson.put("categoryCount", count);
        return paletteJson;
    }
}
