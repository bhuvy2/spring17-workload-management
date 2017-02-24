package org.apache.airavata.sga.messaging.service.util;

/**
 * Created by Ajinkya on 2/2/17.
 */
import org.apache.thrift.TBase;
public class MessageContext {
    private final TBase event;
    private final String messageId;
    private long deliveryTag;
    private int priority;

    public MessageContext(TBase event, String messageId){
        this.event = event;
        this.messageId = messageId;
    }

    public MessageContext(TBase event, String messageId, long deliveryTag){
        this.event = event;
        this.messageId = messageId;
        this.deliveryTag = deliveryTag;
    }

    public MessageContext(TBase event, String messageId, long deliveryTag, int priority){
        this.event = event;
        this.messageId = messageId;
        this.deliveryTag = deliveryTag;
        this.priority = priority;
    }

    public TBase getEvent() {
        return event;
    }

    public String getMessageId() {
        return messageId;
    }

    public long getDeliveryTag() {
        return deliveryTag;
    }

    public int getPriority() {
        return priority;
    }
}
