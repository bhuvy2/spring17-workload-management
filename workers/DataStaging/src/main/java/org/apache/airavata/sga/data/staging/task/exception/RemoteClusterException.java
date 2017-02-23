package org.apache.airavata.sga.data.staging.task.exception;

/**
 * Created by Ajinkya on 2/21/17.
 */
public class RemoteClusterException extends Exception {

    public RemoteClusterException(String message) {
        super(message);
    }

    public RemoteClusterException(String message, Exception e) {
        super(message, e);
    }

}
