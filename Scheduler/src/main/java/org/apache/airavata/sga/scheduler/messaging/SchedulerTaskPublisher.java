package org.apache.airavata.sga.scheduler.messaging;

import org.apache.airavata.sga.messaging.service.core.MessagingFactory;
import org.apache.airavata.sga.messaging.service.core.Publisher;
import org.apache.airavata.sga.messaging.service.util.RabbitMQProperties;
import org.apache.airavata.sga.scheduler.util.Constants;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by Ajinkya on 2/24/17.
 */
public class SchedulerTaskPublisher {

    private static final Logger logger = LogManager.getLogger(SchedulerTaskPublisher.class);

    public static Publisher getPublisher(final String queueName){

        logger.info("getPublisher() -> Getting publisher for queue : " + queueName);
        if(Constants.PUBLISHER_MAP.containsKey(queueName)){
            return Constants.PUBLISHER_MAP.get(queueName);
        }

        logger.info("getPublisher() -> Creating publisher for queue : " + queueName);

        RabbitMQProperties rabbitMQProperties = Constants.getProperties();
        rabbitMQProperties.setExchangeName("");
        rabbitMQProperties.setQueueName(queueName);
        rabbitMQProperties.setRoutingKey("");

        Publisher publisher = MessagingFactory.getPublisher(rabbitMQProperties);
        //save publisher against queue for future reference
        Constants.PUBLISHER_MAP.put(queueName, publisher);
        return publisher;
    }
}
