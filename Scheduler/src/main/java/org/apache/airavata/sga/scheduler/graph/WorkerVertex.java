package org.apache.airavata.sga.scheduler.graph;

import org.apache.airavata.sga.scheduler.gossip.Worker;

import java.util.Objects;

/**
 * Created by arvind on 2/26/17.
 */
public class WorkerVertex extends Vertex{

    private Worker _metadata;

    public WorkerVertex(int vertexID, Worker worker) {
        _metadata = worker;
        _vertexID = vertexID;
    }

    @Override
    public Worker getMetadata() {
        return _metadata;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_metadata.getHostName(), _metadata.getJobTypes(), _metadata.getMachineType(), _metadata.getPortNumber());
    }
}
