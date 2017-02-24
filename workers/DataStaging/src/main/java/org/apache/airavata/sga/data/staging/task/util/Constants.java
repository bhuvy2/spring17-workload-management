package org.apache.airavata.sga.data.staging.task.util;

import org.apache.airavata.sga.commons.scheduler.RabbitMQConstants;
import org.apache.airavata.sga.data.staging.task.messaging.Sample;
import org.apache.airavata.sga.messaging.service.core.MessagingFactory;
import org.apache.airavata.sga.messaging.service.util.RabbitMQProperties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Ajinkya on 2/22/17.
 */
public class Constants {

    private static final Logger logger = LogManager.getLogger(Constants.class);

    public static final String DATA_STAGING_PROP_FILE = "data-staging.properties";

    public static final String AMQP_URI = "amqp.uri";

    public static final String CONSUMER_TAG = "consumer.tag";
    public static final String IS_DURABLE_QUEUE = "is.durable.queue";
    public static final String IS_AUTO_ACK= "is.auto.ack";
    public static final String PREFETCH_COUT = "prefetch.count";

    public static final String ENVIRONMENT_SETUP_EXCHANGE_NAME = "environment.setup.exchange.name";
    public static final String ENVIRONMENT_SETUP_QUEUE = "environment.setup.queue";
    public static final String ENVIRONMENT_SETUP_ROUTING_KEY = "environment.setup.routing.key";

    public static final String DATA_STAGING_EXCHANGE_NAME = "data.staging.exchange.name";
    public static final String DATA_STAGING_QUEUE = "data.staging.queue";
    public static final String DATA_STAGING_ROUTING_KEY = "data.staging.routing.key";

    public static final RabbitMQProperties DATA_STAGING_RABBITMQ_PROPERTIES;
    public static final RabbitMQProperties ENVIRONMENT_SETUP_RABBITMQ_PROPERTIES;
    public static final RabbitMQProperties SCHEDULER_RABBITMQ_PROPERTIES;

    public static final Properties properties;

    static{
        properties = new Properties();
        try {
            properties.load(Sample.class.getClassLoader().getResourceAsStream(Constants.DATA_STAGING_PROP_FILE));
        } catch (IOException e) {
           logger.error("Error loading properties.", e);
        }
        DATA_STAGING_RABBITMQ_PROPERTIES = getDataStagingRabbitMQProperties();
        ENVIRONMENT_SETUP_RABBITMQ_PROPERTIES = getEnvironmentSetupRabbitMQProperties();
        SCHEDULER_RABBITMQ_PROPERTIES = getSchedulerRabbitMQProperties();
    }

    private static RabbitMQProperties getDataStagingRabbitMQProperties(){
        RabbitMQProperties rabbitMQProperties = getProperties();
        rabbitMQProperties.setRoutingKey(properties.getProperty(DATA_STAGING_ROUTING_KEY));
        rabbitMQProperties.setExchangeName(properties.getProperty(DATA_STAGING_EXCHANGE_NAME));
        rabbitMQProperties.setExchangeName(properties.getProperty(DATA_STAGING_EXCHANGE_NAME));
        rabbitMQProperties.setQueueName(properties.getProperty(DATA_STAGING_QUEUE));
        return rabbitMQProperties;
    }


    private static RabbitMQProperties getEnvironmentSetupRabbitMQProperties(){
        RabbitMQProperties rabbitMQProperties = getProperties();
        rabbitMQProperties.setRoutingKey(properties.getProperty(ENVIRONMENT_SETUP_ROUTING_KEY));
        rabbitMQProperties.setExchangeName(properties.getProperty(ENVIRONMENT_SETUP_EXCHANGE_NAME));
        rabbitMQProperties.setQueueName(properties.getProperty(ENVIRONMENT_SETUP_QUEUE));
        return rabbitMQProperties;
    }

    private static RabbitMQProperties getSchedulerRabbitMQProperties() {
        RabbitMQProperties rabbitMQProperties = getProperties();
        rabbitMQProperties.setRoutingKey(RabbitMQConstants.SCHEDULER_ROUTING_KEY);
        rabbitMQProperties.setExchangeName(RabbitMQConstants.SCHEDULER_EXCHANGE);
        rabbitMQProperties.setQueueName(RabbitMQConstants.SCHEDULER_QUEUE);
        return rabbitMQProperties;
    }

    private static RabbitMQProperties getProperties() {
        return new RabbitMQProperties()
                .setBrokerUrl(properties.getProperty(AMQP_URI))
                .setDurable(Boolean.parseBoolean(properties.getProperty(IS_DURABLE_QUEUE)))
                .setPrefetchCount(Integer.parseInt(properties.getProperty(PREFETCH_COUT)))
                .setAutoRecoveryEnable(true)
                .setAutoAck(Boolean.parseBoolean(properties.getProperty(IS_AUTO_ACK)))
                .setConsumerTag(properties.getProperty(CONSUMER_TAG))
                .setExchangeType(RabbitMQProperties.EXCHANGE_TYPE.TOPIC);
    }
}
