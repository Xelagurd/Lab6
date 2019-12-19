package lab6;

import akka.actor.ActorRef;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.List;

public class CustomWatcher implements Watcher {
    private ZooKeeper zoo;
    private ActorRef storageActor;

    public CustomWatcher(ZooKeeper zoo,ActorRef storageActor) {
        this.zoo = zoo;
        this.storageActor = storageActor;
    }

    @Override
    public void process(WatchedEvent event) {
        List<String> servers = new ArrayList<>();
        try {
            servers = zoo.getChildren(AkkaServer.ZOOKEEPER_SERVERS_DIR, this);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<String> serversData = new ArrayList<>();

        for (String server : servers) {
            byte[] data = new byte[0];
            try {
                data = zoo.getData(AkkaServer.ZOOKEEPER_SERVER_DIR + server, false, null);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
            serversData.add(new String(data));
        }

        storageActor.tell(new StoreServersMessage(serversData), ActorRef.noSender());
    }
}
