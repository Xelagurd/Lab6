package lab6;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CacheActor extends AbstractActor {
    private ArrayList<String> servers = new ArrayList<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(StoreServers.class, m -> {
                    servers = m.getServers();
                })
                .match(GetRandomServer.class, req -> {
                    System.out.println("Message for site: " + req.getSite() + " with count " + req.getRequestCount() + " received");
                    if (storage.containsKey(req.getSite() + req.getRequestCount())) {
                        sender().tell(new Integer(storage.get(req.getSite() + req.getRequestCount())), ActorRef.noSender());
                    } else {
                        sender().tell(NO_ANSWER_MSG, ActorRef.noSender());
                    }
                }).build();
    }
}
