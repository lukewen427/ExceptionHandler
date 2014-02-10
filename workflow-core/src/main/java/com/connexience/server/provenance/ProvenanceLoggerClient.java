package com.connexience.server.provenance;

import com.connexience.server.model.logging.graph.GraphOperation;
import com.connexience.server.rmi.IProvenanceLogger;
import com.connexience.server.util.SerializationUtils;
import org.apache.log4j.Logger;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;
import org.hornetq.jms.client.HornetQConnectionFactory;
import org.pipeline.core.xmlstorage.prefs.PreferenceManager;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Client to send a JMS Message to the provenance server to log an operation without going via the server
 * User: nsjw7
 * Date: Mar 15, 2011
 * Time: 4:02:04 PM
 */
public class ProvenanceLoggerClient extends AbstractProvenanceClient implements IProvenanceLogger
{
  static Logger logger = Logger.getLogger(ProvenanceLoggerClient.class.getName());

  public ProvenanceLoggerClient()
  {
  }

  /**
   * Log a graph operation
   *
   * @param operation operation to be logged
   */
  public void log(GraphOperation operation)
  {
    try
    {
      boolean provEnabled = PreferenceManager.getSystemPropertyGroup("Provenance").booleanValue("Enabled", true);
      if (provEnabled)
      {
        sendMessage("ProvenanceQueue", operation); //ProvenanceQueue = queue name
      }
    } catch (Exception e)
    {
      logger.fatal("Cannot send message to proveneance service");
    }
  }

  /**
   * Send the message
   *
   * @param queueName queuename
   * @param message   graph operation
   * @throws Exception something went wrong
   */
  private void sendMessage(String queueName, GraphOperation message) throws Exception
  {
    try
    {
      Connection connection = null;
      try
      {
        Queue queue = HornetQJMSClient.createQueue(queueName);

        String jMSServerURLHost = PreferenceManager.getSystemPropertyGroup("Provenance").stringValue("JMSServerHost", "localhost");
        int jMSServerURLPort = PreferenceManager.getSystemPropertyGroup("Provenance").intValue("JMSServerPort", 5445);

        Map<String, Object> connectionParams = new HashMap<String, Object>();
        connectionParams.put(TransportConstants.HOST_PROP_NAME, jMSServerURLHost);
        connectionParams.put(TransportConstants.PORT_PROP_NAME, jMSServerURLPort);

        TransportConfiguration transportConfiguration = new TransportConfiguration(NettyConnectorFactory.class.getName(),
            connectionParams);

        HornetQConnectionFactory cf = HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.QUEUE_CF, transportConfiguration);

        String jMSUser = PreferenceManager.getSystemPropertyGroup("Provenance").stringValue("JMSUser", "connexience");
        String jMSPassword = PreferenceManager.getSystemPropertyGroup("Provenance").stringValue("JMSPassword", "1234");

        connection = cf.createConnection(jMSUser, jMSPassword);

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        MessageProducer producer = session.createProducer(queue);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        BytesMessage bm = session.createBytesMessage();

        byte[] data = SerializationUtils.serialize(message);
        bm.writeBytes(data);
        producer.send(bm);


      } finally
      {
        if (connection != null)
        {
          connection.close();
        }
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }


}
