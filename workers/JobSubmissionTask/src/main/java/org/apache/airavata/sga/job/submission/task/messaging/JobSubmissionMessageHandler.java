package org.apache.airavata.sga.job.submission.task.messaging;

import org.apache.airavata.sga.commons.model.Response;
import org.apache.airavata.sga.commons.model.TaskContext;
import org.apache.airavata.sga.commons.task.CommonTask;
import org.apache.airavata.sga.job.submission.task.impl.JobSubmissionTaskImpl;
import org.apache.airavata.sga.messaging.service.core.MessageHandler;
import org.apache.airavata.sga.messaging.service.core.Publisher;
import org.apache.airavata.sga.messaging.service.util.MessageContext;
import org.apache.airavata.sga.messaging.service.util.ThriftUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.thrift.TBase;

public class JobSubmissionMessageHandler implements MessageHandler {

	private static final Logger logger = LogManager.getLogger(JobSubmissionMessageHandler.class);

	@Override
	public void onMessage(MessageContext messageContext) {
		try {
			TBase<?, ?> event = messageContext.getEvent();
			byte[] bytes = ThriftUtils.serializeThriftObject(event);

			// get taskcontext from message
			TaskContext taskContext = new TaskContext();
			ThriftUtils.createThriftFromBytes(bytes, taskContext);

			// fetch experimentId from taskcontext
			logger.info("Received jobsubmission task, with taskContext: " + taskContext);
			String experimentId = taskContext.getExperiment().getExperimentId();
			
			// execute the task
			CommonTask task = new JobSubmissionTaskImpl();
			Response response = task.execute(taskContext);
			logger.info("Response | expId: " + experimentId
					+ ", response: " + response);
			
			// publish message back to scheduler
			logger.info("Sending response back as message, for expId: " + experimentId);
//			Publisher publisher = JobSubmissionTaskPublisher.getPublisher();
//			MessageContext responseMsg = new MessageContext(response, experimentId);
//			publisher.publish(responseMsg);
		} catch (Exception ex) {
			logger.error("Error performing jobsubmission task, ex: " + ex, ex);
		}
	}
}
