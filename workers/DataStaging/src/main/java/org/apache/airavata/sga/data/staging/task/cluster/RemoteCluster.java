package org.apache.airavata.sga.data.staging.task.cluster;

import org.apache.airavata.sga.data.staging.task.exception.RemoteClusterException;

/**
 * Created by Ajinkya on 2/21/17.
 */
public interface RemoteCluster {

    public String write(String localFile, String remoteFile) throws RemoteClusterException;

    public String read(String remoteFile, String localFile) throws RemoteClusterException;

    public String thirdPartyTransfer(String sourceFile, String destinationFile, boolean ignoreEmptyFile) throws RemoteClusterException;

    public String makeDirectory(String path) throws RemoteClusterException;

}
