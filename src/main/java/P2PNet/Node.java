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
import java.util.Scanner;

public class Node {

    private NodeImpl nodeImpl;

    private Integer port;
    private Integer id;
    private String ip;

    private Server nodeReceiver;

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

    public void waitAndExit () {
        System.out.println("Type any number to exit: ");
        Scanner scanner = new Scanner(System.in);
        int x = scanner.nextInt();
        this.nodeImpl.exitFromNetwork();
    }

    public Node (Integer id, String ip, Integer port) {
        try {
            this.id = id;
            this.ip = ip;
            this.port = port;
            this.nodeImpl = new NodeImpl(id,ip,port);
            this.nodeReceiver = ServerBuilder.forPort(port).addService(this.nodeImpl).build();
            nodeReceiver.start();
            if (insertInNetwork()) {
                System.out.println("Node Server started!");
                this.waitAndExit();
            }
            nodeReceiver.shutdown();
        }
        catch (IOException e) { e.printStackTrace(); }
    }

}