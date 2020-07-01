package P2PNet;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.Scanner;

public class Node {

    public Node (int id, String ip, int port) {
        NodeWorker nodeWorker = new NodeWorker(id,ip,port);
        new Thread(nodeWorker).start();
        System.out.println("Insert any number to exit: ");
        Scanner scanner = new Scanner(System.in);
        scanner.nextInt();
        nodeWorker.exitFromNetwork();
    }

}

class NodeWorker implements Runnable {

    private NodeImpl nodeImpl;
    private Server server;
    private int id;
    private String ip;
    private int port;

    public void exitFromNetwork () {
        this.nodeImpl.exitFromNetwork();
        this.server.shutdown();
    }

    public void run () {
        try {
            this.nodeImpl = new NodeImpl(this.id,this.ip,this.port);
            this.server = ServerBuilder.forPort(this.port).addService(nodeImpl).build();
            this.server.start();
            this.nodeImpl.insertInNetwork();
            System.out.println("Node started!");
            server.awaitTermination();
        }
        catch (InterruptedException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public NodeWorker (int id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }

}