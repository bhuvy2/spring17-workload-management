package org.apache.airavata.sga.data.staging.task.cluster.impl;

import com.jcraft.jsch.JSchException;
import org.apache.airavata.sga.commons.model.DataTransferProtocol;
import org.apache.airavata.sga.commons.model.TargetMachine;
import org.apache.airavata.sga.data.staging.task.cluster.RemoteCluster;
import org.apache.airavata.sga.data.staging.task.entity.ServerInfo;
import org.apache.airavata.sga.data.staging.task.exception.RemoteClusterException;
import org.apache.airavata.sga.data.staging.task.exception.SSHException;
import org.apache.airavata.sga.data.staging.task.protocols.RemoteInteraction;
import org.apache.airavata.sga.data.staging.task.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Ajinkya on 2/21/17.
 */
public class RemoteClusterImpl implements RemoteCluster {

    private static final Logger logger = LoggerFactory.getLogger(RemoteClusterImpl.class);

    private static final int MAX_RETRY_COUNT = 3;

    private RemoteInteraction remoteInteraction;

    private ServerInfo targetMachine;
    private ServerInfo sourceMachine;

    public RemoteClusterImpl(ServerInfo targetMachine, DataTransferProtocol dtProtocol) throws RemoteClusterException {
        try {
            this.targetMachine = targetMachine;
            this.remoteInteraction = Factory.getRemoteInteraction(targetMachine, dtProtocol);
        } catch (JSchException e) {
            logger.error("RemoteClusterImpl() -> Error contacting target. Target : " + targetMachine.getHost(), e);
            throw new RemoteClusterException("Error contacting target. Target : " + targetMachine.getHost());
        }
    }

    public RemoteClusterImpl(ServerInfo sourceMachine, ServerInfo targetMachine, DataTransferProtocol dtProtocol) throws RemoteClusterException {
        try {
            this.sourceMachine = sourceMachine;
            this.targetMachine = targetMachine;
            this.remoteInteraction = Factory.getRemoteInteraction(sourceMachine, targetMachine, dtProtocol);
        } catch (JSchException e) {
            logger.error("RemoteClusterImpl() -> Error contacting remote machines. Source : " + sourceMachine.getHost() + ", Target : " + targetMachine.getHost(), e);
            throw new RemoteClusterException("Error contacting remote machines. Source : " + sourceMachine.getHost() + ", Target : " + targetMachine.getHost());
        }
    }


    @Override
    public String write(String localFile, String remoteFile) throws RemoteClusterException {
        int retry = 3;
        while (retry > 0) {
            try {
                logger.info("write() -> Transferring localhost:" + localFile  + " to " + targetMachine.getHost() + ":" + remoteFile);
                remoteInteraction.write(localFile, remoteFile);
                retry = 0;
            } catch (SSHException e) {
                retry--;
                if (retry == 0) {
                    throw new RemoteClusterException("Failed to transfer localhost:" + localFile + " to " + targetMachine.getHost() +
                            ":" + remoteFile, e);
                } else {
                    logger.info("write() -> Retry transfer localhost:" + localFile + " to " + targetMachine.getHost() + ":" +
                            remoteFile);
                }
            }
        }
        return remoteFile;
    }

    @Override
    public String read(String remoteFile, String localFile) throws RemoteClusterException {
        int retry = 3;
        while(retry>0) {
            try {
                logger.info("read() -> Transferring " + targetMachine.getHost() + ":" + remoteFile + " To localhost : " + localFile);
                remoteInteraction.read(remoteFile, localFile);
                retry=0;
            } catch (SSHException e) {
                retry--;
                if (retry == 0) {
                    throw new RemoteClusterException("Failed to transfer " + targetMachine.getHost() + ":" + remoteFile + " to " +
                            "localhost:" + localFile, e);
                } else {
                    logger.info("read() -> Retry transfer " + targetMachine.getHost() + ":" + remoteFile + "  to localhost:" + localFile);
                }
            }
        }
        return remoteFile;
    }

    @Override
    public String thirdPartyTransfer(String sourceFile, String destinationFile, boolean ignoreEmptyFile) throws RemoteClusterException {
        int retryCount= 0;
        try {
            while (retryCount < MAX_RETRY_COUNT) {
                retryCount++;
                logger.info("thirdPartyTransfer() -> Transferring from : " + targetMachine.getHost() + " : " + sourceFile + " To: " + destinationFile);
                try {
                    remoteInteraction.thirdPartyTransfer(sourceFile, destinationFile, ignoreEmptyFile);
                    break; // exit while loop
                } catch (SSHException e) {
                    if (retryCount == MAX_RETRY_COUNT) {
                        logger.error("Retry count " + MAX_RETRY_COUNT + " exceeded for  transferring from:"
                                + sourceFile + " To: " + destinationFile, e);
                        throw e;
                    }
                    logger.error("thirdPartyTransfer() -> Retry transferring from: " + sourceMachine.getHost() + ":" + sourceFile + " To: " + targetMachine.getHost() + ":" + destinationFile, e);
                }
            }
        } catch (SSHException e) {
            throw new RemoteClusterException("Failed scp file:" + sourceFile + " to remote file "
                    +destinationFile , e);
        }
        return destinationFile;
    }


    @Override
    public String makeDirectory(String directoryPath) throws RemoteClusterException {
        int retryCount = 0;
        try {
            while (retryCount < MAX_RETRY_COUNT) {
                retryCount++;
                logger.info("makeDirectory() -> Creating directory: " + targetMachine.getHost() + ":" + directoryPath);
                try {
                    remoteInteraction.makeDirectory(directoryPath);
                    break;  // Exit while loop
                } catch (SSHException e) {
                    if (retryCount == MAX_RETRY_COUNT) {
                        logger.error("makeDirectory() -> Retry count " + MAX_RETRY_COUNT + " exceeded for creating directory: "
                                + targetMachine.getHost() + ":" + directoryPath, e);

                        throw e;
                    }
                    logger.error("makeDirectory() -> Issue with jsch, Retry creating directory: " + targetMachine.getHost() + ":" + directoryPath);
                }
            }
        } catch (SSHException e) {
            throw new RemoteClusterException("Failed to create directory " + targetMachine.getHost() + ":" + directoryPath, e);
        }
        return directoryPath;
    }
}
