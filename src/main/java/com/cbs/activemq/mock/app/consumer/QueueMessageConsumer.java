package com.cbs.activemq.mock.app.consumer;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQTextMessage;

import com.cbs.activemq.mock.app.constants.Constant;
import com.cbs.activemq.mock.app.conversion.FileAsByteArrayManager;

public class QueueMessageConsumer implements MessageListener{

	private String activeMqBrokerUri;
    private String username;
    private String password;
    private String destinationName;
    private FileAsByteArrayManager fileManager = new FileAsByteArrayManager();
	
    public QueueMessageConsumer(String activeMqBrokerUri, String username, String password) {
        super();
        this.activeMqBrokerUri = activeMqBrokerUri;
        this.username = username;
        this.password = password;
    }
    
    public void run() throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(username, password, activeMqBrokerUri);
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 
        Destination destination = session.createQueue(destinationName);
 
        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(this);
 
        System.out.println(String.format("QueueMessageConsumer Waiting for messages at queue='%s' broker='%s'",
                destinationName, this.activeMqBrokerUri));
    }
    
	@Override
	public void onMessage(Message message) {
		try {
            String filename = message.getStringProperty(Constant.FILE_NAME);
 
            Instant start = Instant.now();
 
            if (message instanceof ActiveMQTextMessage) {
                handleTextMessage((ActiveMQTextMessage) message);
            } else if (message instanceof ActiveMQBytesMessage) {
                handleBytesMessage((ActiveMQBytesMessage) message, filename);
                Instant end = Instant.now();
                System.out
                        .println("Consumed message with filename [" + filename + "], took " + Duration.between(start, end));
            } else {
                System.out.println("test");
            }
 
           
 
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}
	
	private void handleBytesMessage(ActiveMQBytesMessage bytesMessage, String filename)
            throws IOException, JMSException {
        String outputfileName = Constant.FILE_OUTPUT_BYTE_DIRECTORY + filename;
        fileManager.writeFile(bytesMessage.getContent().getData(), outputfileName);
        System.out.println("Received ActiveMQBytesMessage message");
    }
	
	 private void handleTextMessage(ActiveMQTextMessage txtMessage) throws JMSException {
	        String msg = String.format("Received ActiveMQTextMessage [ %s ]", txtMessage.getText());
	        System.out.println(msg);
	 }
	 
	 public String getDestinationName() {
	        return destinationName;
	 }
	 
	 public void setDestinationName(String destinationName) {
	        this.destinationName = destinationName;
	 }

}
