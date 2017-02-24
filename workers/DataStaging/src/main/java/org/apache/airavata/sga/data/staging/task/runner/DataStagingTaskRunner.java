package org.apache.airavata.sga.data.staging.task.runner;

import org.apache.airavata.sga.data.staging.task.messaging.DataStagingTaskMessagingFactory;
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
            dataStagingSubscriber = DataStagingTaskMessagingFactory.getSubscriber();
            logger.info("startDataStagingTaskRunner() -> Data Staging subscriber now listening: " + dataStagingSubscriber);
        } catch (Exception ex) {
            logger.error("startDataStagingTaskRunner() -> Something went wrong starting data staging subscriber. Error: " + ex, ex);
        }
    }
}
