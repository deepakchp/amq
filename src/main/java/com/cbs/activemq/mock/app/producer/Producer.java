/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cbs.activemq.mock.app.producer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;


import com.cbs.activemq.mock.app.constants.Constant;

/**
 * A simple JMS Queue example that creates a producer and consumer on a queue and sends then receives a message.
 */
public class Producer {

   public static void main(final String[] args) throws Exception {
	   try {
		   
		   InputStream input = QueueMessageProducer.class.getClassLoader().getResourceAsStream("config.properties");
	       Properties props = new Properties();
	       try {
				props.load(input);
		   } catch (IOException e) {
				e.printStackTrace();
		   }
		   
		   String userName = props.getProperty("amq.username");
		   String password = props.getProperty("amq.password");
		   
           QueueMessageProducer queProducer = new QueueMessageProducer(Constant.BROKER_URL,userName, password);

           System.out.println("Enter message type for transferring file:"
                   + "\n\t1 - Text Message \n\t2 - File as BytesMessage");
           try (Scanner scanIn = new Scanner(System.in)) {
               String inputFileType = scanIn.nextLine();
               switch (inputFileType) {
               case "1":{
            	   String textMsg = scanIn.nextLine();
            	   queProducer.sendTextMessages(Constant.QUEUE_NAME,textMsg);
                   break;
               }
                   
               case "2":
                   queProducer.sendBytesMessages(Constant.QUEUE_NAME);
                   break;
               default:
                   System.out.println("Wrong input");
               }
           }

       } catch (Exception e) {
           e.printStackTrace();
       }
   }
}
