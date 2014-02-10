package com.connexience.server.provenance;

import org.apache.log4j.Logger;
import org.pipeline.core.xmlstorage.prefs.PreferenceManager;

import java.io.File;

/**
 * This class is an abstract client for the provenance ensuring that they have access to the properties files wherever they run
 */
public abstract class AbstractProvenanceClient
{
  static Logger logger = Logger.getLogger(AbstractProvenanceClient.class.getName());
  
  /** Initialise properties in static code */
  static {
      AbstractProvenanceClient.loadProperties();
  }
  
  protected AbstractProvenanceClient()
  {
    
  }

  /**
   * Try and load the properties file
   */
  public static void loadProperties()
  {
    File propertiesFile = new File(System.getProperty("user.home") + File.separator + ".inkspot" + File.separator + "Provenance.xml");
    boolean loaded = PreferenceManager.loadPropertiesFromFile(propertiesFile);
    if (!loaded)
    {
      PreferenceManager.getSystemPropertyGroup("Provenance").add("Enabled", false, "Should JMS Messages be sent to the provenance server");
      PreferenceManager.getSystemPropertyGroup("Provenance").add("JMSServerHost", "localhost", "Host of the JMS Server");
      PreferenceManager.getSystemPropertyGroup("Provenance").add("JMSServerPort", 5445, "Port of the JMS Server");
      PreferenceManager.getSystemPropertyGroup("Provenance").add("JMSUser", "connexience", "Username of the JMS Queue");
      PreferenceManager.getSystemPropertyGroup("Provenance").add("JMSPassword", "1234", "Password for the JMS Queue");

      PreferenceManager.saveProperties();
      logger.info("Created default provenance properties");


      // Try and load them agaiin
      loaded = PreferenceManager.loadPropertiesFromFile(propertiesFile);
      if (!loaded)
      {
        logger.fatal("Error creating properties file: " + propertiesFile.getPath());
      }
      else
      {
        logger.info("Loaded standard properties file: " + propertiesFile.getPath());
      }
    }
    else
    {
      logger.info("Loaded standard properties file: " + propertiesFile.getPath());
    }
  }


}
