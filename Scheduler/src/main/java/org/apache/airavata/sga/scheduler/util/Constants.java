package org.apache.airavata.sga.scheduler.util;

import org.apache.airavata.sga.commons.scheduler.RabbitMQConstants;
import org.apache.airavata.sga.messaging.service.core.Publisher;
import org.apache.airavata.sga.messaging.service.util.RabbitMQProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ajinkya on 2/24/17.
 */
public class Constants {

    public static final Map<String, Publisher> PUBLISHER_MAP = new HashMap<>();

    public static RabbitMQProperties getProperties() {
        return new RabbitMQProperties().setBrokerUrl(RabbitMQConstants.AMQP_URI)
                .setDurable(Boolean.parseBoolean(RabbitMQConstants.IS_DURABLE_QUEUE))
                .setPrefetchCount(Integer.parseInt(RabbitMQConstants.PREFETCH_COUT)).setAutoRecoveryEnable(true)
                .setAutoAck(Boolean.parseBoolean(RabbitMQConstants.IS_AUTO_ACK))
                .setConsumerTag(RabbitMQConstants.CONSUMER_TAG)
                .setExchangeType(RabbitMQProperties.EXCHANGE_TYPE.TOPIC);
    }
}
