package org.apache.airavata.sga.scheduler.gossip;

/**
 * State enum to represent worker states
 * Created by arvind on 2/25/17.
 */
public enum WorkerState {
    WAITING("WAITING"),
    SENT("SENT"),
    FAILED("FAILED"),
    DONE("DONE"),
    DEFAULT("DEFAULT");

    private final String text;

    private WorkerState(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
