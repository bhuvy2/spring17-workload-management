package org.apache.airavata.sga.job.submission.task.runner;

import org.apache.airavata.sga.job.submission.task.messaging.JobSubmissionMessageHandler;
import org.apache.airavata.sga.job.submission.task.util.Constants;
import org.apache.airavata.sga.messaging.service.core.MessagingFactory;
import org.apache.airavata.sga.messaging.service.core.Subscriber;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * The Class JobSubmissionTaskRunner.
 * 
 * @author goshenoy
 */
public class JobSubmissionTaskRunner {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(JobSubmissionTaskRunner.class);

	/** The job submission subscriber. */
	private Subscriber jobSubmissionSubscriber;

	/**
	 * Start job submission task runner.
	 */
	public void startJobSubmissionTaskRunner() {
		try {
			logger.info("Initializing JobSubmissionTask subscriber");
			jobSubmissionSubscriber = MessagingFactory.getSubscriber(new JobSubmissionMessageHandler(),
					Constants.JOB_SUBMISSION_RABBITMQ_PROPERTIES);
			logger.info("JobSubmissionTask subscriber now listening: " + jobSubmissionSubscriber);
		} catch (Exception ex) {
			logger.error("Something went wrong starting subscriber. Error: " + ex, ex);
		}
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			Runnable runner = new Runnable() {
				@Override
				public void run() {
					JobSubmissionTaskRunner jsTaskRunner = new JobSubmissionTaskRunner();
					jsTaskRunner.startJobSubmissionTaskRunner();
				}
			};
			
			// start the worker thread
			logger.info("Starting the JobSubmissionTask worker.");
			new Thread(runner).start();
		} catch (Exception ex) {
			logger.error("Something went wrong with the JobSubmissionTask runner. Error: " + ex, ex);
		}
	}
}
