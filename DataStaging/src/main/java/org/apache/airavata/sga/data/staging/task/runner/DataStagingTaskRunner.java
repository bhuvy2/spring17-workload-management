package org.apache.airavata.sga.data.staging.task.runner;

import org.apache.airavata.sga.data.staging.task.messaging.DataStagingMessageHandler;
import org.apache.airavata.sga.data.staging.task.util.Constants;
import org.apache.airavata.sga.messaging.service.core.MessagingFactory;
import org.apache.airavata.sga.messaging.service.core.Subscriber;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by Ajinkya on 2/23/17.
 */
public class DataStagingTaskRunner {

    /** The Constant logger. */
    private static final Logger logger = LogManager.getLogger(DataStagingTaskRunner.class);

    /** The Data Staging subscriber. */
    private Subscriber dataStagingSubscriber;

    /**
     * Start Data Staging task runner.
     */
    public void startDataStagingTaskRunner() {
        try {
            logger.info("startDataStagingTaskRunner() -> Initializing Data Staging subscriber");
            dataStagingSubscriber = MessagingFactory.getSubscriber(new DataStagingMessageHandler(),
                    Constants.DATA_STAGING_RABBITMQ_PROPERTIES);
            logger.info("startDataStagingTaskRunner() -> Data Staging subscriber now listening: " + dataStagingSubscriber);
        } catch (Exception ex) {
            logger.error("startDataStagingTaskRunner() -> Something went wrong starting data staging subscriber. Error: " + ex, ex);
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
                    DataStagingTaskRunner dataStagingTaskRunner = new DataStagingTaskRunner();
                    dataStagingTaskRunner.startDataStagingTaskRunner();
                }
            };

            // start the worker thread
            logger.info("main() -> Starting the DataStaging worker.");
            new Thread(runner).start();
        } catch (Exception ex) {
            logger.error("main() -> Something went wrong with the DataStaging runner. Error: " + ex, ex);
        }
    }
}
