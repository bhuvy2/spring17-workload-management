package org.apache.airavata.sga.scheduler.gossip;

/**
 * Created by arvind on 2/26/17.
 */
public class WorkerAccessException extends Exception {
    public WorkerAccessException(String message) {
        super("WorkerAccessException: " + message);
    }
}
