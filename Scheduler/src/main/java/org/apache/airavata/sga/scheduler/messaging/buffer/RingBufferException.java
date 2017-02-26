package org.apache.airavata.sga.scheduler.messaging.buffer;

/**
 * Created by arvind on 2/25/17.
 */
public class RingBufferException extends BufferException {

    private static final String DEFAULT_MESSAGE = "Issue in ring buffer operations";

    public RingBufferException() {
        super("RingBufferException: " + DEFAULT_MESSAGE);
    }

    public RingBufferException(String message) {
        super("RingBufferException: " + message);
    }

}
