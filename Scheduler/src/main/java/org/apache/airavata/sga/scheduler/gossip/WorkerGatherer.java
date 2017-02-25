package org.apache.airavata.sga.scheduler.gossip;

import java.util.ArrayList;

public class WorkerGatherer {
	
	public class Worker {
		ArrayList<String> JobTypes;
		int port;
		String ipAddress;
		String state;
	}
	
	private ArrayList<Worker> workers;
	
	public boolean receiveMessage(int msg){
		/*
		 * Update the state in the Arraylist
		 * Cancel previous callback to remove node
		 * Register new callback to remove node
		 */
		
		return false;
	}
	
	public void removeWorker(Worker wrk){
		/*
		 *  To be called later
		 */
	}
	
	public ArrayList<Worker> getWorkers(){
		/*
		 * I need some way of getting the worker
		 */
		return workers;
	}

}
