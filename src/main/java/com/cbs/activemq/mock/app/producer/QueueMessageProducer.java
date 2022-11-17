package com.cbs.activemq.mock.app.producer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Properties;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;

import com.cbs.activemq.mock.app.constants.Constant;
import com.cbs.activemq.mock.app.conversion.FileAsByteArrayManager;

public class QueueMessageProducer {

	private String brokerUri;
    private String username;
    private String password;
 
    private ActiveMQSession session;
    private MessageProducer msgProducer;
    private ConnectionFactory connFactory;
    private Connection connection;
 
    private FileAsByteArrayManager fileManager = new FileAsByteArrayManager();
 
    public QueueMessageProducer(String brokerUri, String username, String password) {
        super();
        this.brokerUri = brokerUri;
        this.username = username;
        this.password = password;
    }
 
    private void setup() throws JMSException {
    	    	
        connFactory = new ActiveMQConnectionFactory(username, password, brokerUri);
        connection = connFactory.createConnection();
        connection.start();
        session = (ActiveMQSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }
 
    private void close() {
        try {
            if (msgProducer != null) {
                msgProducer.close();
            }
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Throwable ignore) {
        }
    }
 
    public void sendBytesMessages(String queueName) throws JMSException, IOException {
 
        setup();
        Queue queue = session.createQueue(queueName);
        msgProducer = session.createProducer(queue);
 
        File[] files = new File(Constant.FILE_INPUT_DIRECTORY).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                sendFileAsBytesMessage(file);
            }
        }
 
        close();
    }
    
    public void sendTextMessages(String queueName,String textMessage) throws JMSException, IOException {
    	 
        setup();
        Queue queue = session.createQueue(queueName);
        msgProducer = session.createProducer(queue);
 
        TextMessage message = session.createTextMessage(textMessage);

        System.out.println("Sent message: " + message.getText());

        msgProducer.send(message);
 
        close();
    }
    

 
       private void sendFileAsBytesMessage(File file) throws JMSException, IOException {
        Instant start = Instant.now();
        BytesMessage bytesMessage = session.createBytesMessage();
        bytesMessage.setStringProperty(Constant.FILE_NAME, file.getName());
        bytesMessage.writeBytes(fileManager.readfileAsBytes(file));
        msgProducer.send(bytesMessage);
        Instant end = Instant.now();
        System.out.println("sendFileAsBytesMessage for [" + file.getName() + "], took " + Duration.between(start, end));
    }
}
