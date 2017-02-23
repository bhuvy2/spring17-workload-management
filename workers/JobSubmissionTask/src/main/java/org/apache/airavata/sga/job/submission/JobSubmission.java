package org.apache.airavata.sga.job.submission;

import java.util.List;

/**
 * The Interface JobSubmission.
 * 
 * @author goshenoy
 */
public interface JobSubmission {

	/**
	 * Submit job.
	 *
	 * @param targetHost the target host
	 * @param targetPort the target port
	 * @param experimentId the experiment id
	 * @param userName the user name
	 * @param environmentName the environment name
	 * @param numCPU the num CPU
	 * @param diskMB the disk MB
	 * @param ramMB the ram MB
	 * @param commands the commands
	 * @return the string
	 * @throws Exception the exception
	 */
	public String submitJob(String targetHost, int targetPort, String experimentId, String userName, String environmentName, Double numCPU, Long diskMB,
			Long ramMB, List<String> commands) throws Exception;
}
