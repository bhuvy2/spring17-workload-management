package org.apache.airavata.job.submission.task.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.airavata.sga.job.submission.JobSubmission;
import org.apache.airavata.sga.job.submission.impl.CloudJobSubmissionTaskImpl;

public class JobSubmissionTaskTest {

	public void testSubmitCloudJob() {
		try {
			JobSubmission cloudJS = new CloudJobSubmissionTaskImpl();
			List<String> commands = new ArrayList<>();
			commands.add("ping sga-mesos-master -c 4");
			commands.add("ping sga-mesos-slave -c 4");
			
			String status = cloudJS.submitJob("experiment-001", "centos", "devel", new Double(0.1), new Long(10),
					new Long(128), commands);
			System.out.println(status);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		JobSubmissionTaskTest test = new JobSubmissionTaskTest();
		test.testSubmitCloudJob();
	}
}
