package org.apache.airavata.graph;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.airavata.graph.Edge;

public class DiGraph {
	
	private HashMap<Integer, ArrayList<Edge>> verticies;
	private int numEdges = 0;
	
	public DiGraph(){
		verticies = new HashMap<Integer, ArrayList<Edge>>();
	}
	
	public void addVertex(int vertex){
			verticies.put(vertex, new ArrayList<Edge>());
	}
	
	public void addEdge(int vertex1, int vertex2){
		Edge e = new Edge(vertex1, vertex2);
		verticies.get(vertex1).add(e);
		numEdges++;
	}
	
	public void removeVertex(int vertex){
		verticies.remove(vertex);
	}
	
	public void removeEdge(int vertex1, int vertex2){
		for(int i = 0; i < verticies.get(vertex1).size(); i++) {
			Edge e = verticies.get(vertex1).get(i);
			if(e.job == vertex1 && e.worker == vertex2) {
				verticies.get(vertex1).remove(i);
				numEdges--;
			}
		}
	}
	
	private int findPath(int job, int worker, ArrayList<Edge> path) {

		if(job == worker)
			return 0;
		
		for(int i = 0; i < verticies.get(job).size(); i++) {
			Edge e = verticies.get(job).get(i);
			int residual = e.capacity - e.flow;
			if((residual > 0) && !(path.contains(e))) {
				path.add(e);
				if(findPath(e.worker, worker, path) == 0)
					return 0;
			}
		}
		
		return -1;
	}
	
	public HashSet<Edge> maxFlow(int source, int target){
		HashSet<Edge> edges = new HashSet<Edge>();
		ArrayList<Edge> path = new ArrayList<Edge>();
		findPath(source, target, path);
		
		while(!path.isEmpty()) {
			int minResidual = path.get(0).capacity - path.get(0).flow;
			
			for(int i = 1; i < path.size(); i++) {
				int residual = path.get(i).capacity - path.get(i).flow;
				if(residual < minResidual)
					minResidual = residual;
			}
			
			while(!path.isEmpty()) {
				path.get(0).flow += minResidual;
				edges.add(path.remove(0));
			}
			
			findPath(source, target, path);
		}
		
		return edges;
	}
	
	public int numVertexes(){
		return verticies.size();
	}
	
	public int numEdges(){
		return numEdges;
	}
}
