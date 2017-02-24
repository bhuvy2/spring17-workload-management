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

            logger.info("onMessage() -> Received response. Response : " + response.toString());

        } catch (TException e) {
            logger.error("onMessage() -> Error processing message. Message Id : " + messageContext.getMessageId(), e);
        }
    }
}
