package org.apache.airavata.sga.scheduler.messaging;

import org.apache.airavata.sga.commons.model.Response;
import org.apache.airavata.sga.messaging.service.core.MessageHandler;
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
public class SchedulerResponseHandler implements MessageHandler {

    /** The Constant logger. */
    private static final Logger logger = LogManager.getLogger(SchedulerResponseHandler.class);

    @Override
    public void onMessage(MessageContext messageContext) {
        try {
            logger.info("onMessage() -> New message received. Message Id : " + messageContext.getMessageId());

            TBase event = messageContext.getEvent();
            byte[] bytes = ThriftUtils.serializeThriftObject(event);

            Message message = new Message();
            ThriftUtils.createThriftFromBytes(bytes, message);

            Response response = new Response();
            ThriftUtils.createThriftFromBytes(message.getEvent(), response);

            logger.info("onMessage() -> Sending response back to orchestrator. Response : " + response.toString()+ ", Experiment Id : " +  response.getExperimentId());

            MessageContext responseMsg = new MessageContext(response, response.getExperimentId());
            SchedulerMessagingFactory.getOrchestratorResponsePublisher().publish(responseMsg);
            logger.info("onMessage() -> Response sent. Response : " + response.toString());

            //send ack back to respective queue
//            logger.info("onMessage() -> Sending ack for message. Message Id : " + messageContext.getMessageId() + ", Delivery Tag : " + messageContext.getDeliveryTag());
//
//            SchedulerMessagingFactory.getSubscriber(messageContext).sendAck(messageContext.getDeliveryTag());
//            logger.debug("onMessage() -> Ack sent. Message Id : " + messageContext.getMessageId() + ", Experiment Id : " +  response.getExperimentId());


        } catch (TException e) {
            logger.error("onMessage() -> Error processing message. Message Id : " + messageContext.getMessageId(), e);
        } catch (Exception e) {
            logger.error("onMessage() -> Error sending response back to orchestrator. Message Id : " + messageContext.getMessageId(), e);
        }
    }
}
