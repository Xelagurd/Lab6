package lab6;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.Random;

public class CacheActor extends AbstractActor {
    private ArrayList<String> servers = new ArrayList<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(StoreServers.class, m -> {
                    servers = m.getServers();
                })
                .match(GetRandomServer.class, m -> {
                    servers.remove(m.getPort());
                    Random rand = new Random();
                    int len = servers.size();
                    getSender().tell(Integer.parseInt(servers.get(rand.nextInt(len))), ActorRef.noSender());

                }).build();
    }
}
