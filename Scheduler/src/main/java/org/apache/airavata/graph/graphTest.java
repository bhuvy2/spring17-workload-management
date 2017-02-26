package org.apache.airavata.graph;

import org.apache.airavata.graph.DiGraph;
import java.util.HashSet;
import java.util.Iterator;

public class graphTest {
	public static void main(String [] args) {
		DiGraph graph = new DiGraph();
		
		graph.addVertex(-1);
		graph.addVertex(-2);
		for(int i = 1; i < 17; i++) {
			graph.addVertex(i);
		}
		
		for(int i = 1; i < 9; i++) {
			graph.addEdge(-1, i);
		}
		
		for(int i = 9; i < 17; i++) {
			graph.addEdge(i, -2);
		}
		
		for(int i = 9; i < 17; i++)
			graph.addEdge(1, i);
		
		graph.addEdge(2, 10);
		graph.addEdge(2, 12);
		graph.addEdge(2, 13);
		
		graph.addEdge(3, 13);
		graph.addEdge(3, 16);
		
		graph.addEdge(4, 9);
		graph.addEdge(4, 14);
		
		graph.addEdge(5, 15);
		
		graph.addEdge(6, 11);
		graph.addEdge(6, 14);
		graph.addEdge(6, 15);
		graph.addEdge(6, 16);
		
		graph.addEdge(7, 10);
		graph.addEdge(7, 11);
		graph.addEdge(7, 13);
		graph.addEdge(7, 14);
		graph.addEdge(7, 15);
		graph.addEdge(7, 16);
		
		graph.addEdge(8, 9);
		graph.addEdge(8, 10);
		graph.addEdge(8, 11);
		
		HashSet<Edge> edges = graph.maxFlow(-1, -2);
		
		Iterator<Edge> it = edges.iterator();
		while(it.hasNext()) {
			Edge e = it.next();
			if(e.job != -1 && e.worker != -2)
				System.out.printf("Edge from %d to %d\n", e.job, e.worker);
		}
	}
}