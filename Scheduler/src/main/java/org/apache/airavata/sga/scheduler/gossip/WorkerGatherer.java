package org.apache.airavata.sga.scheduler.gossip;

import org.apache.airavata.sga.commons.model.MachineType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class WorkerGatherer {
	
	private ArrayList<Worker> workers;

	private WorkerGatherer(Collection<Worker> workers) {
		workers = new ArrayList<>(workers);
	}

	public boolean receiveMessage(int msg){
		/*
		 * Update the state in the Arraylist
		 * Cancel previous callback to remove node
		 * Register new callback to remove node
		 */



		return false;
	}

	public void addWorker(Worker worker) {
		workers.add(worker);
	}

	public void removeWorker(Worker worker){
		workers.remove(worker);
	}

	// note: clarify which term defines jobType
	public ArrayList<Worker> matchWorkers(String jobType) {
		return workers.stream().
				filter(worker -> worker.jobTypes.contains(jobType)).collect(Collectors.toCollection(ArrayList::new));
	}

	public Worker getWorker(String hostName, int portNumber, MachineType machineType) {
		for(Worker worker : workers) {
			if(worker.hostName.equals(hostName) && worker.portNumber == portNumber &&
					worker.machineType == machineType) {
				return worker;
			}
		}
		return null;
	}
	
	public ArrayList<Worker> getWorkers(){
		return workers;
	}

	/**
	 * Assemble the worker
	 * @return
	 */
	public static WorkerGatherer getWorkerGatherer(Collection<Worker> workers) {
		// build worker info into arraylist of workers
		ArrayList<Worker> workerList = new ArrayList<>(workers);
		return new WorkerGatherer(workers);
	}

}
