package org.apache.airavata.graph;

import org.apache.airavata.graph.DiGraph;
import java.util.HashSet;
import java.util.Iterator;

public class graphTest {
	public static void main(String [] args) {
		DiGraph graph = new DiGraph();
		
		graph.addVertex(-1);
		graph.addVertex(-2);
		for(int i = 1; i < 9; i++) {
			graph.addVertex(i);
		}
		
		for(int i = 1; i < 5; i++) {
			graph.addEdge(-1, i);
		}
		
		for(int i = 5; i < 9; i++) {
			graph.addEdge(i, -2);
		}
		
		graph.addEdge(1, 7);
		graph.addEdge(1, 4);
		graph.addEdge(2, 4);
		graph.addEdge(2, 5);
		graph.addEdge(2, 6);
		graph.addEdge(3, 5);
		
		HashSet<Edge> edges = graph.maxFlow(-1, -2);
		
		Iterator<Edge> it = edges.iterator();
		while(it.hasNext()) {
			Edge e = it.next();
			if(e.job != -1 && e.worker != -2)
				System.out.printf("Edge from %d to %d\n", e.job, e.worker);
		}
	}
}