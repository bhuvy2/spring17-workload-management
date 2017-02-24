package org.apache.airavata.sga.scheduler.messaging;

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

/**
 * Created by Ajinkya on 2/24/17.
 */
public class SchedulerMessageHandler implements MessageHandler {

    /** The Constant logger. */
    private static final Logger logger = LogManager.getLogger(SchedulerResponseHandler.class);

    @Override
    public void onMessage(MessageContext messageContext) {
        try{
            logger.info("onMessage() -> New message received. Message Id : " + messageContext.getMessageId());

            TBase event = messageContext.getEvent();
            byte[] bytes = ThriftUtils.serializeThriftObject(event);

            Message message = new Message();
            ThriftUtils.createThriftFromBytes(bytes, message);

            TaskContext taskContext = new TaskContext();
            ThriftUtils.createThriftFromBytes(message.getEvent(), taskContext);

            logger.debug("onMessage() -> Get publisher. Message Id : " + messageContext.getMessageId());

            Publisher publisher = SchedulerTaskPublisher.getPublisher(taskContext.getQueueName());

            logger.info("onMessage() -> Publishing task context. Queue name : " + taskContext.getQueueName() + ", Experiment Id : " +  taskContext.getExperiment().getExperimentId());

            publisher.publish(messageContext);
            logger.info("onMessage() -> Message published. Queue name : " + taskContext.getQueueName() + ", Experiment Id : " +  taskContext.getExperiment().getExperimentId());

        } catch (TException e) {
            logger.error("onMessage() -> Error processing message. Message Id : " + messageContext.getMessageId(), e);
        } catch (Exception e) {
            logger.error("onMessage() -> Error publishing message. Message Id : " + messageContext.getMessageId(), e);
        }
    }
}
