package org.apache.airavata.sga.scheduler.gossip;

import java.util.ArrayList;

/**
 * Created by arvind on 2/26/17.
 */
public interface WorkerClusterInformation {

    public ArrayList<Worker> getInformation() throws WorkerAccessException;

}
