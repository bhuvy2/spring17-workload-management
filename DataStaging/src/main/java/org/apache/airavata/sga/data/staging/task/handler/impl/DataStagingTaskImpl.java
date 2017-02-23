package org.apache.airavata.sga.data.staging.task.handler.impl;

import org.apache.airavata.sga.commons.model.OperationFailedException;
import org.apache.airavata.sga.commons.model.Response;
import org.apache.airavata.sga.commons.model.Status;
import org.apache.airavata.sga.commons.model.TaskContext;
import org.apache.airavata.sga.commons.task.CommonTask;
import org.apache.airavata.sga.data.staging.task.cluster.RemoteCluster;
import org.apache.airavata.sga.data.staging.task.cluster.impl.RemoteClusterImpl;
import org.apache.airavata.sga.data.staging.task.entity.ServerInfo;
import org.apache.airavata.sga.data.staging.task.exception.RemoteClusterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ajinkya on 2/21/17.
 */
public class DataStagingTaskImpl implements CommonTask {

    private static final Logger logger = LoggerFactory.getLogger(DataStagingTaskImpl.class);

    @Override
    public void init() throws OperationFailedException {

    }

    @Override
    public Response execute(TaskContext taskContext) throws OperationFailedException {

        logger.info("execute() -> Staging file. Experiment Id : " + taskContext.getExperiment().getExperimentId());

        Response response = new Response();
        response.setExperimentId(taskContext.getExperiment().getExperimentId());

        try {

            if(taskContext.getTargetMachine().getDtProtocol() != taskContext.getLocalStorage().getDtProtocol()){
                throw new RemoteClusterException("We do not support cross protocol transfer yet.");
            }

            ServerInfo targetMachine = new ServerInfo(taskContext.getTargetMachine().getLoginId(), taskContext.getTargetMachine().getHostname(), "", taskContext.getTargetMachine().getPort());
            ServerInfo localMachine = new ServerInfo(taskContext.getLocalStorage().getLoginId(), taskContext.getLocalStorage().getHostname(), "", taskContext.getLocalStorage().getPort());

            RemoteCluster remoteCluster = new RemoteClusterImpl(localMachine, targetMachine, taskContext.getTargetMachine().getDtProtocol());
            remoteCluster.thirdPartyTransfer(taskContext.getLocalStorage().getScratchDir(), taskContext.getTargetMachine().getScratchDir(), true);

            logger.info("execute() -> File transferred successfully. Experiment Id : " + taskContext.getExperiment().getExperimentId() + ", Source : " + taskContext.getLocalStorage().getScratchDir() + ", Destination : " + taskContext.getTargetMachine().getScratchDir());
            response.setStatus(Status.OK);
            response.setMessage("File staged successfully.");

        } catch (RemoteClusterException e) {

            logger.error("execute() ->Error staging file. Experiment Id : " + taskContext.getExperiment().getExperimentId(), e);

            response.setStatus(Status.FAILED);
            response.setMessage("Error staging file.");
        }
        return response;
    }

    @Override
    public void postExecute() throws OperationFailedException {

    }
}
