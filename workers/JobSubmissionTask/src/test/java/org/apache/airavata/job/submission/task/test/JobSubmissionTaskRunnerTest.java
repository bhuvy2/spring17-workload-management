package org.apache.airavata.job.submission.task.test;

import java.util.concurrent.CountDownLatch;

import org.apache.airavata.job.submission.task.test.util.TestUtils;
import org.apache.airavata.sga.commons.model.TaskContext;
import org.apache.airavata.sga.messaging.service.core.Publisher;
import org.apache.airavata.sga.messaging.service.util.MessageContext;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * The Class JobSubmissionTaskRunnerTest.
 * 
 * @author goshenoy
 */
public class JobSubmissionTaskRunnerTest {

	/** The logger. */
	private Logger logger = LogManager.getLogger(JobSubmissionTaskRunnerTest.class);
	
	/**
	 * Test JS task runner start.
	 */
	public void testJSTaskRunnerStart() {
		// signal to wait
		CountDownLatch signal = new CountDownLatch(1);
		
		try {
			TaskContext taskContext = TestUtils.getTaskContext();
			Publisher publisher = TestUtils.getPublisher();

			// start scheduler subscriber
			TestUtils.startMockSchedulerSubscriber(signal);
			
			// publish message
			MessageContext messageContext = new MessageContext(taskContext,
					taskContext.getExperiment().getExperimentId());
			publisher.publish(messageContext);
			
			// wait for scheduler handler signal
			logger.info("Waiting for scheduler signal.");
			signal.await();
			logger.info("Received signal from scheduler.");
			
			// kill any background daemons
			System.exit(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		JobSubmissionTaskRunnerTest test = new JobSubmissionTaskRunnerTest();
		test.testJSTaskRunnerStart();
	}
}
