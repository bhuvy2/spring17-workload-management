package org.apache.airavata.sga.scheduler.load;

import org.apache.airavata.graph.*;
import java.util.Queue; 

/**
 * @author Hackillinois 2017
 *
 */

 class FaultTolerance {
	private HashMap <Worker,String> dead;
	
	static Queue<String> waitingjobs;
	waitingjobs = new Queue<String>();
	static Queue ongoingjobs =;
	
	

	private DiGraph matching;
	
	private class Job {
		/* Stuff goes here */
		/* String label */
	}
	
	public void ReserveJob(Worker w){
		if(w.getworkerstate == "failed"){
			waitingjobs.add();
		}
	}
	
	
	
	
	public void PutbackJob(Queue waitingjobs, Queue ongoingjobs){
		
	}
	

	public boolean GoodtoGo(){
		
		
		
		
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
