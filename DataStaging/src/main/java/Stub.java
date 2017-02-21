
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.airavata.sga.data.staging.task.entity.ServerInfo;
import org.apache.airavata.sga.data.staging.task.exception.SSHException;
import org.apache.airavata.sga.data.staging.task.protocols.DataMovement;
import org.apache.airavata.sga.data.staging.task.protocols.impl.SSHTransfer;
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
        Session session = null;

        try{

            DataMovement tp = new SSHTransfer(serverInfo);
            tp.write(local, remoteDir);
            logger.debug("main() -> File moved to remote server. User : " + serverInfo.getUserName() + ", Remote : " + serverInfo.getHost());

        }catch ( JSchException | SSHException e) {
            logger.error("Error moving file. User : " + serverInfo.getUserName() + ", Remote : " + serverInfo.getHost(), e);
        }finally {

            if( null != session && session.isConnected()){
                logger.info("main() -> Terminating session. User : " + serverInfo.getUserName() + ", Remote : " + serverInfo.getHost());
                session.disconnect();
            }
        }

    }
}
