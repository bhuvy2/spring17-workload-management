package org.apache.airavata.sga.job.submission.task.impl;

import org.apache.airavata.sga.commons.model.MachineType;
import org.apache.airavata.sga.commons.model.OperationFailedException;
import org.apache.airavata.sga.commons.model.Response;
import org.apache.airavata.sga.commons.model.Status;
import org.apache.airavata.sga.commons.model.TaskContext;
import org.apache.airavata.sga.commons.task.CommonTask;
import org.apache.airavata.sga.job.submission.JobSubmission;
import org.apache.airavata.sga.job.submission.impl.CloudJobSubmissionTaskImpl;
import org.apache.airavata.sga.job.submission.impl.HPCJobSubmissionTaskImpl;
import org.apache.airavata.sga.job.submission.task.util.AuroraUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class JobSubmissionTaskImpl implements CommonTask {

	private static final Logger logger = LogManager.getLogger(JobSubmissionTaskImpl.class);

	@Override
	public void init() throws OperationFailedException {
		// TODO Auto-generated method stub

	}

	@Override
	public Response execute(TaskContext taskContext) throws OperationFailedException {
		Response response = null;
		try {
			JobSubmission jobSubmission = null;
			MachineType machineType = taskContext.getTargetMachine().getMachineType();
			if (machineType != null) {
				if (machineType.equals(MachineType.CLOUD)) {
					jobSubmission = new CloudJobSubmissionTaskImpl();
					String status = jobSubmission.submitJob(taskContext.getExperiment().getExperimentId(),
							taskContext.getTargetMachine().getLoginId(), AuroraUtils.ENVIRONMENT,
							taskContext.getExperiment().getNumCPU(), taskContext.getExperiment().getDiskMB(),
							taskContext.getExperiment().getRamMB(), taskContext.getApplication().getCommands());
					
					response = new Response();
					if (status != null) {
						if (status.equals(Status.OK.name())) {
							response.setStatus(Status.OK);
							response.setMessage("CloudJobSubmission Successfull!");
						} else {
							response.setStatus(Status.BAD_REQUEST);
							response.setMessage("Something went wrong with CloudJobSubmission. Please check logs");
						}
					}
				} else if (machineType.equals(MachineType.HPC)) {
					jobSubmission = new HPCJobSubmissionTaskImpl();
					// TODO: implement hpc job submission
				} else {
					throw new Exception("Unimplemented job submission for machine-type: " + machineType.name());
				}
			}

		} catch (Exception ex) {
			logger.error("JobSubmissionTask failed, reason: " + ex.getMessage(), ex);
			throw new OperationFailedException("JobSubmissionTask failed, reason: " + ex.getMessage());
		}

		return response;
	}

	@Override
	public void postExecute() throws OperationFailedException {
		// TODO Auto-generated method stub

	}
}
