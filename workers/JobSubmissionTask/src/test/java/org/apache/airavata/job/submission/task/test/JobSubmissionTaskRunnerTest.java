package org.apache.airavata.job.submission.task.test;

import org.apache.airavata.job.submission.task.test.util.TestUtils;
import org.apache.airavata.sga.commons.model.TaskContext;
import org.apache.airavata.sga.messaging.service.core.Publisher;
import org.apache.airavata.sga.messaging.service.util.MessageContext;

/**
 * The Class JobSubmissionTaskRunnerTest.
 * 
 * @author goshenoy
 */
public class JobSubmissionTaskRunnerTest {

	/**
	 * Test JS task runner start.
	 */
	public void testJSTaskRunnerStart() {
		try {
			TaskContext taskContext = TestUtils.getTaskContext();
			Publisher publisher = TestUtils.getPublisher();

			// publish message
			MessageContext messageContext = new MessageContext(taskContext,
					taskContext.getExperiment().getExperimentId());
			publisher.publish(messageContext);
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
