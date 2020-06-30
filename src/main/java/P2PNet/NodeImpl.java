package P2PNet;

import Beans.Nodes;
import Beans.NodesList;
import com.sun.jersey.api.container.filter.LoggingFilter;
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
            P2PNet.Node.sendToken(nextNodeIp, nextNodePort, request);
        }
    }

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
                    P2PNet.Node.changeNext(nodes.get(Math.abs((i-1)%nodes.size())).getIp(),nodes.get(Math.abs((i-1)%nodes.size())).getPort(),nodes.get(i).getId(),nodes.get(i).getIp(),nodes.get(i).getPort());
        }
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