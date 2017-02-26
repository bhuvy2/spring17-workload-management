package org.apache.airavata.sga.scheduler.util;

import org.apache.airavata.sga.commons.scheduler.RabbitMQConstants;
import org.apache.airavata.sga.messaging.service.core.Publisher;
import org.apache.airavata.sga.messaging.service.util.RabbitMQProperties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Ajinkya on 2/24/17.
 */
public class Constants {

    private static final Logger logger = LogManager.getLogger(Constants.class);

    public static final String SCHEDULER_PROP_FILE = "scheduler.properties";
    public static final String SCHEDULER_MESSAGE_EXCHANGE_NAME = "scheduler.message.exchange.name";
    public static final String SCHEDULER_MESSAGE_QUEUE = "scheduler.message.queue";
    public static final String SCHEDULER_MESSAGE_ROUTING_KEY = "scheduler.message.routing.key";

    public static final String ORCHESTRATOR_RESPONSE_EXCHANGE_NAME = "orchestrator.response.exchange.name";
    public static final String ORCHESTRATOR_RESPONSE_QUEUE = "orchestrator.response.queue";
    public static final String ORCHESTRATOR_RESPONSE_ROUTING_KEY = "orchestrator.response.routing.key";

    public static final String SCHEDULER_MESSAGE_BUFFER_SIZE_CONSTANT = "scheduler.message.buffer.size.constant";
    public static final String SCHEDULER_MESSAGE_BUFFER_TRACKING_SIZE = "scheduler.message.buffer.tracking.size";
    public static final String SCHEDULER_MESSAGE_BUFFER_RATE_MULTIPLIER = "scheduler.message.buffer.rate.multiplier";

    public static final Map<String, Publisher> PUBLISHER_MAP = new HashMap<>();
    public static final RabbitMQProperties SCHEDULER_RABBITMQ_PROPERTIES;
    public static final RabbitMQProperties SCHEDULER_MESSAGE_RABBITMQ_PROPERTIES;
    public static final RabbitMQProperties ORCHESTRATOR_RESPONSE_RABBITMQ_PROPERTIES;
    public static final Properties properties;

    static{
        properties = new Properties();
        try {
            properties.load(Constants.class.getClassLoader().getResourceAsStream(SCHEDULER_PROP_FILE));
        } catch (IOException ex) {
            logger.error("Error loading properties: " + ex.getMessage(), ex);
        }
        SCHEDULER_MESSAGE_RABBITMQ_PROPERTIES = getSchedulerMessageRabbitMQProperties();
        SCHEDULER_RABBITMQ_PROPERTIES = getSchedulerRabbitMQProperties();
        ORCHESTRATOR_RESPONSE_RABBITMQ_PROPERTIES = getOrchestratorResponseRabbitMQProperties();
    }

    private static RabbitMQProperties getOrchestratorResponseRabbitMQProperties() {
        RabbitMQProperties rabbitMQProperties = getProperties();
        rabbitMQProperties.setRoutingKey(properties.getProperty(ORCHESTRATOR_RESPONSE_ROUTING_KEY));
        rabbitMQProperties.setExchangeName(properties.getProperty(ORCHESTRATOR_RESPONSE_EXCHANGE_NAME));
        rabbitMQProperties.setQueueName(properties.getProperty(ORCHESTRATOR_RESPONSE_QUEUE));
        return rabbitMQProperties;
    }

    private static RabbitMQProperties getSchedulerMessageRabbitMQProperties() {
        RabbitMQProperties rabbitMQProperties = getProperties();
        rabbitMQProperties.setRoutingKey(properties.getProperty(SCHEDULER_MESSAGE_ROUTING_KEY));
        rabbitMQProperties.setExchangeName(properties.getProperty(SCHEDULER_MESSAGE_EXCHANGE_NAME));
        rabbitMQProperties.setQueueName(properties.getProperty(SCHEDULER_MESSAGE_QUEUE));
        return rabbitMQProperties;
    }

    private static RabbitMQProperties getSchedulerRabbitMQProperties() {
        RabbitMQProperties rabbitMQProperties = getProperties();
        rabbitMQProperties.setRoutingKey(RabbitMQConstants.SCHEDULER_ROUTING_KEY);
        rabbitMQProperties.setExchangeName(RabbitMQConstants.SCHEDULER_EXCHANGE);
        rabbitMQProperties.setQueueName(RabbitMQConstants.SCHEDULER_QUEUE);
        return rabbitMQProperties;
    }

    public static RabbitMQProperties getProperties() {
        return new RabbitMQProperties().setBrokerUrl(RabbitMQConstants.AMQP_URI)
                .setDurable(Boolean.parseBoolean(RabbitMQConstants.IS_DURABLE_QUEUE))
                .setPrefetchCount(Integer.parseInt(RabbitMQConstants.PREFETCH_COUT)).setAutoRecoveryEnable(true)
                .setAutoAck(Boolean.parseBoolean(RabbitMQConstants.IS_AUTO_ACK))
                .setConsumerTag(RabbitMQConstants.CONSUMER_TAG)
                .setExchangeType(RabbitMQProperties.EXCHANGE_TYPE.TOPIC);
    }

    public static int getPropertyOrDefault(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key));
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static double getPropertyOrDefault(String key, double defaultValue) {
        try {
            return Double.parseDouble(properties.getProperty(key));
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }
}
