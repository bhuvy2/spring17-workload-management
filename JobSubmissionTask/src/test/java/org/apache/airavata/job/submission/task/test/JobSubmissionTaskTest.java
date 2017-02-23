package org.apache.airavata.job.submission.task.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.airavata.sga.commons.model.Application;
import org.apache.airavata.sga.commons.model.Experiment;
import org.apache.airavata.sga.commons.model.MachineType;
import org.apache.airavata.sga.commons.model.OperationFailedException;
import org.apache.airavata.sga.commons.model.Response;
import org.apache.airavata.sga.commons.model.TargetMachine;
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
		// create experiment
		Experiment exp = new Experiment();
		exp.setExperimentId("experiment-" + ThreadLocalRandom.current().nextInt(5000));
		exp.setDiskMB(10);
		exp.setRamMB(128);
		exp.setNumCPU(0.1);
		
		// create application
		Application app = new Application();
		List<String> commands = new ArrayList<>();
		commands.add("ping sga-mesos-master -c 4");
		commands.add("ping sga-mesos-slave -c 4");
		app.setCommands(commands);
		
		// create target machine
		TargetMachine target = new TargetMachine();
		target.setHostname("sga-mesos-master");
		target.setPort(8081);
		target.setLoginId("centos");
		target.setMachineType(MachineType.CLOUD);
		
		// create taskcontext
		TaskContext taskContext = new TaskContext();
		taskContext.setApplication(app);
		taskContext.setExperiment(exp);
		taskContext.setTargetMachine(target);
	
		CommonTask task = new JobSubmissionTaskImpl();
		try {
			System.out.println("*** Submitting experiment: " + exp.getExperimentId());
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
	 * @param args the arguments
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		JobSubmissionTaskTest test = new JobSubmissionTaskTest();
		test.testSubmitCloudJob();
		test.testCloudJobSubmissionTaskImpl();

		// load test with concurrent submissions
//		Runnable runnable = new Runnable() {
//			@Override
//			public void run() {
//				test.testCloudJobSubmissionTaskImpl();
//			}
//		};
//		
//		for (int i=0 ; i<20 ; i++) {
//			Thread thread = new Thread(runnable);
//			thread.start();
//		}
	}
}
