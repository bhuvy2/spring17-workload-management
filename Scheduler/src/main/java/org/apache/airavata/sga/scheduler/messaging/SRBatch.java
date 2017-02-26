package org.apache.airavata.sga.scheduler.messaging;

import org.apache.airavata.sga.commons.model.SchedulingRequest;
import org.apache.airavata.sga.messaging.service.util.MessageContext;
import org.apache.airavata.sga.scheduler.messaging.buffer.Buffer;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by arvind on 2/26/17.
 */
public class SRBatch {

    Buffer<SchedulingRequest> batch;

    public SRBatch(Buffer<SchedulingRequest> newBatch) {
        this.batch = newBatch;
    }

    public Iterator<SchedulingRequest> iterator() {
        return batch.iterator();
    }

}
