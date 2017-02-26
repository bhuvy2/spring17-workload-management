package org.apache.airavata.sga.scheduler.graph;

/**
 * Created by arvind on 2/26/17.
 */
public abstract class Vertex {

    protected int _vertexID;

    public abstract Object getMetadata();

    public int getVertexID() { return _vertexID; }

    @Override
    public int hashCode() {
        return _vertexID;
    }
}
