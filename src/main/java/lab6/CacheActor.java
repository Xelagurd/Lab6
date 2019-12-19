package lab6;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CacheActor extends AbstractActor {
    private ArrayList<String> servers = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(StoreServers.class, m -> {
                    if (!storage.containsKey(m.getSite() + m.getRequestCount())) {
                        storage.put(m.getSite() + m.getRequestCount(), m.getResult());
                        System.out.println("Info for site: " + m.getSite() + " with count " + m.getRequestCount() + " added");
                    }
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
