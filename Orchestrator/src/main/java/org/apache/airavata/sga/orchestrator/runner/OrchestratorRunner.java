package org.apache.airavata.sga.orchestrator.runner;

import org.apache.airavata.sga.messaging.service.core.MessagingFactory;
import org.apache.airavata.sga.messaging.service.core.Subscriber;
import org.apache.airavata.sga.orchestrator.messaging.OrchestratorMessageHandler;
import org.apache.airavata.sga.orchestrator.messaging.Orchestrator;
import org.apache.airavata.sga.orchestrator.util.Constants;
import org.apache.airavata.sga.orchestrator.runner.OrchestratorRunner;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by Ajinkya on 2/24/17.
 */
public class OrchestratorRunner {

    /** The Constant logger. */
    private static final Logger logger = LogManager.getLogger(OrchestratorRunner.class);

    private Subscriber subscriber;

    private void startSchedulerRunner() {

		/*
        try {
            logger.info("Initializing scheduler's respose subscriber");
            subscriber = MessagingFactory.getSubscriber(new Orchestrator(),
                    Constants.SCHEDULER_RABBITMQ_PROPERTIES);
            logger.info("Schedule's Response subscriber now listening: " + subscriber);

            logger.info("Initializing scheduler's message subscriber");
            subscriber = MessagingFactory.getSubscriber(new OrchestratorMessageHandler(),
                    Constants.SCHEDULER_MESSAGE_RABBITMQ_PROPERTIES);
            logger.info("Schedule's Message subscriber now listening: " + subscriber);

        } catch (Exception ex) {
            logger.error("Something went wrong starting subscriber. Error: " + ex, ex);
        }
        */
    }

    public static void main(String[] args) {
        try {
            Runnable runner = new Runnable() {
                @Override
                public void run() {
                    OrchestratorRunner schedulerRunner = new OrchestratorRunner();
                    schedulerRunner.startSchedulerRunner();
                }
            };

            // start the worker thread
            logger.info("Starting the Scheduler.");
            new Thread(runner).start();
        } catch (Exception ex) {
            logger.error("Something went wrong with the Scheduler runner. Error: " + ex, ex);
        }
    }
}
