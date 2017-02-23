package org.apache.airavata.job.submission.task.test.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.airavata.sga.commons.model.Application;
import org.apache.airavata.sga.commons.model.Experiment;
import org.apache.airavata.sga.commons.model.MachineType;
import org.apache.airavata.sga.commons.model.TargetMachine;
import org.apache.airavata.sga.commons.model.TaskContext;
import org.apache.airavata.sga.job.submission.task.messaging.JobSubmissionTaskPublisher;
import org.apache.airavata.sga.messaging.service.core.Publisher;

/**
 * The Class TestUtils.
 * 
 * @author goshenoy
 */
public class TestUtils {

	/**
	 * Gets the task context.
	 *
	 * @return the task context
	 */
	public static TaskContext getTaskContext() {
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
		
		return taskContext;
	}
	
	/**
	 * Gets the publisher.
	 *
	 * @return the publisher
	 */
	public static Publisher getPublisher() {
		return JobSubmissionTaskPublisher.getPublisher();
	}
}
