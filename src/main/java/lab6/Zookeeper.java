package lab6;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.stream.ActorMaterializer;

import java.io.IOException;

public class Zookeeper {
    public static void main(String[] args) throws IOException {
        Zookeeper zoo = new 
        ActorSystem system = ActorSystem.create(ACTOR_SYSTEM_NAME);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        ActorRef cacheActor = system.actorOf(Props.create(CacheActor.class));
    }
}
