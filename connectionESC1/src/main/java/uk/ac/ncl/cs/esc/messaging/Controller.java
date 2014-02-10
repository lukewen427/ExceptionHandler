package uk.ac.ncl.cs.esc.messaging;

import java.util.Scanner;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Controller {
	
	public void receiveVisitMessage(String queueName) throws Exception{
	
		Wrapper app=new Wrapper(queueName);
		System.out.println(queueName);
		MessageConsumer consumer=app.run();
		Message message=consumer.receive();
		
		
		returnCheckMessage(message,queueName);
	}
	
	public void returnCheckMessage(Message message,String queueName) throws JMSException{
		
		if(message instanceof TextMessage){
			
			TextMessage txtMessage = (TextMessage)message;
			System.out.println("Choose Excetpion type");
			System.out.println("Case 1:"+txtMessage.getText()+"is not ready");
			System.out.println("Case 2: Data lost during transmission");
			System.out.println("Case 3:"+ txtMessage.getText()+"is failed");
			System.out.println("Case 4: No Exception Detected");
			Scanner s=new Scanner(System.in);
			int option=s.nextInt();
			String cloud=txtMessage.getText();
			Producer producer = new Producer("re"+queueName,cloud);
			switch(option){
			case 1:{
				producer.returnMessgagetoMytool("false");
				
			}
			case 2:{
				producer.returnMessgagetoMytool("false");
				
			}
			case 3:{
				producer.returnMessgagetoMytool("false");
				
			}
			case 4:{
			    	producer.returnMessgagetoMytool("true");
				
			 }
			}	
				
		} else{
            System.out.println("Invalid message received.");
        }
	}
}
