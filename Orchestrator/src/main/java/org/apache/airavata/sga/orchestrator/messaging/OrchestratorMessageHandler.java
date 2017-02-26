package org.apache.airavata.sga.orchestrator.messaging;

import org.apache.airavata.sga.commons.model.SchedulingRequest;
import org.apache.airavata.sga.commons.model.TaskContext;
import org.apache.airavata.sga.messaging.service.core.MessageHandler;
import org.apache.airavata.sga.messaging.service.core.Publisher;
import org.apache.airavata.sga.messaging.service.model.Message;
import org.apache.airavata.sga.messaging.service.util.MessageContext;
import org.apache.airavata.sga.messaging.service.util.ThriftUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;

import util.TestUtils;

/**
 * Created by Ajinkya on 2/24/17.
 */
public class OrchestratorMessageHandler implements MessageHandler {

    /** The Constant logger. */
	Orchestrator mainOrchestrator;
	public OrchestratorMessageHandler(Orchestrator orch){
		mainOrchestrator = orch;
	}
    private static final Logger logger = LogManager.getLogger(OrchestratorMessageHandler.class);

    @Override
    public void onMessage(MessageContext messageContext) {
        try{
            logger.info("onMessage() -> New message received. Message Id : " + messageContext.getMessageId());

            if (messageContext.getMessage() != null){
            	// Call function to create a job
            	System.out.println("Message context exists");
            	return;
            }
            TBase<?, ?> event = messageContext.getEvent();
            byte[] bytes = ThriftUtils.serializeThriftObject(event);

            Message message = new Message();
            ThriftUtils.createThriftFromBytes(bytes, message);

            SchedulingRequest schedulingRequest = new SchedulingRequest();
            ThriftUtils.createThriftFromBytes(message.getEvent(), schedulingRequest);

            logger.debug("onMessage() -> Get publisher. Message Id : " + messageContext.getMessageId());

            Publisher publisher = TestUtils.getSchedulerMessagePublisher(); //OrchestratorMessagingFactory.getPublisher(schedulingRequest.getTaskContext().getQueueName());

            logger.info("onMessage() -> Publishing task context. Queue name : " + schedulingRequest.getTaskContext().getQueueName() + ", Experiment Id : " +  schedulingRequest.getTaskContext().getQueueName());
            publisher.publish(new MessageContext(schedulingRequest.getTaskContext(),
                    schedulingRequest.getTaskContext().getExperiment().getExperimentId()));
            logger.info("onMessage() -> Message published. Queue name : " + schedulingRequest.getTaskContext().getQueueName() + ", Experiment Id : " +  schedulingRequest.getTaskContext().getExperiment().getExperimentId());

        } catch (TException e) {
            logger.error("onMessage() -> Error processing message. Message Id : " + messageContext.getMessageId(), e);
        } catch (Exception e) {
            logger.error("onMessage() -> Error publishing message. Message Id : " + messageContext.getMessageId(), e);
        }
    }
}
