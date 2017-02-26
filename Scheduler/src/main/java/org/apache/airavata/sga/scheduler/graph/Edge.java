package org.apache.airavata.sga.scheduler.graph;

import org.apache.airavata.sga.commons.model.SchedulingRequest;
import org.apache.airavata.sga.scheduler.gossip.Worker;

public class Edge {
    JobVertex job;
    WorkerVertex worker;
    boolean isFull = false;
    boolean isSOrT = false;

    public Edge(JobVertex jv, WorkerVertex wv) {
        job = jv;
        worker = wv;
    }

    public Edge(JobVertex jv, WorkerVertex wv, boolean SOrT) {
        job = jv;
        worker = wv;
        isSOrT = SOrT;
    }

    // clarify which identifer identifies worker
    public static SchedulingRequest edgeToRequest(Edge edge) {
        SchedulingRequest request = edge.job.getMetadata().deepCopy();
        Worker worker = edge.worker.getMetadata();

        // check this line for accuracy + correctness
        // is hostname the correct worker attrib
        request.getTaskContext().setQueueName(worker.getHostName());
        return request;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Edge) {
            Edge objEdge = (Edge) obj;
            return objEdge.equals(this.job) && objEdge.equals(this.worker);
        }
        return false;
    }
}