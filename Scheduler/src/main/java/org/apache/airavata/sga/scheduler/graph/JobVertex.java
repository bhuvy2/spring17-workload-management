package org.apache.airavata.sga.scheduler.graph;

import org.apache.airavata.sga.commons.model.SchedulingRequest;

import java.util.Objects;

/**
 * Created by arvind on 2/26/17.
 */
public class JobVertex extends Vertex {

    private SchedulingRequest _metadata;

    public JobVertex(int vertexID, SchedulingRequest request) {
        _metadata = request;
        _vertexID = vertexID;
    }

    @Override
    public SchedulingRequest getMetadata() {
        return _metadata;
    }

    @Override
    public int hashCode() {
        return _metadata.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof JobVertex) {
            JobVertex objJobVertex = (JobVertex) obj;
            return _metadata.equals(objJobVertex._metadata);
        }
        return false;
    }
}
