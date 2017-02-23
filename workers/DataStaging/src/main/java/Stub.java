
import org.apache.airavata.sga.commons.model.DataTransferProtocol;
import org.apache.airavata.sga.data.staging.task.cluster.RemoteCluster;
import org.apache.airavata.sga.data.staging.task.cluster.impl.RemoteClusterImpl;
import org.apache.airavata.sga.data.staging.task.entity.ServerInfo;
import org.apache.airavata.sga.data.staging.task.exception.RemoteClusterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Ajinkya on 2/16/17.
 */
public class Stub {

    private static final Logger logger = LoggerFactory.getLogger(Stub.class);

    public static void main(String... args){
        String user = "adhamnas";
        String host = "iris.ils.indiana.edu";
        String privateKey = "/Users/Ajinkya/.ssh/id_rsa";
        int port = 22;
        String local = "/Users/Ajinkya/ajinkya/test/Docker.dmg";
        String remoteDir = "/home/adhamnas/www/test/";

        ServerInfo serverInfo = new ServerInfo(user, host, privateKey, port);

        try{

            RemoteCluster remoteCluster = new RemoteClusterImpl(serverInfo, DataTransferProtocol.SCP);
            remoteCluster.write(local, remoteDir);
            logger.debug("main() -> File moved to remote server. User : " + serverInfo.getUserName() + ", Remote : " + serverInfo.getHost());

        }catch ( RemoteClusterException e) {
            logger.error("Error moving file. User : " + serverInfo.getUserName() + ", Remote : " + serverInfo.getHost(), e);
        }

    }
}
