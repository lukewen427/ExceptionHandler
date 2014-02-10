package com.connexience.server.util.logging;

import java.util.HashMap;

/**
 * Author: Simon
 * Date: Jan 14, 2010
 */
public class LogEventLongManager
{
  //todo: this needs turning into a singleton

  private HashMap<String, Long> users = new HashMap<String, Long>();

  private HashMap<String, Long> data = new HashMap<String, Long>();

  private HashMap<String, Long> workflows = new HashMap<String, Long>();

  private HashMap<String, Long> groups = new HashMap<String, Long>();

  private HashMap<String, Long> services = new HashMap<String, Long>();

  private long usersCounter = 0;
  private long dataCounter = 0;
  private long workflowsCounter = 0;
  private long groupsCounter = 0;
  private long servicesCounter = 0;

  public LogEventLongManager()
  {
  }

  public Long addUser(String userId)
  {
    if(users.keySet().contains(userId))
    {
      return users.get(userId);
    }
    else
    {
      users.put(userId, usersCounter);
      usersCounter ++;
      return usersCounter -1;
    }
  }

  public Long getUserIdAsInt(String userId)
  {
    return users.get(userId);
  }

  public Long addData(String dataId)
    {
      if(data.keySet().contains(dataId))
      {
        return data.get(dataId);
      }
      else
      {
        data.put(dataId, dataCounter);
        dataCounter ++;
        return dataCounter -1;
      }
    }

    public Long getDataIdAsLong(String userId)
    {
      return data.get(userId);
    }


  public Long addWorkflow(String workflowId)
     {
       if(workflows.keySet().contains(workflowId))
       {
         return workflows.get(workflowId);
       }
       else
       {
         workflows.put(workflowId, workflowsCounter);
         workflowsCounter ++;
         return workflowsCounter -1;
       }
     }

     public Long getWorkflowIdAsLong(String workflowId)
     {
       return workflows.get(workflowId);
     }



  public Long addGroup(String groupId)
     {
       if(groups.keySet().contains(groupId))
       {
         return groups.get(groupId);
       }
       else
       {
         groups.put(groupId, groupsCounter);
         groupsCounter ++;
         return groupsCounter -1;
       }
     }

     public Long getGroupIdAsLong(String groupId)
     {
       return groups.get(groupId);
     }



  public Long addService(String serviceId)
     {
       if(services.keySet().contains(serviceId))
       {
         return services.get(serviceId);
       }
       else
       {
         services.put(serviceId, servicesCounter);
         servicesCounter ++;
         return servicesCounter -1;
       }
     }

     public Long getServiceIdAsLong(String groupId)
     {
       return services.get(groupId);
     }



  

}
