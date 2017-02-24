/*
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
package org.apache.airavata.sga.messaging.service.core;

import com.rabbitmq.client.AMQP;
import org.apache.airavata.sga.messaging.service.core.impl.MessageConsumer;
import org.apache.airavata.sga.messaging.service.core.impl.RabbitMQPublisher;
import org.apache.airavata.sga.messaging.service.core.impl.RabbitMQSubscriber;
import org.apache.airavata.sga.messaging.service.util.Constants;
import org.apache.airavata.sga.messaging.service.util.RabbitMQProperties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class MessagingFactory {

    private static final Logger logger = LogManager.getLogger(MessagingFactory.class);

    public static Publisher getPublisher(RabbitMQProperties rProperties) {
        logger.info("getPublisher() -> Fetching publisher. Routing Props : " + rProperties.toString());
        return new RabbitMQPublisher(rProperties);
    }

    public static Subscriber getSubscriber(final MessageHandler messageHandler, RabbitMQProperties rProperties) {

        logger.info("getSubscriber() -> Creating subscriber. Routing keys : " + rProperties.getRoutingKey() + ", Routing Props : " + rProperties.toString());

        Subscriber subscriber = new RabbitMQSubscriber(rProperties);
        subscriber.listen(((connection, channel) -> new MessageConsumer(messageHandler, connection, channel)));

        logger.debug("getSubscriber() -> Subscriber created. Routing keys : " + rProperties.getRoutingKey() + ", Routing Props : " + rProperties.toString());

        return subscriber;
    }

    public static AMQP.BasicProperties getBasicProperties(int priority){
        return new AMQP.BasicProperties("text/plain", (String)null, (Map)null, Integer.valueOf(2), Integer.valueOf(priority), (String)null, (String)null, (String)null, (String)null, (Date)null, (String)null, (String)null, (String)null, (String)null);
    }

    private static RabbitMQProperties getProperties() {
        return new RabbitMQProperties()
                .setBrokerUrl(Constants.AMQP_URI)
                .setDurable(Constants.IS_DURABLE_QUEUE)
                .setPrefetchCount(Constants.PREFETCH_COUT)
                .setAutoRecoveryEnable(true)
                .setConsumerTag("default")
                .setExchangeType(RabbitMQProperties.EXCHANGE_TYPE.TOPIC);
    }

}
