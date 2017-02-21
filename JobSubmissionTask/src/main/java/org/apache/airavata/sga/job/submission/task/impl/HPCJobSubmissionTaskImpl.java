package org.apache.airavata.sga.job.submission.task.impl;

import java.util.List;

import org.apache.airavata.sga.job.submission.task.JobSubmissionTask;

public class HPCJobSubmissionTaskImpl implements JobSubmissionTask {

	@Override
	public String submitJob(String experimentId, String userName, String environmentName, Double numCPU, Long diskMB,
			Long ramMB, List<String> commands) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
