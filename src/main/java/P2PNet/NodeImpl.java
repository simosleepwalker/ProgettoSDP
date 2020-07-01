package P2PNet;

import Beans.NodesList;
import com.sun.jersey.api.container.filter.LoggingFilter;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.glassfish.jersey.client.ClientConfig;
import p2p.nodes.Node;
import p2p.nodes.NodeServiceGrpc;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NodeImpl extends NodeServiceGrpc.NodeServiceImplBase {

    private Integer id;
    private String ip;
    private Integer port;

    private Integer nextNodeId;
    private String nextNodeIp;
    private Integer nextNodePort;



    //region Metodi grpc
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
        synchronized (this) {
            Node.OkMessage response = Node.OkMessage.newBuilder().setVal("Yeah").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            System.out.println(request.getToken());
            try {
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) { e.printStackTrace(); }
            sendToken(nextNodeIp, nextNodePort, request);
        }
    }
    //endregion

    //region Metodi
    public void exitFromNetwork () {
        synchronized (this) {
            Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
            WebTarget webTarget = client.target("http://localhost:8080/simple_service_webapp_war/webapi/nodes/remove_node");
            Beans.Node nodeBean = new Beans.Node(this.id,this.ip,this.port);
            Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(Entity.json(nodeBean));
            List<Beans.Node> nodes = response.readEntity(NodesList.class).getNodes();
            for (int i = 0; i < nodes.size(); i++)
                if (nodes.get(i).getId().equals(this.nextNodeId))
                    changeNext(nodes.get(Math.abs((i-1)%nodes.size())).getIp(),nodes.get(Math.abs((i-1)%nodes.size())).getPort(),nodes.get(i).getId(),nodes.get(i).getIp(),nodes.get(i).getPort());
        }
    }

    public boolean insertInNetwork () {
        Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
        WebTarget webTarget = client.target("http://localhost:8080/simple_service_webapp_war/webapi/nodes/insert_node");
        Beans.Node nodeBean = new Beans.Node(this.id,this.ip,this.port);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.json(nodeBean));
        List<Beans.Node> nodes = null;
        try{ nodes = response.readEntity(NodesList.class).getNodes(); } catch (Exception e) { return false; }
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getId().equals(this.id)){
                changeNext(nodes.get(Math.abs((i-1)%nodes.size())).getIp(),nodes.get(Math.abs((i-1)%nodes.size())).getPort(),this.id,this.ip,this.port);
                changeNext(this.ip,this.port,nodes.get(Math.abs((i+1)%nodes.size())).getId(),nodes.get(Math.abs((i+1)%nodes.size())).getIp(),nodes.get(Math.abs((i+1)%nodes.size())).getPort());
            }
        }
        if (nodes.size() == 1) {
            p2p.nodes.Node.Token token = p2p.nodes.Node.Token.newBuilder().setToken("Prova").build();
            sendToken(this.ip,this.port,token);
        }
        return true;
    }
    //endregion

    //region Client
    public static void sendToken (String ip, Integer port, p2p.nodes.Node.Token token) {
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(ip + ":" + port.toString()).usePlaintext(true).build();
        NodeServiceGrpc.NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);
        stub.recvToken(token);
        channel.shutdown();
    }

    public static void changeNext (String ip, Integer port, Integer newId, String newIp, Integer newPort){
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(ip + ":" + port.toString()).usePlaintext(true).build();
        NodeServiceGrpc.NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);
        p2p.nodes.Node.NodeMessage request = p2p.nodes.Node.NodeMessage.newBuilder().setPort(newPort).setId(newId).setIp(newIp).build();
        p2p.nodes.Node.OkMessage response = stub.changeNext(request);
        System.out.println(response.getVal());
        channel.shutdown();
    }
    //endregion

    public NodeImpl (Integer id, String ip, Integer port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.nextNodeId = null;
        this.nextNodePort = null;
        this.nextNodeIp = null;
    }

}
