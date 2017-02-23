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

/**
 * The Class JobSubmissionTaskImpl.
 * 
 * @author goshenoy
 */
public class JobSubmissionTaskImpl implements CommonTask {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(JobSubmissionTaskImpl.class);

	/* (non-Javadoc)
	 * @see org.apache.airavata.sga.commons.task.CommonTask#init()
	 */
	@Override
	public void init() throws OperationFailedException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.airavata.sga.commons.task.CommonTask#execute(org.apache.airavata.sga.commons.model.TaskContext)
	 */
	@Override
	public Response execute(TaskContext taskContext) throws OperationFailedException {
		Response response = new Response();
		response.setExperimentId(taskContext.getExperiment().getExperimentId());
		
		try {
			JobSubmission jobSubmission = null;
			MachineType machineType = taskContext.getTargetMachine().getMachineType();
			if (machineType != null) {
				// determine of cloud/hpc job submission
				if (machineType.equals(MachineType.CLOUD)) {
					jobSubmission = new CloudJobSubmissionTaskImpl();
					// submit cloud job
					String status = jobSubmission.submitJob(taskContext.getTargetMachine().getHostname(),
							taskContext.getTargetMachine().getPort(), taskContext.getExperiment().getExperimentId(),
							taskContext.getTargetMachine().getLoginId(), AuroraUtils.ENVIRONMENT,
							taskContext.getExperiment().getNumCPU(), taskContext.getExperiment().getDiskMB(),
							taskContext.getExperiment().getRamMB(), taskContext.getApplication().getCommands());

					if (status != null) {
						if (status.equals(Status.OK.name())) {
							response.setStatus(Status.OK);
							response.setMessage("CloudJobSubmission Successfull!");
						} else {
							throw new Exception("Something went wrong with CloudJobSubmission. Please check logs");
						}
					} else {
						throw new Exception("No response from the target machine. Please check logs");
					}
				} else if (machineType.equals(MachineType.HPC)) {
					jobSubmission = new HPCJobSubmissionTaskImpl();
					response.setStatus(Status.NOT_FOUND);
					response.setMessage("HPC job submission not implemented.");
				} else {
					throw new Exception("Unimplemented job submission for machine-type: " + machineType.name());
				}
			}

		} catch (Exception ex) {
			logger.error("JobSubmissionTask failed, reason: " + ex.getMessage(), ex);
			response.setStatus(Status.FAILED);
			response.setMessage("JobSubmissionTask failed, reason: " + ex.getMessage());
		}

		return response;
	}

	/* (non-Javadoc)
	 * @see org.apache.airavata.sga.commons.task.CommonTask#postExecute()
	 */
	@Override
	public void postExecute() throws OperationFailedException {
		// TODO Auto-generated method stub

	}
}
