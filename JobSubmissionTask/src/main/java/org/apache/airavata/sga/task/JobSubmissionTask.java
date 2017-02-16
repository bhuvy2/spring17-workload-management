package org.apache.airavata.sga.task;

import java.util.List;

public interface JobSubmissionTask {

	public String submitJob(String experimentId, String userName, String environmentName, Double numCPU, Long diskMB,
			Long ramMB, List<String> commands) throws Exception;
}
