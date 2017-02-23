package org.apache.airavata.sga.data.staging.task.exception;

/**
 * Created by Ajinkya on 2/16/17.
 */
public class SSHException extends Exception {

    public SSHException(String message) {
        super(message);
    }

    public SSHException(String message, Exception e) {
        super(message, e);
    }
}