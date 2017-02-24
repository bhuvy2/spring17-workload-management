package org.apache.airavata.sga.data.staging.task.messaging;

import org.apache.airavata.sga.data.staging.task.util.Constants;
import org.apache.airavata.sga.messaging.service.core.MessagingFactory;
import org.apache.airavata.sga.messaging.service.core.Publisher;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by Ajinkya on 2/23/17.
 */
public class SchedulerMessageFactory {

    private static final Logger logger = LogManager.getLogger(SchedulerMessageFactory.class);

    private static Publisher schedulerPublisher;

    public static Publisher getSchedulerPublisher() {
        if (schedulerPublisher == null) {
            logger.info("Initializing Scheduler publisher");
            schedulerPublisher = MessagingFactory.getPublisher(Constants.SCHEDULER_RABBITMQ_PROPERTIES);
        }
        return schedulerPublisher;
    }
}
