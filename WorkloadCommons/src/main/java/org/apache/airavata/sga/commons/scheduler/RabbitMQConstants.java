package org.apache.airavata.sga.commons.scheduler;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * The Class RabbitMQConstants.
 * 
 * @author goshenoy
 */
public class RabbitMQConstants {

	public static final Logger logger = LogManager.getLogger(RabbitMQConstants.class);
	
	// RabbitMQ default property names
	public static final String AMQP_URI_PROP = "amqp.uri";
	public static final String CONSUMER_TAG_PROP = "consumer.tag";
	public static final String IS_DURABLE_QUEUE_PROP = "is.durable.queue";
	public static final String IS_AUTO_ACK_PROP = "is.auto.ack";
	public static final String PREFETCH_COUT_PROP = "prefetch.count";
		
	// Scheduler RabbitMQ property names
	private static final String SCHEDULER_PROP_FILE_NAME = "rabbitmq.properties";
	private static final String SCHEDULER_EXCHANGE_PROP = "scheduler.exchange.name";
	private static final String SCHEDULER_QUEUE_PROP = "scheduler.queue";
	private static final String SCHEDULER_ROUTING_KEY_PROP = "scheduler.routing.key";
	
	// Scheduler RabbitMQ property values
	public static final String SCHEDULER_QUEUE;
	public static final String SCHEDULER_EXCHANGE;
	public static final String SCHEDULER_ROUTING_KEY;
	
	// RabbitMQ default property names
	public static final String AMQP_URI;
	public static final String CONSUMER_TAG;
	public static final String IS_DURABLE_QUEUE;
	public static final String IS_AUTO_ACK;
	public static final String PREFETCH_COUT;
	
	public static final Properties properties;

	static {
		properties = new Properties();
		try {
			properties.load(RabbitMQConstants.class.getClassLoader().getResourceAsStream(SCHEDULER_PROP_FILE_NAME));
		} catch (IOException ex) {
			logger.error(ex, ex);
		}
		
		// initialize general values
		AMQP_URI = properties.getProperty(AMQP_URI_PROP);
		CONSUMER_TAG = properties.getProperty(CONSUMER_TAG_PROP);
		IS_DURABLE_QUEUE = properties.getProperty(IS_DURABLE_QUEUE_PROP);
		IS_AUTO_ACK = properties.getProperty(IS_AUTO_ACK_PROP);
		PREFETCH_COUT = properties.getProperty(PREFETCH_COUT_PROP);
		
		// initialize scheduler values
		SCHEDULER_QUEUE = properties.getProperty(SCHEDULER_QUEUE_PROP);
		SCHEDULER_EXCHANGE = properties.getProperty(SCHEDULER_EXCHANGE_PROP);
		SCHEDULER_ROUTING_KEY = properties.getProperty(SCHEDULER_ROUTING_KEY_PROP);
	}
}
