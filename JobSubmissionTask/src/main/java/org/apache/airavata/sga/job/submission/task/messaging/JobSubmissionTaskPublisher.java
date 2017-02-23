package org.apache.airavata.sga.job.submission.task.messaging;

import org.apache.airavata.sga.job.submission.task.util.Constants;
import org.apache.airavata.sga.messaging.service.core.MessagingFactory;
import org.apache.airavata.sga.messaging.service.core.Publisher;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * The Class JobSubmissionTaskPublisher.
 * 
 * @author goshenoy
 */
public class JobSubmissionTaskPublisher {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(JobSubmissionTaskPublisher.class);
	
	/** The publisher. */
	private static Publisher publisher;
	
	/**
	 * Gets the publisher.
	 *
	 * @return the publisher
	 */
	public static Publisher getPublisher() {
		if (publisher == null) {
			logger.info("Initializing JobSubmissionTask publisher");
			publisher = MessagingFactory.getPublisher(Constants.JOB_SUBMISSION_RABBITMQ_PROPERTIES);
		}
		return publisher;
	}
}
