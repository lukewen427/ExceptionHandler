package uk.ac.ncl.cs.esc.messaging;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Producer {
	
	public static String brokerURL = "tcp://localhost:61616";
	private ConnectionFactory factory;
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private String cloud;
 
    public Producer(String queueName,String cloud) throws JMSException
    {	
    	this.cloud=cloud;
    	 factory = new ActiveMQConnectionFactory(brokerURL);
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(queueName);
        producer = session.createProducer(destination);
    }
 
    public void sentMessagetoService() throws JMSException
    {
            System.out.println("Creating Message ");
           
            Message message = session.createTextMessage(cloud);
            producer.send(message);
     
    }
    
    public void returnMessgagetoMytool(String isAvailable) throws JMSException{
    	 System.out.println("Creating return Message ");
    	 Message message = session.createTextMessage(isAvailable);
         producer.send(message);
         System.out.println("Message sent");
    } 
 
    public void close() throws JMSException
    {
        if (connection != null)
        {
            connection.close();
        }
    }
}
