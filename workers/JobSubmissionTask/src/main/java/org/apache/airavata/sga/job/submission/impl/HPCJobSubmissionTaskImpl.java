package org.apache.airavata.sga.job.submission.impl;

import java.util.List;

import org.apache.airavata.sga.job.submission.JobSubmission;

/**
 * The Class HPCJobSubmissionTaskImpl.
 * 
 * @author goshenoy
 */
public class HPCJobSubmissionTaskImpl implements JobSubmission {

	/* (non-Javadoc)
	 * @see org.apache.airavata.sga.job.submission.JobSubmission#submitJob(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.Double, java.lang.Long, java.lang.Long, java.util.List)
	 */
	@Override
	public String submitJob(String targetHost, int targetPort, String experimentId, String userName,
			String environmentName, Double numCPU, Long diskMB, Long ramMB, List<String> commands) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
