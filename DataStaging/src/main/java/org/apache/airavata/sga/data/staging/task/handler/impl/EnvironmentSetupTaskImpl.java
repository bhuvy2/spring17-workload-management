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

import java.util.Arrays;

/**
 * Created by Ajinkya on 2/21/17.
 */
public class EnvironmentSetupTaskImpl implements CommonTask {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentSetupTaskImpl.class);

    @Override
    public void init() throws OperationFailedException {

    }

    @Override
    public Response execute(TaskContext taskContext) throws OperationFailedException {

        logger.info("execute() -> Creating environment. Experiment Id : " + taskContext.getExperiment().getExperimentId());

        Response response = new Response();
        response.setExperimentId(taskContext.getExperiment().getExperimentId());

        try {

            ServerInfo targetMachine = new ServerInfo(taskContext.getTargetMachine().getLoginId(), taskContext.getTargetMachine().getHostname(), "", taskContext.getTargetMachine().getPort());

            RemoteCluster remoteCluster = new RemoteClusterImpl(targetMachine, taskContext.getTargetMachine().getDtProtocol());
            remoteCluster.makeDirectory(taskContext.getTargetMachine().getScratchDir());

            logger.info("execute() -> Environment created successfully. Experiment Id : " + taskContext.getExperiment().getExperimentId() + ", Remote directory : " + taskContext.getTargetMachine().getScratchDir());

            response.setStatus(Status.OK);
            response.setMessage("Environment created successfully.");

        } catch (RemoteClusterException e) {

            logger.error("execute() -> Error while environment setup. Experiment Id : " + taskContext.getExperiment().getExperimentId(), e);

            response.setStatus(Status.FAILED);
            response.setMessage("Failed to setup environment.");

        }
        return response;
    }

    @Override
    public void postExecute() throws OperationFailedException {

    }
}
