package org.apache.airavata.sga.data.staging.task.messaging;

import org.apache.airavata.sga.commons.model.TaskContext;
import org.apache.airavata.sga.commons.task.CommonTask;
import org.apache.airavata.sga.data.staging.task.handler.impl.EnvironmentSetupTaskImpl;
import org.apache.airavata.sga.messaging.service.core.MessageHandler;
import org.apache.airavata.sga.messaging.service.util.MessageContext;
import org.apache.airavata.sga.messaging.service.util.ThriftUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;

/**
 * Created by Ajinkya on 2/22/17.
 */
public class EnvironmentSetupMessageHandler implements MessageHandler {

    /** The Constant logger. */
    private static final Logger logger = LogManager.getLogger(EnvironmentSetupMessageHandler.class);

    @Override
    public void onMessage(MessageContext messageContext) {
        try {

            logger.info("onMessage() -> New message received. Message Id : " + messageContext.getMessageId());

            TBase event = messageContext.getEvent();
            byte[] bytes = ThriftUtils.serializeThriftObject(event);

            TaskContext taskContext = new TaskContext();
            ThriftUtils.createThriftFromBytes(bytes, taskContext);

            logger.info("onMessage() -> Handling environment setup task. Message Id : " + messageContext.getMessageId() + ", Experiment Id : " +  taskContext.getExperiment().getExperimentId());

            CommonTask task = new EnvironmentSetupTaskImpl();
            task.execute(taskContext);

        } catch (TException e) {
            logger.error("onMessage() -> Error processing message. Message Id : " + messageContext.getMessageId(), e);
        }
    }
}
