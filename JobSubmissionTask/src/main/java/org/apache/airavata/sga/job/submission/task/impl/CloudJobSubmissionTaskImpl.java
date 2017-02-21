package org.apache.airavata.sga.job.submission.task.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.airavata.cloud.aurora.client.AuroraThriftClient;
import org.apache.airavata.cloud.aurora.client.bean.IdentityBean;
import org.apache.airavata.cloud.aurora.client.bean.JobConfigBean;
import org.apache.airavata.cloud.aurora.client.bean.JobKeyBean;
import org.apache.airavata.cloud.aurora.client.bean.ProcessBean;
import org.apache.airavata.cloud.aurora.client.bean.ResourceBean;
import org.apache.airavata.cloud.aurora.client.bean.ResponseBean;
import org.apache.airavata.cloud.aurora.client.bean.TaskConfigBean;
import org.apache.airavata.cloud.aurora.util.AuroraThriftClientUtil;
import org.apache.airavata.cloud.aurora.util.Constants;
import org.apache.airavata.sga.job.submission.task.JobSubmissionTask;
import org.apache.airavata.sga.job.submission.task.util.AuroraUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class CloudJobSubmissionTaskImpl implements JobSubmissionTask {
	
	private static final Logger logger = LogManager.getLogger(CloudJobSubmissionTaskImpl.class);

	@Override
	public String submitJob(String experimentId, String userName, String environmentName, Double numCPU, Long diskMB,
			Long ramMB, List<String> commands) throws Exception {
		logger.info("Submitting job on cloud, with experimentId: " + experimentId);
		try {
			JobKeyBean jobKey = new JobKeyBean(environmentName, userName, experimentId);
			IdentityBean owner = new IdentityBean(userName);
			
			Set<ProcessBean> processes = new LinkedHashSet<>();
			for (int i = 0 ; i < commands.size() ; i++) {
				String command = commands.get(i);
				ProcessBean process = new ProcessBean("process_" + i, command, false);
				processes.add(process);
			}
			
			ResourceBean resources = new ResourceBean(numCPU, diskMB, ramMB);
			
			TaskConfigBean taskConfig = new TaskConfigBean("Task_SGA_" + experimentId, processes, resources);
			JobConfigBean jobConfig = new JobConfigBean(jobKey, owner, taskConfig, AuroraUtils.CLUSTER);
			
			String executorConfigJson = AuroraThriftClientUtil.getExecutorConfigJson(jobConfig);
			logger.debug("Aurora executor config JSON: " + executorConfigJson);
			
			AuroraThriftClient client = AuroraThriftClient.getAuroraThriftClient(Constants.AURORA_SCHEDULER_PROP_FILE);
			ResponseBean response = client.createJob(jobConfig);
			logger.info("Cloud job submission response: " + response);
			
			return response.getResponseCode().name();
		} catch (Exception ex) {
			logger.error("Failed to perform Cloud JobSubmissionTask, Reason: " + ex.getMessage(), ex);
			throw ex;
		}
	}

}
