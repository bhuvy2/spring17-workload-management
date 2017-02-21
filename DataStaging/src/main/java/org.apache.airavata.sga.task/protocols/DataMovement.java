package org.apache.airavata.sga.task.protocols;

import com.jcraft.jsch.JSchException;
import org.apache.airavata.sga.task.exception.SSHException;

import java.io.IOException;

/**
 * Created by Ajinkya on 2/16/17.
 */
public interface DataMovement {

    public String write(String localFile, String remoteFile) throws SSHException;

    public String read(String remoteFile, String localFile) throws SSHException;

    public String thirdPartyTransfer(String sourceFile, String destinationFile, boolean ignoreEmptyFile) throws SSHException;

    public String makeDirectory(String path) throws SSHException;

}
