package P2PNet;

import Beans.NodesList;
import com.sun.jersey.api.container.filter.LoggingFilter;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.glassfish.jersey.client.ClientConfig;
import p2p.nodes.NodeServiceGrpc;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.List;

public class Node {

    private Integer port;
    private Integer id;
    private String ip;

    private Server nodeReceiver;

    public Server getNodeReceiver () {
        return this.nodeReceiver;
    }

    public Integer getPort () {
        return this.port;
    }

    public static void changeNext (String ip, Integer port, Integer newId, String newIp, Integer newPort){
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(ip + ":" + port.toString()).usePlaintext(true).build();
        NodeServiceGrpc.NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);
        p2p.nodes.Node.NodeMessage request = p2p.nodes.Node.NodeMessage.newBuilder().setPort(newPort).setId(newId).setIp(newIp).build();
        p2p.nodes.Node.OkMessage response = stub.changeNext(request);
        System.out.println(response.getVal());
        channel.shutdown();
    }

    public static void sendToken (String ip, Integer port, p2p.nodes.Node.Token token) {
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(ip + ":" + port.toString()).usePlaintext(true).build();
        NodeServiceGrpc.NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);
        stub.recvToken(token);
        channel.shutdown();
    }

    public boolean insertNode () {
        Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
        WebTarget webTarget = client.target("http://localhost:8080/simple_service_webapp_war/webapi/nodes/insert_node");
        Beans.Node nodeBean = new Beans.Node(this.id,this.ip,this.port);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.json(nodeBean));
        List<Beans.Node> nodes = null;
        try{ nodes = response.readEntity(NodesList.class).getNodes(); } catch (Exception e) { return false; }
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getId().equals(this.id)){
                changeNext(nodes.get((i-1)%nodes.size()).getIp(),nodes.get((i-1)%nodes.size()).getPort(),this.id,this.ip,this.port);
                changeNext(this.ip,this.port,nodes.get((i+1)%nodes.size()).getId(),nodes.get((i+1)%nodes.size()).getIp(),nodes.get((i+1)%nodes.size()).getPort());
            }
        }
        return true;
    }

    public Node (Integer id, String ip, Integer port) {
        try {
            this.id = id;
            this.ip = ip;
            this.port = port;
            this.nodeReceiver = ServerBuilder.forPort(port).addService(new NodeImpl(id,ip,port)).build();
            nodeReceiver.start();
            if (insertNode()) {
                System.out.println("Node Server started!");
                nodeReceiver.awaitTermination();
                //FAR PARTIRE TUTTO IL DIOCANE
            }
            else {
                nodeReceiver.shutdown();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
