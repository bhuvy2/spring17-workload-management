package org.apache.airavata.sga.messaging.service.core.impl;/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

import com.rabbitmq.client.*;
import org.apache.airavata.sga.messaging.service.core.MessagingFactory;
import org.apache.airavata.sga.messaging.service.util.Constants;
import org.apache.airavata.sga.messaging.service.util.MessageContext;
import org.apache.airavata.sga.messaging.service.core.Publisher;
import org.apache.airavata.sga.messaging.service.model.Message;
import org.apache.airavata.sga.messaging.service.util.RabbitMQProperties;
import org.apache.airavata.sga.messaging.service.util.ThriftUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RabbitMQPublisher implements Publisher {

    private static final Logger logger = LogManager.getLogger(RabbitMQPublisher.class);

    //private final Function<MessageContext, String> routingKeySupplier;
    private Connection connection;
    private final RabbitMQProperties properties;
    private Channel channel;


    public RabbitMQPublisher(RabbitMQProperties properties) {
        this.properties = properties;
        connect();
    }

    private void connect(){
        try {
            logger.info("connect() -> Connecting to server");
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setUri(properties.getBrokerUrl());
            connectionFactory.setAutomaticRecoveryEnabled(properties.isAutoRecoveryEnable());
            connection = connectionFactory.newConnection();
            connection.addShutdownListener(new ShutdownListener() {
                public void shutdownCompleted(ShutdownSignalException cause) {
                }
            });

            channel = connection.createChannel();
            //channel.queueDelete(properties.getQueueName(), true, true);
            channel.queueDeclare(properties.getQueueName(),
                    properties.isDurable(), // durable
                    false, // exclusive
                    false, // autoDelete
                    Constants.QUEUE_ARGS);// arguments

            /*
            Not required for work queue implementation
             */
            //channel.basicQos(properties.getPrefetchCount());
            //channel.exchangeDeclare(properties.getExchangeName(), properties.getExchangeType(), true);

        } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException e) {
            logger.error("connect() -> Error connecting to server.", e);
        }


    }


    @Override
    public void publish(MessageContext messageContext) throws Exception{
        logger.info("publish() -> Publishing message. Message Id : " + messageContext.getMessageId());

        byte[] body = ThriftUtils.serializeThriftObject(messageContext.getEvent());
        Message message = new Message();
        message.setEvent(body);
        message.setMessageId(messageContext.getMessageId());

        byte[] messageBody = ThriftUtils.serializeThriftObject(message);

        send(messageBody, messageContext.getPriority());
        logger.info("publish() -> Message Sent. Message Id : " + messageContext.getMessageId());

    }

    public void send(byte []message, int priority) throws Exception {
        try {
            channel.basicPublish(properties.getExchangeName(), properties.getQueueName(), MessagingFactory.getBasicProperties(priority), message);
        } catch (IOException e) {
            logger.error("send() -> Error sending message.", e);
        }
    }
}
