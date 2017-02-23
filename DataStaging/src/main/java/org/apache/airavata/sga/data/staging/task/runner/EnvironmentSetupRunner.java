package org.apache.airavata.sga.data.staging.task.runner;

import org.apache.airavata.sga.data.staging.task.messaging.DataStagingMessageHandler;
import org.apache.airavata.sga.data.staging.task.messaging.EnvironmentSetupMessageHandler;
import org.apache.airavata.sga.data.staging.task.util.Constants;
import org.apache.airavata.sga.messaging.service.core.MessagingFactory;
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
            environmentSetupSubscriber = MessagingFactory.getSubscriber(new EnvironmentSetupMessageHandler(),
                    Constants.ENVIRONMENT_SETUP_RABBITMQ_PROPERTIES);
            logger.info("startEnvironmentSetupTaskRunner() -> Environment Setup subscriber now listening: " + environmentSetupSubscriber);
        } catch (Exception ex) {
            logger.error("startEnvironmentSetupTaskRunner() -> Something went wrong starting Environment Setup subscriber. Error: " + ex, ex);
        }
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        try {
            Runnable runner = new Runnable() {
                @Override
                public void run() {
                    EnvironmentSetupRunner environmentSetupRunner = new EnvironmentSetupRunner();
                    environmentSetupRunner.startEnvironmentSetupTaskRunner();
                }
            };

            // start the worker thread
            logger.info("main() -> Starting the EnvironmentSetupTask worker.");
            new Thread(runner).start();
        } catch (Exception ex) {
            logger.error("main() -> Something went wrong with the EnvironmentSetupTask runner. Error: " + ex, ex);
        }
    }

}
