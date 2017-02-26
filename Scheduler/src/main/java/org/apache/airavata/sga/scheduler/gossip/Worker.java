package org.apache.airavata.sga.scheduler.gossip;

import org.apache.airavata.sga.commons.model.MachineType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by arvind on 2/25/17.
 */
public class Worker {

    Set<String> jobTypes;
    int portNumber;
    String hostName;
    MachineType machineType;
    WorkerState state;

    public Worker(Set<String> jobTypes, int portNumber, String hostName, MachineType machineType) {
        this.jobTypes = jobTypes;
        this.portNumber = portNumber;
        this.hostName = hostName;
        this.state = WorkerState.DEFAULT;
        this.machineType = machineType;
    }

    public Worker(Set<String> jobTypes, int portNumber, String hostName, WorkerState state, MachineType machineType) {
        this.jobTypes = jobTypes;
        this.portNumber = portNumber;
        this.hostName = hostName;
        this.state = state;
        this.machineType = machineType;
    }

    public static Worker getWorker(Collection<String> jobTypesCollection, int portNumber, String ipAddress, String
            worker, MachineType machineType) {
        Set<String> jobTypes = new HashSet<String>(jobTypesCollection);
        return new Worker(jobTypes, portNumber, ipAddress, Enum.valueOf(WorkerState.class, worker), machineType);
    }

    public Set<String> getJobTypes() {
        return jobTypes;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public String getHostName() {
        return hostName;
    }

    public MachineType getMachineType() {
        return machineType;
    }

    public WorkerState getState() {
        return state;
    }
}
