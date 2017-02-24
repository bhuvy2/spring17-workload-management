package org.apache.airavata.sga.data.staging.task.runner;

import org.apache.airavata.sga.data.staging.task.messaging.EnvironmentSetupTaskMessagingFactory;
import org.apache.airavata.sga.messaging.service.core.Subscriber;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by Ajinkya on 2/23/17.
 */
public class EnvironmentSetupRunner {

    /** The Constant logger. */
    private static final Logger logger = LogManager.getLogger(EnvironmentSetupRunner.class);

    /** The Environment Setup subscriber. */
    private Subscriber environmentSetupSubscriber;

    /**
     * Start Environment Setup task runner.
     */
    public void startEnvironmentSetupTaskRunner() {
        try {
            logger.info("startEnvironmentSetupTaskRunner() -> Initializing Environment Setup subscriber");
            environmentSetupSubscriber = EnvironmentSetupTaskMessagingFactory.getSubscriber();
            logger.info("startEnvironmentSetupTaskRunner() -> Environment Setup subscriber now listening: " + environmentSetupSubscriber);
        } catch (Exception ex) {
            logger.error("startEnvironmentSetupTaskRunner() -> Something went wrong starting Environment Setup subscriber. Error: " + ex, ex);
        }
    }
}
