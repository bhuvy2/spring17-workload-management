package org.apache.airavata.sga.job.submission;

import java.util.List;

public interface JobSubmission {

	public String submitJob(String experimentId, String userName, String environmentName, Double numCPU, Long diskMB,
			Long ramMB, List<String> commands) throws Exception;
}
