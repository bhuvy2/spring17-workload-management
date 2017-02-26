package org.apache.airavata.sga.scheduler.graph;

import org.apache.airavata.sga.commons.model.SchedulingRequest;

import java.util.*;

public class DiGraph {

    private HashMap<Vertex, ArrayList<Edge>> vertexEdges;
    private int numEdges = 0;

    public DiGraph(){
        vertexEdges = new HashMap<Vertex, ArrayList<Edge>>();
    }

    public DiGraph(HashMap<Vertex, ArrayList<Edge>> map) {
        vertexEdges = map;
    }

    public void addVertex(Vertex vertex){
        vertexEdges.put(vertex, new ArrayList<Edge>());
    }

    public void addEdge(JobVertex jv, WorkerVertex wv){
        Edge e = new Edge(jv, wv);
        vertexEdges.get(jv).add(e);
        numEdges++;
    }

    public void removeVertex(int vertex){
        vertexEdges.remove(vertex);
    }

    public void removeEdge(JobVertex jv, WorkerVertex wv) {
        ArrayList<Edge> edges = vertexEdges.get(jv);
        int oldSize = edges.size();
        if(edges.contains(wv)) {
            vertexEdges.get(jv).remove(wv);
            numEdges -= oldSize - vertexEdges.get(jv).size();
        }
    }

    public HashMap<Vertex, ArrayList<Edge>> getVertexEdges() { return vertexEdges; }

    public ArrayList<Edge> getEdges() {
        return vertexEdges.entrySet().stream().map(entry -> entry.getValue()).flatMap(listContainer -> listContainer.)
    }

    private int findPath(Vertex jobVertex, Vertex workerVertex, ArrayList<Edge> path) {

        if(jobVertex.equals(workerVertex))
            return 0;

        for(int i = 0; i < vertexEdges.get(jobVertex).size(); i++) {
            Edge e = vertexEdges.get(jobVertex).get(i);
            if(!e.isFull) {
                if(findPath(e.worker, workerVertex, path) == 0) {
                    path.add(e);
                    return 0;
                }
            }
        }

        return -1;
    }

    public HashSet<Edge> maxFlow(Vertex source, Vertex target){
        HashSet<Edge> edges = new HashSet<Edge>();
        ArrayList<Edge> path = new ArrayList<Edge>();

        findPath(source, target, path);

        while(!path.isEmpty()) {

            while(!path.isEmpty()) {
                path.get(0).isFull = true;
                edges.add(path.remove(0));
            }
            findPath(source, target, path);
        }

        return edges;
    }

    // clarify which identifier for routing queues
    public static ArrayList<SchedulingRequest> convertToRequests(Collection<Edge> edges) {
        edges.stream().map()
    }

    public int numVertexes(){
        return vertexEdges.size();
    }

    public int numEdges(){
        return numEdges;
    }
}