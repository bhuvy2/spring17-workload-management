package org.apache.airavata.sga.data.staging.task.util;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.airavata.sga.commons.model.DataTransferProtocol;
import org.apache.airavata.sga.data.staging.task.entity.ServerInfo;
import org.apache.airavata.sga.data.staging.task.protocols.RemoteInteraction;
import org.apache.airavata.sga.data.staging.task.protocols.impl.SFTPImpl;
import org.apache.airavata.sga.data.staging.task.protocols.impl.SSHImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by Ajinkya on 2/16/17.
 */
public class Factory {

    private static final Logger logger = LoggerFactory.getLogger(Factory.class);

    public static Session getSSHSession(ServerInfo serverInfo) throws JSchException {

        logger.info("getSSHSession() -> Creating ssh session. User : " + serverInfo.getUserName() + ", Host : " + serverInfo.getHost());

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");

        JSch jsch = new JSch();

        jsch.addIdentity(serverInfo.getPrivateKey());
        Session session = jsch.getSession(serverInfo.getUserName(), serverInfo.getHost(), serverInfo.getPort());

        session.setConfig(config);

        logger.debug("getSSHSession() -> Session created. User : " + serverInfo.getUserName() + ", Host : " + serverInfo.getHost());

        return session;
    }

    public static RemoteInteraction getRemoteInteraction(ServerInfo targetMachine, DataTransferProtocol dtProtocol) throws JSchException {

        logger.info("getRemoteInteraction() -> Setting up remote interaction. Target : " + targetMachine.getHost());

        RemoteInteraction remoteInteraction = null;

        if(dtProtocol == DataTransferProtocol.SCP){
            remoteInteraction = new SSHImpl(targetMachine);

        }else if(dtProtocol == DataTransferProtocol.SFTP){
            remoteInteraction = new SFTPImpl(targetMachine);
        }

        return remoteInteraction;
    }

    public static RemoteInteraction getRemoteInteraction(ServerInfo sourceMachine, ServerInfo targetMachine, DataTransferProtocol dtProtocol) throws JSchException {
        logger.info("getRemoteInteraction() -> Setting up remote interaction. Target : " + targetMachine.getHost());

        RemoteInteraction remoteInteraction = null;

        if(dtProtocol == DataTransferProtocol.SCP){
            remoteInteraction = new SSHImpl(sourceMachine, targetMachine);

        }else if(dtProtocol == DataTransferProtocol.SFTP){
            remoteInteraction = new SFTPImpl(sourceMachine, targetMachine);
        }

        return remoteInteraction;
    }
}
