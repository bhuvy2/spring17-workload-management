package org.apache.airavata.sga.scheduler.messaging.buffer;

/**
 * Created by arvind on 2/25/17.
 */
public class BufferException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Issue in buffer operations";

    public BufferException() {
        super("BufferException: " + DEFAULT_MESSAGE);
    }

    public BufferException (String message) {
        super("BufferException: " + message);
    }

}
