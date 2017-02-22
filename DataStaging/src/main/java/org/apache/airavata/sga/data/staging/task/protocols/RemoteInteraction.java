package org.apache.airavata.sga.data.staging.task.protocols;

import org.apache.airavata.sga.data.staging.task.exception.SSHException;

/**
 * Created by Ajinkya on 2/21/17.
 */
public interface RemoteInteraction {

    public String write(String localFile, String remoteFile) throws SSHException;

    public String read(String remoteFile, String localFile) throws SSHException;

    public String thirdPartyTransfer(String sourceFile, String destinationFile, boolean ignoreEmptyFile) throws SSHException;

    public String makeDirectory(String path) throws SSHException;
}
