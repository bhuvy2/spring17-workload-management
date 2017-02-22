package org.apache.airavata.sga.data.staging.task.messaging;

import org.apache.airavata.sga.commons.model.TaskContext;
import org.apache.airavata.sga.messaging.service.core.MessageHandler;
import org.apache.airavata.sga.messaging.service.util.MessageContext;
import org.apache.airavata.sga.messaging.service.util.ThriftUtils;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;

/**
 * Created by Ajinkya on 2/22/17.
 */
public class EnvironmentSetupMessageHandler implements MessageHandler {

    @Override
    public void onMessage(MessageContext messageContext) {
        try {

            TBase event = messageContext.getEvent();
            byte[] bytes = ThriftUtils.serializeThriftObject(event);

            TaskContext taskContext = new TaskContext();
            ThriftUtils.createThriftFromBytes(bytes, taskContext);

            // TODO: call implementation
            System.out.println(taskContext);

        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
