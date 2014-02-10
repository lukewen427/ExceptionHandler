package uk.ac.ncl.cs.esc.messaging;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

public class Mytool {
	
	public void sendCheckMessage(String cloud,String queueName) throws Exception{
		 Producer producer = new Producer(queueName,cloud);
		 System.out.println(queueName);
		 producer.sentMessagetoService();
	    producer.close();
	     Controller control=new Controller();
	     control.receiveVisitMessage(queueName);
	}
	
	public boolean receiveCheckMessage(String queueName) throws Exception{
		Wrapper app=new Wrapper(queueName);
		MessageConsumer consumer=app.run();
		Message message=consumer.receive();
		if(message instanceof TextMessage){
			TextMessage txtMessage = (TextMessage)message;
			if(txtMessage.getText().equals("true")){
				return true;
			}
			if(txtMessage.getText().equals("false")){
				return false;
			}
		}else{
			System.out.println("Invalid message received.");
			
		}
		
		return false;

	}
	
}
