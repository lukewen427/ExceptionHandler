package uk.ac.ncl.cs.esc.messaging;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Wrapper implements MessageListener{
	public static String brokerURL = "tcp://localhost:61616";
	
	    private ConnectionFactory factory;
	    private Connection connection;
	    private Session session;
	    private MessageConsumer consumer;
	    private String queueName;
	public Wrapper(String queueName){
		this.queueName=queueName;
	}
	public MessageConsumer run()
    {
        try
        {
            ConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
            connection = factory.createConnection();
            connection.start(); 
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(queueName);
            consumer = session.createConsumer(destination);
     //       consumer.setMessageListener(this);
        }
        catch (Exception e)
        {
            System.out.println("Caught:" + e);
            e.printStackTrace();
        }
		return consumer;
    }
	
	/*public MessageConsumer returnMessage() throws Exception{
		ConnectionFactory factory1 = new ActiveMQConnectionFactory(brokerURL);
        connection = factory1.createConnection();
        connection.start();
        System.out.println(queueName);
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(queueName);
        consumer = session.createConsumer(destination);
   //     consumer.setMessageListener(this);
		return consumer;
	} */
	
	public void onMessage(Message message)
    {
        try
        {
            if (message instanceof TextMessage)
            {
                TextMessage txtMessage = (TextMessage)message;
                System.out.println("Message received: " + txtMessage.getText());
            }
            else
            {
                System.out.println("Invalid message received.");
            }
        }
        catch (JMSException e)
        {
            System.out.println("Caught:" + e);
            e.printStackTrace();
        }
    }
 

}
