package org.apache.airavata.sga.scheduler.gossip;

/**
 * Created by arvind on 2/26/17.
 */
public class WorkerGathererSession {

    public WorkerGatherer workerGatherer;
    WorkerClusterInformation workerClusterInformation;

    private WorkerGathererSession(WorkerClusterInformation workerClusterInformation) {
        this.workerClusterInformation = workerClusterInformation;
    }

    public static WorkerGathererSession getSession(WorkerClusterInformation workerClusterInformation) throws
            WorkerAccessException {
        WorkerGathererSession session = new WorkerGathererSession(workerClusterInformation);
        boolean getData = session.refreshWorkerInformation();
        if(getData) {
            return session;
        } else {
            throw new WorkerAccessException("Unable to fetch required worker information from cluster");
        }
    }

    /**
     * Connect to cluster and update status info
     * @return update succeeded - true or false
     */
    public boolean refreshWorkerInformation() {
        try {
            workerGatherer = WorkerGatherer.getWorkerGatherer(workerClusterInformation.getInformation());
            return true;
        } catch(WorkerAccessException wae) {
            return false;
        }
    }

}
