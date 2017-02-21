package org.apache.airavata.sga.task.protocols;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.airavata.sga.task.entity.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by Ajinkya on 2/16/17.
 */
public class Factory {

    private static final Logger logger = LoggerFactory.getLogger(Factory.class);

    public static Session getSSHSession(ServerInfo serverInfo) throws JSchException {

        logger.info("getSSHSession() -> Creating ssh session. User : " + serverInfo.getUserName());

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");

        JSch jsch = new JSch();

        jsch.addIdentity(serverInfo.getPrivateKey());
        Session session = jsch.getSession(serverInfo.getUserName(), serverInfo.getHost(), serverInfo.getPort());

        session.setConfig(config);
        return session;
    }
}
