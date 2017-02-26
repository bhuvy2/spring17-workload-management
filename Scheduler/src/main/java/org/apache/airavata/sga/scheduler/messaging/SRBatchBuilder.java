package org.apache.airavata.sga.scheduler.messaging;

import org.apache.airavata.sga.commons.model.SchedulingRequest;
import org.apache.airavata.sga.messaging.service.util.MessageContext;
import org.apache.airavata.sga.scheduler.messaging.buffer.TrackingBuffer;
import org.apache.airavata.sga.scheduler.util.Constants;

/**
 * Created by arvind on 2/26/17.
 */
public class SRBatchBuilder {

    private static final int BUFFER_SIZE_CONSTANT = Constants.getPropertyOrDefault
            (Constants.SCHEDULER_MESSAGE_BUFFER_SIZE_CONSTANT,20);
    private static final int BUFFER_TRACKING_SIZE = Constants.getPropertyOrDefault
            (Constants.SCHEDULER_MESSAGE_BUFFER_TRACKING_SIZE,10);
    private static final double BUFFER_RATE_MULTIPLIER = Constants.getPropertyOrDefault
            (Constants.SCHEDULER_MESSAGE_BUFFER_RATE_MULTIPLIER, 0.2);
    private static final double SCORE_BENCHMARK = BUFFER_SIZE_CONSTANT * BUFFER_RATE_MULTIPLIER;


    private static TrackingBuffer<SchedulingRequest> tBuffer = new TrackingBuffer<>(BUFFER_TRACKING_SIZE);

    public void processRequest(SchedulingRequest schedulingRequest) {
        tBuffer.push(schedulingRequest);
    }

    public boolean shouldProcess() {
        return shouldProcess(tBuffer.size(), tBuffer.getAverage());
    }

    public SRBatch getBatch() {
        return new SRBatch(tBuffer);
    }

    public static boolean shouldProcess(int bufferSize, double avgAddTime) {
        double score = bufferSize * 5 / avgAddTime ;
        return score > SCORE_BENCHMARK;
    }

}
