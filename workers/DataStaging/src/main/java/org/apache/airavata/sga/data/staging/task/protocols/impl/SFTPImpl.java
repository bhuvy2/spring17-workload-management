package org.apache.airavata.sga.data.staging.task.protocols.impl;

import com.jcraft.jsch.*;
import org.apache.airavata.sga.data.staging.task.entity.ServerInfo;
import org.apache.airavata.sga.data.staging.task.exception.SSHException;
import org.apache.airavata.sga.data.staging.task.protocols.RemoteInteraction;
import org.apache.airavata.sga.data.staging.task.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Ajinkya on 2/16/17.
 */
public class SFTPImpl implements RemoteInteraction {

    private static final Logger logger = LoggerFactory.getLogger(SSHImpl.class);

    private Session srcSession = null;
    private Session session = null;

    public SFTPImpl(ServerInfo serverInfo) throws JSchException {
        session = Factory.getSSHSession(serverInfo);
    }

    public SFTPImpl(ServerInfo srcServerInfo, ServerInfo destServerInfo) throws JSchException {
        srcSession = Factory.getSSHSession(srcServerInfo);
        session = Factory.getSSHSession(destServerInfo);
    }

    @Override
    public String write(String localFile, String remoteFile) throws SSHException {

        logger.info("read() -> Transferring file. From  : " + localFile + ", To : " + remoteFile);

        Channel channel = null;

        try {

            if(null != session && !session.isConnected()){
                session.connect();
            }

            channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp channelSftp = (ChannelSftp) channel;

            channelSftp.put(localFile, remoteFile);

        } catch (SftpException | JSchException e) {
            logger.error("write() -> Error transferring file. From  : " + localFile + ", To : " + remoteFile, e);
            throw new SSHException("Error transferring file. From  : " + localFile + ", To : " + remoteFile);

        } finally {

            if (null != channel) {
                channel.disconnect();
            }
            if( null != session && session.isConnected() ){
                logger.info("write() -> Terminating session. From  : " + localFile + ", To : " + remoteFile);
                session.disconnect();
            }
        }

        return remoteFile;
    }

    @Override
    public String read(String remoteFile, String localFile) throws SSHException {

        logger.info("read() -> Transferring file. From  : " + localFile + ", To : " + remoteFile);

        Channel channel = null;
        try {

            if(null != session && !session.isConnected()){
                session.connect();
            }
            channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp channelSftp = (ChannelSftp) channel;

            channelSftp.get(remoteFile, localFile);

        } catch (SftpException | JSchException e) {

            logger.error("read() -> Error transferring file. From  : " + localFile + ", To : " + remoteFile, e);
            throw new SSHException("Error transferring file. From  : " + localFile + ", To : " + remoteFile);

        } finally {
            if (null != channel) {
                channel.disconnect();
            }
            if( null != session && session.isConnected() ){
                logger.info("read() -> Terminating session. From  : " + localFile + ", To : " + remoteFile);
                session.disconnect();
            }
        }
        return remoteFile;
    }

    @Override
    public String thirdPartyTransfer(String sourceFile, String destinationFile, boolean ignoreEmptyFile) throws SSHException {
        Channel srcChannel = null;
        Channel destChannel = null;
        InputStream in = null;
        OutputStream out = null;

        logger.info("thirdPartyTransfer() -> Transferring file. From  : " + sourceFile + ", To : " + destinationFile);

        try {

            if(null != srcSession && !srcSession.isConnected()){
                srcSession.connect();
            }
            srcChannel = srcSession.openChannel("sftp");

            if(null != session && !session.isConnected()){
                session.connect();
            }
            destChannel = session.openChannel("sftp");

            ChannelSftp channelSrcSftp = (ChannelSftp) srcChannel;
            ChannelSftp channelDestSftp = (ChannelSftp) destChannel;

            srcChannel.connect();
            destChannel.connect();

            in = channelSrcSftp.get(sourceFile);
            out = channelDestSftp.put(destinationFile);

            byte[] buffer = new byte[1024];
            int len;

            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

        } catch (SftpException | JSchException | IOException e) {
            logger.error("thirdPartyTransfer() -> Error transferring file. From  : " + sourceFile + ", To : " + destinationFile, e);
            throw new SSHException("Error transferring file. From  : " + sourceFile + ", To : " + destinationFile);
        } finally {

            try{
                if (null != in)in.close();
                if (null != out)out.close();
            }catch (IOException e){
                logger.error("thirdPartyTransfer() -> Error closing streams. From  : " + sourceFile + ", To : " + destinationFile, e);
            }

            if (null != srcChannel)srcChannel.disconnect();
            if( null != srcSession && srcSession.isConnected() ){
                logger.info("thirdPartyTransfer() -> Terminating session. From  : " + sourceFile + ", To : " + destinationFile);
                srcSession.disconnect();
            }

            if (null != destChannel)destChannel.disconnect();
            if( null != session && session.isConnected() ){
                logger.info("thirdPartyTransfer() -> Terminating session. From  : " + sourceFile + ", To : " + destinationFile);
                session.disconnect();
            }
        }
        return destinationFile;
    }

    @Override
    public String makeDirectory(String path) throws SSHException {

        logger.info("makeDirectory() -> Creating directory. Path : " + path);

        Channel channel = null;
        try {
            if(null != session && !session.isConnected()){
                session.connect();
            }
            channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp channelSftp = (ChannelSftp) channel;

            channelSftp.mkdir(path);

        } catch (SftpException | JSchException e) {

            logger.error("makeDirectory() -> Error crating directory. Path  : " + path, e);
            throw new SSHException("Error crating directory. Path  : " + path);

        } finally {
            if (null != channel) {
                channel.disconnect();
            }
            if( null != session && session.isConnected() ){
                logger.info("makeDirectory() -> Terminating session. Create Directory : " + path);
                session.disconnect();
            }
        }
        return path;
    }

}
