package com.connexience.server.jms;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.jboss.logging.Logger;

import com.connexience.server.ConnexienceException;

public class JMSProperties
{
    private static final Logger logger = Logger.getLogger(JMSProperties.class);

    private static Properties properties;
    
    public static String getHostname() throws ConnexienceException
    {
        return getProperties().getProperty("hostname");
    }

    public static String getPassword() throws ConnexienceException
    {
        return getProperties().getProperty("password");
    }
    
    private synchronized static Properties getProperties() throws ConnexienceException
    {
        try
        {
            if (properties == null)
            {
                properties = new Properties();
                InputStream is = JMSProperties.class.getResourceAsStream("/META-INF/jms.properties");
                properties.load(is);
                is.close();
            }
            return properties;
        }
        catch (IOException e)
        {
            logger.warn("Failed to load /META-INF/jms.properties");
            throw new ConnexienceException("Failed to load /META-INF/jms.properties", e);
        }
    }
    
    public static String getUsername() throws ConnexienceException
    {
        return getProperties().getProperty("username");
    }
    
    public static boolean isUser() throws ConnexienceException
    {
        return getUsername() != null;
    }
}
