package org.apache.airavata.sga.scheduler.load;

import org.apache.airavata.graph.*;

import java.util.HashSet;
import java.util.Iterator;

/**
 * @author Hackillinois 2017
 *
 */
public class LoadBalancer {

	/**
	 * If a worker is in this graph it is free
	 * If a job is in this graph, it is not being
	 * worked on by any worker.
	 */
	private DiGraph matching;
	
	private class Job {
		/* Stuff goes here */
		String label;
	}
	
	public LoadBalancer(){
		matching = new DiGraph();
	}
	
	public void scheduleJobs(){
		/*
		 * Grab the worker availability from the gossip
		 * 	module
		 * Update graph
		 * Take the graph and find the max flow
		 * Discard s or t edges
		 * For each other edge:
		 * 		Send message to worker # to run job
		 * 		Register _callback_ to remove the vertex
		 */
		for w in workers:
			if(w.getState() == "Available")
				addWorker(w);
		
		HashSet<Edge> edges = matching.maxFlow(START_LABEL,END_LABEL);
		
		Iterator it = edges.Iterator();
		while(it.hasNext()) {
			Edge e = it.next();
			if(START_LABEL != e.job && END_LABEL != e.worker) {
				workerRunJob(e.worker, e.job);
				
			}
		}
		
		
	}
	
	public void addWorker(String worker){
		//Add edge to graph
		matching.addVertex(worker);
		matching.addEdge(worker, END_LABEL);
		// array a = getWorkerCapabilities(worker);
		// for job in a:
		// addEdge(job, worker);
	}
	
	public void removeWorker(String worker){
		//Remove Worker
		matching.removeVertex(worker);
	}
	
	
	public void addJob(Job job){
		// Add to graph
		matching.addVertex(job.label);
		matching.addEdge(START_LABEL, job.label);
		// For each worker:
		// if worker can handle job
		// addEdge(job, worker)
	}
	
	public void removeJob(Job job){
		// Removing from the graph.
		matching.removeVertex(job.label);
	}
	
}
