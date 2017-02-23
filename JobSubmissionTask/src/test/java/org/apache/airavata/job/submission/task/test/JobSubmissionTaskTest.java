package org.apache.airavata.job.submission.task.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.airavata.job.submission.task.test.util.TestUtils;
import org.apache.airavata.sga.commons.model.OperationFailedException;
import org.apache.airavata.sga.commons.model.Response;
import org.apache.airavata.sga.commons.model.TaskContext;
import org.apache.airavata.sga.commons.task.CommonTask;
import org.apache.airavata.sga.job.submission.JobSubmission;
import org.apache.airavata.sga.job.submission.impl.CloudJobSubmissionTaskImpl;
import org.apache.airavata.sga.job.submission.task.impl.JobSubmissionTaskImpl;

/**
 * The Class JobSubmissionTaskTest.
 * 
 * @author goshenoy
 * 
 */
public class JobSubmissionTaskTest {

	/**
	 * Test submit cloud job.
	 */
	public void testSubmitCloudJob() {
		try {
			JobSubmission cloudJS = new CloudJobSubmissionTaskImpl();
			List<String> commands = new ArrayList<>();
			commands.add("ping sga-mesos-master -c 4");
			commands.add("ping sga-mesos-slave -c 4");

			String status = cloudJS.submitJob("sga-mesos-master", 8081, "experiment-001", "centos", "devel",
					new Double(0.1), new Long(10), new Long(128), commands);
			System.out.println(status);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void testCloudJobSubmissionTaskImpl() {
		TaskContext taskContext = TestUtils.getTaskContext();
		CommonTask task = new JobSubmissionTaskImpl();

		try {
			System.out.println("*** Submitting experiment: " + taskContext.getExperiment().getExperimentId());
			Response response = task.execute(taskContext);
			System.out.println("* ExperimentID : " + response.getExperimentId());
			System.out.println("* Response Status: " + response.getStatus());
			System.out.println("* Response Message: " + response.getMessage());
		} catch (OperationFailedException ex) {
			System.err.println("* Failed to submit job to cloud. Reason: " + ex);
			ex.printStackTrace();
		}
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
//		JobSubmissionTaskTest test = new JobSubmissionTaskTest();
//		test.testSubmitCloudJob();
//		test.testCloudJobSubmissionTaskImpl();

		// load test with concurrent submissions
		// Runnable runnable = new Runnable() {
		// @Override
		// public void run() {
		// test.testCloudJobSubmissionTaskImpl();
		// }
		// };
		//
		// for (int i=0 ; i<20 ; i++) {
		// Thread thread = new Thread(runnable);
		// thread.start();
		// }
	}
}
