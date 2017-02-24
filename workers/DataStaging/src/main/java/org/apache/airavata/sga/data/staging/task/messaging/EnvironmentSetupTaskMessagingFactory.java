package org.apache.airavata.sga.data.staging.task.messaging;

import org.apache.airavata.sga.data.staging.task.util.Constants;
import org.apache.airavata.sga.messaging.service.core.MessagingFactory;
import org.apache.airavata.sga.messaging.service.core.Publisher;
import org.apache.airavata.sga.messaging.service.core.Subscriber;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by Ajinkya on 2/23/17.
 */
public class EnvironmentSetupTaskMessagingFactory {

    /** The Constant logger. */
    private static final Logger logger = LogManager.getLogger(EnvironmentSetupTaskMessagingFactory.class);

    /** The publisher. */
    private static Publisher publisher;

    /** The Environment Setup subscriber. */
    private static Subscriber subscriber;

    public static Publisher getPublisher() {
        if (publisher == null) {
            logger.info("Initializing Environment Setup publisher");
            publisher = MessagingFactory.getPublisher(Constants.ENVIRONMENT_SETUP_RABBITMQ_PROPERTIES);
        }
        return publisher;
    }

    public static Subscriber getSubscriber() {
        if (subscriber == null) {
            logger.info("Initializing Environment Setup subscriber");
            subscriber = MessagingFactory.getSubscriber(new EnvironmentSetupMessageHandler(),
                    Constants.ENVIRONMENT_SETUP_RABBITMQ_PROPERTIES);
        }
        return subscriber;
    }
}
