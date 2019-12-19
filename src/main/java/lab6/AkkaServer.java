package lab6;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.*;

public class AkkaServer {
    private static int port;
    private static ActorRef storageActor;
    private static ZooKeeper zoo;

    private static final String ROUTES = "routes";
    private static final String LOCALHOST = "localhost";
    private static final String SERVER_ONLINE = "Server online on localhost:";
    private static final String ZOOKEEPER_HOST = "127.0.0.1:5000";
    private static final String ZOOKEEPER_SERVERS_DIR = "/servers";
    private static final String ZOOKEEPER_SERVER_DIR = "/servers/";
    private static final int TIMEOUT = 5000;

    public static void main(String[] args) throws Exception {
        AkkaServer akkaServer = new AkkaServer();

        Scanner in = new Scanner(System.in);
        port = in.nextInt();

        ActorSystem system = ActorSystem.create(ROUTES);
        storageActor = system.actorOf(Props.create(StorageActor.class));

        zoo = new ZooKeeper(
                ZOOKEEPER_HOST,
                TIMEOUT,
                new CustomWatcher()
        );
        zoo.create(
                ZOOKEEPER_SERVER_DIR + Integer.toString(port),
                Integer.toString(port).getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL
        );

        zoo.getChildren(ZOOKEEPER_SERVERS_DIR, new CustomWatcher());

        final ActorMaterializer materializer = ActorMaterializer.create(system);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = akkaServer.route().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = Http.get(system).bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(LOCALHOST, port),
                materializer
        );

        System.out.println(SERVER_ONLINE + port);
        System.in.read();

        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate());
    }
}
