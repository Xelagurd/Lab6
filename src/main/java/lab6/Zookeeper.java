package lab6;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class Zookeeper {
    public static void main(String[] args) throws IOException {
        ZooKeeper zoo = new ZooKeeper("127.0.0.1:8080",3000,this);
        ActorSystem system = ActorSystem.create(ACTOR_SYSTEM_NAME);
        ActorRef cacheActor = system.actorOf(Props.create(CacheActor.class));
    }
}
