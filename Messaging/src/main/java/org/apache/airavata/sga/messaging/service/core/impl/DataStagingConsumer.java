package org.apache.airavata.sga.messaging.service.core.impl;

import java.io.IOException;

import org.apache.airavata.sga.messaging.service.core.MessageHandler;
import org.apache.airavata.sga.messaging.service.model.Message;
import org.apache.airavata.sga.messaging.service.model.Orders;
import org.apache.airavata.sga.messaging.service.util.Constants;
import org.apache.airavata.sga.messaging.service.util.MessageContext;
import org.apache.airavata.sga.messaging.service.util.ThriftUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;

public class DataStagingConsumer extends QueueingConsumer {

	private static final Logger logger = LogManager.getLogger(DataStagingConsumer.class);

    private MessageHandler handler;
    private Channel channel;
    private Connection connection;

    public DataStagingConsumer(MessageHandler messageHandler, Connection connection, Channel channel) {
        super(channel);
        this.handler = messageHandler;
        this.connection = connection;
        this.channel = channel;
    }


    @Override
    public void handleDelivery(String consumerTag,
                               Envelope envelope,
                               AMQP.BasicProperties properties,
                               byte[] body) throws IOException {

        Message message = new Message();

        try {
            logger.info("handleDelivery() -> Handling message delivery. Consumer Tag : " + consumerTag);
            ThriftUtils.createThriftFromBytes(body, message);

            Orders experimentEvent = new Orders();
            ThriftUtils.createThriftFromBytes(message.getEvent(), experimentEvent);

            TBase event = experimentEvent;
            MessageContext messageContext = new MessageContext(event, message.getMessageId(), envelope.getDeliveryTag());
            handler.onMessage(messageContext);
            //sendAck(deliveryTag);

        } catch (TException e) {
            logger.error("handleDelivery() -> Error handling delivery. Consumer Tag : " + consumerTag, e);
        }

    }


    private void sendAck(long deliveryTag){
        logger.info("sendAck() -> Sending ack. Delivery Tag : " + deliveryTag);
        try {
            if (channel.isOpen()){
                channel.basicAck(deliveryTag,false);
            }else {
                channel = connection.createChannel();
                channel.basicQos(Constants.PREFETCH_COUT);
                channel.basicAck(deliveryTag, false);
            }
        } catch (IOException e) {
            logger.error("sendAck() -> Error sending ack. Delivery Tag : " + deliveryTag, e);
        }
    }
}
