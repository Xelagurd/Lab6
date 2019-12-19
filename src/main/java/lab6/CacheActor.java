package lab6;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
                    
                    if (storage.containsKey(req.getSite() + req.getRequestCount())) {
                        sender().tell(new Integer(storage.get(req.getSite() + req.getRequestCount())), ActorRef.noSender());
                    } else {
                        sender().tell(NO_ANSWER_MSG, ActorRef.noSender());
                    }
                }).build();
    }
}
