package org.apache.airavata.sga.job.submission.task.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.airavata.sga.commons.scheduler.RabbitMQConstants;
import org.apache.airavata.sga.messaging.service.util.RabbitMQProperties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * The Class Constants.
 * 
 * @author goshenoy
 */
public class Constants {

	// logger
	private static final Logger logger = LogManager.getLogger(Constants.class);

	// RabbitMQ jobsubmission property names
	public static final String JOB_SUBMISSION_PROP_FILE = "job-submission.properties";
	public static final String JOB_SUBMISSION_EXCHANGE_NAME = "jobsubmission.exchange.name";
	public static final String JOB_SUBMISSION_QUEUE = "jobsubmission.queue";
	public static final String JOB_SUBMISSION_ROUTING_KEY = "jobsubmission.routing.key";

	// RabbitMQ properties
	public static final RabbitMQProperties JOB_SUBMISSION_RABBITMQ_PROPERTIES;
	public static final RabbitMQProperties SCHEDULER_RABBITMQ_PROPERTIES;
	public static final Properties properties;

	static {
		properties = new Properties();
		try {
			properties.load(Constants.class.getClassLoader().getResourceAsStream(JOB_SUBMISSION_PROP_FILE));
		} catch (IOException ex) {
			logger.error("Error loading properties: " + ex.getMessage(), ex);
		}

		JOB_SUBMISSION_RABBITMQ_PROPERTIES = getJobSubmissionRabbitMQProperties();
		SCHEDULER_RABBITMQ_PROPERTIES = getSchedulerRabbitMQProperties();
	}

	/**
	 * Gets the job submission rabbit MQ properties.
	 *
	 * @return the job submission rabbit MQ properties
	 */
	private static RabbitMQProperties getJobSubmissionRabbitMQProperties() {
		RabbitMQProperties rabbitMQProperties = getProperties();
		rabbitMQProperties.setRoutingKey(properties.getProperty(JOB_SUBMISSION_ROUTING_KEY));
		rabbitMQProperties.setExchangeName(properties.getProperty(JOB_SUBMISSION_EXCHANGE_NAME));
		rabbitMQProperties.setQueueName(properties.getProperty(JOB_SUBMISSION_QUEUE));
		return rabbitMQProperties;
	}
	
	/**
	 * Gets the scheduler rabbit MQ properties.
	 *
	 * @return the scheduler rabbit MQ properties
	 */
	private static RabbitMQProperties getSchedulerRabbitMQProperties() {
		RabbitMQProperties rabbitMQProperties = getProperties();
		rabbitMQProperties.setRoutingKey(RabbitMQConstants.SCHEDULER_ROUTING_KEY);
		rabbitMQProperties.setExchangeName(RabbitMQConstants.SCHEDULER_EXCHANGE);
		rabbitMQProperties.setQueueName(RabbitMQConstants.SCHEDULER_QUEUE);
		return rabbitMQProperties;
	}

	/**
	 * Gets the properties.
	 *
	 * @return the properties
	 */
	private static RabbitMQProperties getProperties() {
		return new RabbitMQProperties().setBrokerUrl(RabbitMQConstants.AMQP_URI)
				.setDurable(Boolean.parseBoolean(RabbitMQConstants.IS_DURABLE_QUEUE))
				.setPrefetchCount(Integer.parseInt(RabbitMQConstants.PREFETCH_COUT)).setAutoRecoveryEnable(true)
				.setAutoAck(Boolean.parseBoolean(RabbitMQConstants.IS_AUTO_ACK))
				.setConsumerTag(RabbitMQConstants.CONSUMER_TAG)
				.setExchangeType(RabbitMQProperties.EXCHANGE_TYPE.TOPIC);
	}
}
