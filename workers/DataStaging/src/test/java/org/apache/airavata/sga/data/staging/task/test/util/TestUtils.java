package org.apache.airavata.sga.data.staging.task.test.util;

import org.apache.airavata.sga.commons.model.*;
import org.apache.airavata.sga.data.staging.task.messaging.DataStagingTaskMessagingFactory;
import org.apache.airavata.sga.data.staging.task.messaging.EnvironmentSetupTaskMessagingFactory;
import org.apache.airavata.sga.messaging.service.core.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Ajinkya on 2/23/17.
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
		exp.setWorkingDir("wordir");

		// create application
		Application app = new Application();
		List<String> commands = new ArrayList<>();
		commands.add("ping sga-mesos-master -c 4");
		commands.add("ping sga-mesos-slave -c 4");
		app.setCommands(commands);

		Data data = new Data();
		data.setName("name1");
		data.setValue("value1");
		data.setType(DataType.STRING);
		List<Data> inputs = new ArrayList<>();
		inputs.add(data);

		app.setInputs(inputs);
		app.setOutputs(inputs);


		// create target machine
		TargetMachine target = new TargetMachine();
		target.setHostname("iris.ils.indiana.edu");
		target.setPort(22);
		target.setLoginId("adhamnas");
		target.setMachineType(MachineType.CLOUD);
		target.setScratchDir("/home/adhamnas/www/test4/test/");
		target.setDtProtocol(DataTransferProtocol.SCP);
		// create taskcontext
		TaskContext taskContext = new TaskContext();
		taskContext.setApplication(app);
		taskContext.setExperiment(exp);
		taskContext.setTargetMachine(target);
		
		return taskContext;
	}

	public static Publisher getEnvironmentSetupPublisher(){
		return EnvironmentSetupTaskMessagingFactory.getPublisher();
	}

	public static Publisher getDataStagingPublisher(){
		return DataStagingTaskMessagingFactory.getPublisher();
	}
}
