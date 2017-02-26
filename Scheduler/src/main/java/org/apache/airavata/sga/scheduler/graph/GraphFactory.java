package org.apache.airavata.sga.scheduler.graph;

import org.apache.airavata.sga.commons.model.MachineType;
import org.apache.airavata.sga.commons.model.SchedulingRequest;
import org.apache.airavata.sga.commons.model.TargetMachine;
import org.apache.airavata.sga.scheduler.gossip.*;
import org.apache.airavata.sga.scheduler.messaging.SRBatch;
import org.apache.airavata.sga.scheduler.messaging.SchedulerMessagingFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Created by arvind on 2/26/17.
 */
public class GraphFactory {

    WorkerGathererSession WGSession = null;

    private static final Logger logger = LogManager.getLogger(GraphFactory.class);

    public GraphFactory() {}

    public void initSession(WorkerClusterInformation wci) {
        try {
            WGSession = WorkerGathererSession.getSession(wci);
        } catch (WorkerAccessException wae) {
            logger.error("initSession() -> unable to connect to worker cluster.");
        }
    }

    public DiGraph getDiGraph(SRBatch batch) {
        Iterator it = batch.iterator();
        HashMap<Vertex, ArrayList<Edge>> vertices = new HashMap<>();
        int i = 0;
        while(it.hasNext()) {
            SchedulingRequest request = (SchedulingRequest) it.next();
            JobVertex jVert = new JobVertex(i,request);
            TargetMachine targetMachine = request.getTaskContext().getTargetMachine();
            String hostName = targetMachine.getHostname();
            int portNumber = targetMachine.getPort();
            MachineType machineType = targetMachine.getMachineType();
            String jobType = request.getTaskContext().getQueueName();
            final int sourceVertex = i;
            ArrayList<WorkerVertex> workerVertices = WGSession.workerGatherer.matchWorkers(jobType).
                    stream().map(worker -> new WorkerVertex(sourceVertex, worker)).collect(Collectors.toCollection
                    (ArrayList::new));
            for(WorkerVertex wv : workerVertices) {
                vertices.put(wv, new ArrayList<>());
            }
            ArrayList<Edge> edges = workerVertices.stream().
                    map(workerVertex -> new Edge(jVert, workerVertex))
                    .collect(Collectors.toCollection(ArrayList::new));
            vertices.put(jVert, edges);
            i++;
        }
        DiGraph diGraph = new DiGraph(vertices);
        return diGraph;
    }


}
