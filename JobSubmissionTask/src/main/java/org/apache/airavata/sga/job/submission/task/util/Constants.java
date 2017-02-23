package org.apache.airavata.sga.job.submission.task.util;

import java.io.IOException;
import java.util.Properties;

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

	// RabbitMQ default property names
	public static final String AMQP_URI = "amqp.uri";
	public static final String CONSUMER_TAG = "consumer.tag";
	public static final String IS_DURABLE_QUEUE = "is.durable.queue";
	public static final String IS_AUTO_ACK = "is.auto.ack";
	public static final String PREFETCH_COUT = "prefetch.count";

	// RabbitMQ jobsubmission property names
	public static final String JOB_SUBMISSION_PROP_FILE = "job-submission.properties";
	public static final String JOB_SUBMISSION_EXCHANGE_NAME = "jobsubmission.exchange.name";
	public static final String JOB_SUBMISSION_QUEUE = "jobsubmission.queue";
	public static final String JOB_SUBMISSION_ROUTING_KEY = "jobsubmission.routing.key";

	// RabbitMQ properties
	public static final RabbitMQProperties JOB_SUBMISSION_RABBITMQ_PROPERTIES;
	public static final Properties properties;

	static {
		properties = new Properties();
		try {
			properties.load(Constants.class.getClassLoader().getResourceAsStream(JOB_SUBMISSION_PROP_FILE));
		} catch (IOException ex) {
			logger.error("Error loading properties: " + ex.getMessage(), ex);
		}

		JOB_SUBMISSION_RABBITMQ_PROPERTIES = getJobSubmissionRabbitMQProperties();
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
	 * Gets the properties.
	 *
	 * @return the properties
	 */
	private static RabbitMQProperties getProperties() {
		return new RabbitMQProperties().setBrokerUrl(properties.getProperty(AMQP_URI))
				.setDurable(Boolean.parseBoolean(properties.getProperty(IS_DURABLE_QUEUE)))
				.setPrefetchCount(Integer.parseInt(properties.getProperty(PREFETCH_COUT))).setAutoRecoveryEnable(true)
				.setAutoAck(Boolean.parseBoolean(properties.getProperty(IS_AUTO_ACK)))
				.setConsumerTag(properties.getProperty(CONSUMER_TAG))
				.setExchangeType(RabbitMQProperties.EXCHANGE_TYPE.TOPIC);
	}
}
