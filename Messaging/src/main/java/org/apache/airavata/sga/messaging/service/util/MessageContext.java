package org.apache.airavata.sga.messaging.service.util;

/**
 * Created by Ajinkya on 2/2/17.
 */
import org.apache.thrift.TBase;
public class MessageContext {
    private final TBase event;
    private final String messageId;
    private final String message;
    private long deliveryTag;
    private int priority;

    public MessageContext(String m, String messageId){
    	this.event = null;
    	this.message = m;
    	this.messageId = messageId;
    }
    public MessageContext(TBase event, String messageId){
        this.event = event;
        this.message = null;
        this.messageId = messageId;
    }

    public MessageContext(TBase event, String messageId, long deliveryTag){
    	this.message = null;
        this.event = event;
        this.messageId = messageId;
        this.deliveryTag = deliveryTag;
    }

    public MessageContext(TBase event, String messageId, long deliveryTag, int priority){
    	this.message = null;
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
    public String getMessage(){
    	return message;
    }
}
