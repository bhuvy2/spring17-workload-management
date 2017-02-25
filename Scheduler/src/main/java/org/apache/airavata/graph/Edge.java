package org.apache.airavata.graph;

public class Edge{
	int job;
	int worker;
	int capacity = 1;
	int flow = 0;
	boolean isSOrT = false;
	
	public Edge(int j, int w) {
		job = j;
		worker = w;
	}
	
	public Edge(int j, int w, boolean SOrT) {
		job = j;
		worker = w;
		isSOrT = SOrT;
	}
}
