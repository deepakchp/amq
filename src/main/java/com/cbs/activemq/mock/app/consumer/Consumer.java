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
package com.cbs.activemq.mock.app.consumer;

import javax.jms.JMSException;

import com.cbs.activemq.mock.app.constants.Constant;

public class Consumer {

   public static void main(final String[] args) throws Exception {
	   QueueMessageConsumer queueMsgListener = new QueueMessageConsumer(Constant.BROKER_URL, Constant.USERNAME,
               Constant.USERNAME);
       queueMsgListener.setDestinationName(Constant.QUEUE_NAME);

       try {
           queueMsgListener.run();

       } catch (JMSException e) {
           e.printStackTrace();
       }
   }
}
