package org.apache.airavata.sga.data.staging.task.test;

import org.apache.airavata.sga.commons.model.TaskContext;
import org.apache.airavata.sga.data.staging.task.test.util.TestUtils;
import org.apache.airavata.sga.messaging.service.core.Publisher;
import org.apache.airavata.sga.messaging.service.util.MessageContext;

/**
 * Created by Ajinkya on 2/23/17.
 */
public class DataStagingTaskTest {

    public void testDataStaging() {
        try {
            TaskContext taskContext = TestUtils.getTaskContext();
            Publisher publisher = TestUtils.getDataStagingPublisher();

            // publish message
            MessageContext messageContext = new MessageContext(taskContext,
                    taskContext.getExperiment().getExperimentId());
            publisher.publish(messageContext);
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
        DataStagingTaskTest test = new DataStagingTaskTest();
        test.testDataStaging();
    }
}
