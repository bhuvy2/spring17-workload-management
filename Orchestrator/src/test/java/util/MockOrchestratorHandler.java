package util;

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
 * Created by Ajinkya on 2/24/17.
 */
public class MockOrchestratorHandler implements MessageHandler {

    /** The Constant logger. */
    private static final Logger logger = LogManager.getLogger(MockOrchestratorHandler.class);

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

            // Handle response at orchestrator
            logger.info("onMessage() -> Received response from scheduler: " + response);

        } catch (Exception ex) {
            logger.error("Error receiving response from task, ex: " + ex, ex);
        }
    }
}
