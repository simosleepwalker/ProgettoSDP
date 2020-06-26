package P2PNet;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import p2p.nodes.NodeServiceGrpc;

import java.io.IOException;

public class Node {

    private Integer port;
    private Server nodeReceiver;

    public Server getNodeReceiver () {
        return this.nodeReceiver;
    }

    public Integer getPort () {
        return this.port;
    }

    public static void changeNext (Integer port, Integer newPort){
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:" + port.toString()).usePlaintext(true).build();
        NodeServiceGrpc.NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);
        p2p.nodes.Node.NodeMessage request = p2p.nodes.Node.NodeMessage.newBuilder().setPort(newPort).setId(1).setIp("aaa").build();
        p2p.nodes.Node.OkMessage response = stub.changeNext(request);
        System.out.println(response.getVal());
        channel.shutdown();
    }

    public static void sendToken (String ip, Integer port, p2p.nodes.Node.Token token) {
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:" + port.toString()).usePlaintext(true).build();
        NodeServiceGrpc.NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);
        stub.recvToken(token);
        channel.shutdown();
    }

    public Node (Integer id, String ip, Integer port) {
        try {
            this.nodeReceiver = ServerBuilder.forPort(port).addService(new NodeImpl(id,ip,port)).build();
            nodeReceiver.start();
            System.out.println("Node Server started!");
            nodeReceiver.awaitTermination();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
