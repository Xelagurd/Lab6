package lab6;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;

public class CustomWatcher implements Watcher {
    private ZooKeeper zoo;

    public CustomWatcher(ZooKeeper zoo) {
        this.zoo = zoo;
    }

    @Override
    public void process(WatchedEvent event) {
        ArrayList<String> servers = new ArrayList<>();
        try {
            servers = zoo.getChildren(ZOO_KEEPER_SERVER_DIR, this);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        List<String> serversData = new ArrayList<>();
        getServersInfo(servers, serversData);
        storageActor.tell(new ServerMSG(serversData), ActorRef.noSender());
    }
}
