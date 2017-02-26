package org.apache.airavata.sga.scheduler.load;

import org.apache.airavata.graph.*;

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
		/* String label */
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
	}
	
	public void addWorker(int worker){
		//Add edge to graph
	}
	
	public void removeWorker(int worker){
		//Remove Worker
	}
	
	
	public void addJob(Job job){
		// Add to graph
	}
	
	public void removeJob(Job job){
		// Removing from the graph.
	}
	
}
