package P2PNet;

import Beans.NodesList;
import com.sun.jersey.api.container.filter.LoggingFilter;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.message.internal.MessageBodyProviderNotFoundException;
import p2p.nodes.Node;
import p2p.nodes.NodeServiceGrpc;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class NodeImpl extends NodeServiceGrpc.NodeServiceImplBase {

    private volatile List<Beans.Node> nodesInNetwork;

    private volatile boolean canReceiveToken;
    private volatile boolean mustSendToken;
    private static Integer syncToken;
    private static Integer syncNode;

    private Integer id;
    private String ip;
    private Integer port;

    private volatile Integer nextNodeId;
    private volatile String nextNodeIp;
    private volatile Integer nextNodePort;

    private SensorSimulator sensorSimulator;

    //region Metodi grpc
    @Override
    public void updateNodesList(Node.NodesMessage request, StreamObserver<Node.OkMessage> responseObserver) {
        synchronized (syncNode) {
            List<Beans.Node> newNodesInNetwork = new ArrayList<>();
            for (int i = 0; i < request.getNodesListCount(); i++)
                newNodesInNetwork.add(new Beans.Node(request.getNodesList(i).getId(),request.getNodesList(i).getIp(),request.getNodesList(i).getPort()));
            this.nodesInNetwork = newNodesInNetwork;
            Node.OkMessage response = Node.OkMessage.newBuilder().setVal("List Updated for Node " + this.id.toString()).build();
            System.out.println("Nodes List Updated");
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            syncNode.notify();
        }
        printProperties();
    }

    @Override
    public void recvToken (Node.Token request, StreamObserver<Node.OkMessage> responseObserver) {
        if (this.canReceiveToken) {
            this.mustSendToken = true;
            List done = new ArrayList(request.getIdsList());
            List values = new ArrayList(request.getValuesList());
            Node.OkMessage response = Node.OkMessage.newBuilder().setVal("Ok").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            if (this.sensorSimulator.getNewMedia() && this.haveToInsert(done)) {
                values.add(this.sensorSimulator.getMedia());
                done.add(this.id);
            }
            if (nodesInNetwork.size() <= done.size()) {
                System.out.println("Stats sent to Server with nodes: " + done.toString());
                sendStat(getMedia(values));
                values.clear();
                done.clear();
            }
            synchronized (syncToken) {
                sendToken(this.getNextNode().getIp(), this.getNextNode().getPort(), Node.Token.newBuilder().addAllIds(done).addAllValues(values).build());
                this.mustSendToken = false;
                syncToken.notify();
            }
        }
        else {
            Node.OkMessage response = Node.OkMessage.newBuilder().setVal("Error").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
    //endregion

    //region Metodi
    public void exitFromNetwork () {
        synchronized (syncToken) {
            while (this.mustSendToken)
                try { syncToken.wait(); } catch (InterruptedException e) { }
            this.canReceiveToken = false;
            Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
            WebTarget webTarget = client.target("http://localhost:8080/simple_service_webapp_war/webapi/nodes/remove_node");
            Beans.Node nodeBean = new Beans.Node(this.id,this.ip,this.port);
            Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(Entity.json(nodeBean));
            try {
                List<Beans.Node> nodes = response.readEntity(NodesList.class).getNodes();
                this.updateOtherNodesList(nodes);
            }
            catch (MessageBodyProviderNotFoundException e) { this.exitFromNetwork(); }
        }
    }

    public boolean insertInNetwork () {
        try {
            Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
            WebTarget webTarget = client.target("http://localhost:8080/simple_service_webapp_war/webapi/nodes/insert_node");
            Beans.Node nodeBean = new Beans.Node(this.id, this.ip, this.port);
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(Entity.json(nodeBean));
            List<Beans.Node> nodes = response.readEntity(NodesList.class).getNodes();
            this.nodesInNetwork = nodes;
            this.updateOtherNodesList(nodes);
            if (nodes.size() == 1)
                sendToken(this.ip,this.port,p2p.nodes.Node.Token.newBuilder().build());
            printProperties();
            return true;
        }
        catch (Exception e) {
            System.out.println("Server can't be contacted or a Node with the same id is already in the Network");
            return false;
        }
    }

    public boolean haveToInsert (List<Integer> ids) {
        for (int i = 0; i < ids.size(); i++)
            if (ids.get(i).equals(this.id))
                return false;
        return true;
    }

    public void setSensorSimulator(SensorSimulator sensorSimulator) {
        this.sensorSimulator = sensorSimulator;
    }

    public double getMedia (List<Double> vals) {
        double sum = 0;
        for (int i = 0; i < vals.size(); i++)
            sum = sum + vals.get(i);
        return sum/vals.size();
    }

    public void updateOtherNodesList (List<Beans.Node> nodes) {
        for (int i = 0; i < nodes.size(); i++) {
            if (!nodes.get(i).getId().equals(this.id)) {
                final ManagedChannel channel = ManagedChannelBuilder.forTarget(nodes.get(i).getIp() + ":" + nodes.get(i).getPort().toString()).usePlaintext(true).build();
                NodeServiceGrpc.NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);
                Node.OkMessage res = stub.updateNodesList(this.buildNodesMessage(nodes));
                channel.shutdown();
            }
        }
    }

    public Node.NodesMessage buildNodesMessage (List<Beans.Node> nodes) {
        Node.NodesMessage.Builder nodesToSend = Node.NodesMessage.newBuilder();
        for (int i = 0; i < nodes.size(); i++)
            nodesToSend.addNodesList(Node.NodeMessage.newBuilder().setId(nodes.get(i).getId()).setIp(nodes.get(i).getIp()).setPort(nodes.get(i).getPort()));
        return nodesToSend.build();
    }

    public Beans.Node getNextNode () {
        for (int i = 0; i < this.nodesInNetwork.size(); i++) {
            if (this.nodesInNetwork.get(i).getId().equals(this.id))
                return this.nodesInNetwork.get(mod(i+1,this.nodesInNetwork.size()));
        }
        return null;
    }

    public int mod (int m, int n) { return (((m % n) + n) % n); }

    public void printProperties () {
        System.out.println("Node id: " + this.id.toString());
        System.out.println("Next node id: " + this.getNextNode().getId().toString());
    }
    //endregion

    //region Client
    public void sendToken (String ip, Integer port, p2p.nodes.Node.Token token) {
        synchronized (syncNode) {
            try {
                final ManagedChannel channel = ManagedChannelBuilder.forTarget(ip + ":" + port.toString()).usePlaintext(true).build();
                NodeServiceGrpc.NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);
                Node.OkMessage res = stub.recvToken(token);
                channel.shutdown();
                if (!res.getVal().equals("Ok"))
                    throw new NullPointerException();
            }
            catch (NullPointerException e) {
                try {
                    syncNode.wait();
                    Beans.Node nextNode = this.getNextNode();
                    this.sendToken(nextNode.getIp(),nextNode.getPort(),token); }
                catch (InterruptedException ex) { }
            }
            catch (StatusRuntimeException e) { }
        }
    }

    public static void sendStat (double val) {
        Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
        WebTarget webTarget = client.target("http://localhost:8080/simple_service_webapp_war/webapi/statistics/insert_stat");
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        try { invocationBuilder.post(Entity.json(val)); }
        catch (NumberFormatException | ProcessingException e) { }
    }
    //endregion

    public NodeImpl (Integer id, String ip, Integer port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.nextNodeId = null;
        this.nextNodePort = null;
        this.nextNodeIp = null;
        this.sensorSimulator = null;
        this.mustSendToken = false;
        this.canReceiveToken = true;
        syncNode = 0;
        syncToken = 0;
    }

}
