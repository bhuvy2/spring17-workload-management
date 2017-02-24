package org.apache.airavata.sga.data.staging.task.test.util;

import org.apache.airavata.sga.commons.model.Response;
import org.apache.airavata.sga.messaging.service.core.MessageHandler;
import org.apache.airavata.sga.messaging.service.model.Message;
import org.apache.airavata.sga.messaging.service.util.MessageContext;
import org.apache.airavata.sga.messaging.service.util.ThriftUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.thrift.TBase;

import java.util.concurrent.CountDownLatch;

/**
 * The Class MockSchedulerHandler.
 * 
 * @author goshenoy
 */
public class MockSchedulerHandler implements MessageHandler {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(MockSchedulerHandler.class);
	
	private CountDownLatch latch;
	
	public MockSchedulerHandler(CountDownLatch latch) {
		this.latch = latch;
	}

	/* (non-Javadoc)
	 * @see org.apache.airavata.sga.messaging.service.core.MessageHandler#onMessage(org.apache.airavata.sga.messaging.service.util.MessageContext)
	 */
	@Override
	public void onMessage(MessageContext messageContext) {
		try {
			// get the message
			TBase<?, ?> event = messageContext.getEvent();
			byte[] bytes = ThriftUtils.serializeThriftObject(event);
			
			Message message = new Message();
			ThriftUtils.createThriftFromBytes(bytes, message);

			// get response from message
			Response response = new Response();
			ThriftUtils.createThriftFromBytes(message.getEvent(), response);

			// fetch experimentId from taskcontext
			logger.info("Received response from task: " + response);
			
			// send signal
			logger.info("Sending signal to testcase runner");
			latch.countDown();
		} catch (Exception ex) {
			logger.error("Error receiving response from task, ex: " + ex, ex);
		}
		
	}

	
}
