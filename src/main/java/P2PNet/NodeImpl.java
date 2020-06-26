package P2PNet;

import Beans.Nodes;
import io.grpc.stub.StreamObserver;
import p2p.nodes.Node;
import p2p.nodes.NodeServiceGrpc;

import java.util.concurrent.TimeUnit;

public class NodeImpl extends NodeServiceGrpc.NodeServiceImplBase {

    private Integer id;
    private String ip;
    private Integer port;

    private Integer nextNodeId;
    private String nextNodeIp;
    private Integer nextNodePort;

    @Override
    public void changeNext(Node.NodeMessage request, StreamObserver<Node.OkMessage> responseObserver) {
        this.nextNodeId = request.getId();
        this.nextNodeIp = request.getIp();
        this.nextNodePort = request.getPort();
        Node.OkMessage response = Node.OkMessage.newBuilder().setVal("Next Node Changed for Node " + this.id.toString()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void recvToken(Node.Token request, StreamObserver<Node.OkMessage> responseObserver) {
        Node.OkMessage response = Node.OkMessage.newBuilder().setVal("Yeah").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        System.out.println(request.getToken());
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        P2PNet.Node.sendToken(nextNodeIp, nextNodePort, request);
    }

    public Nodes getNodesList () {
        Nodes nodes = new Nodes ();
        return nodes;
    }

    public boolean enterInNetwork () {
        Nodes nodes = this.getNodesList();
        return false;
    }

    public NodeImpl (Integer id, String ip, Integer port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.nextNodeId = null;
        this.nextNodePort = null;
        this.nextNodeIp = null;
    }

}