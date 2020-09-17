package P2PNet;

import Beans.NodesList;
import P2PNet.Lock.Lock;
import com.sun.jersey.api.container.filter.LoggingFilter;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
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

    private Integer id;
    private String ip;
    private Integer port;

    private volatile Integer nextNodeId;
    private volatile String nextNodeIp;
    private volatile Integer nextNodePort;

    private SensorSimulator sensorSimulator;

    private Lock recvLock;
    private Lock sendLock;
    private Lock updtLock;
    private Lock hasToken;

    //region Metodi Network
    @Override
    public void updateNodesList(Node.NodesMessage request, StreamObserver<Node.OkMessage> responseObserver) {
        if (this.updtLock.lock()) {
            List<Beans.Node> newNodesInNetwork = new ArrayList<>();
            for (int i = 0; i < request.getNodesListCount(); i++)
                newNodesInNetwork.add(new Beans.Node(request.getNodesList(i).getId(),request.getNodesList(i).getIp(),request.getNodesList(i).getPort()));
            this.nodesInNetwork = newNodesInNetwork;
            Node.OkMessage response = Node.OkMessage.newBuilder().setVal("List Updated for Node " + this.id.toString()).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            System.out.println("Nodes List Updated");
            printProperties();
            this.updtLock.unlock();
        }
    }

    @Override
    public void recvToken (Node.Token request, StreamObserver<Node.OkMessage> responseObserver) {
        if (canReceiveToken && this.hasToken.lock()) {
            Node.OkMessage response = Node.OkMessage.newBuilder().setVal("Ok").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            tokenElaboration(request);
            this.hasToken.unlock();
        }
        else {
            Node.OkMessage response = Node.OkMessage.newBuilder().setVal("Error").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    public void exitFromNetwork () {
        this.canReceiveToken = false;
        if ((this.nodesInNetwork.size() == 1) || (this.hasToken.lock() && this.updtLock.lock())) {
            Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
            WebTarget webTarget = client.target("http://localhost:8080/simple_service_webapp_war/webapi/nodes/remove_node");
            Beans.Node nodeBean = new Beans.Node(this.id,this.ip,this.port);
            Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(Entity.json(nodeBean));
            try {
                List<Beans.Node> nodes = response.readEntity(NodesList.class).getNodes();
                this.updateOtherNodesList(nodes);
            }
            catch (MessageBodyProviderNotFoundException e) { exitFromNetwork(); }
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
            printProperties();
            if (nodes.size() == 1)
                tokenElaboration(p2p.nodes.Node.Token.newBuilder().build());
            return true;
        }
        catch (Exception e) {
            System.out.println("Server can't be contacted or a Node with the same id is already in the Network");
            return false;
        }
    }

    public void sendToken (String ip, Integer port, p2p.nodes.Node.Token token) {
        try {
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(ip + ":" + port.toString()).usePlaintext(true).build();
            NodeServiceGrpc.NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);
            Node.OkMessage res = stub.recvToken(token);
            channel.shutdown();
            if (!res.getVal().equals("Ok")) {
                throw new Exception("Communication error");
            }
            this.hasToken.unlock();
        }
        catch (Exception e) {
            if (nodesInNetwork.size() != 1)
                sendToken(getNextNode().getIp(),getNextNode().getPort(),token);
            else {
                this.hasToken.unlock();
                tokenElaboration(token);
            }
        }
    }
    //endregion

    //region Metodi
    public boolean doneNodes (ArrayList doneNodes) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (int i = 0; i < nodesInNetwork.size(); i++)
            ids.add(nodesInNetwork.get(i).getId());
        if (doneNodes.containsAll(ids))
            return true;
        else
            return false;
    }

    public Node.Token statsElaboration (Node.Token request) {
        List done = new ArrayList(request.getIdsList());
        List values = new ArrayList(request.getValuesList());
        if (this.sensorSimulator.getNewMedia() && this.haveToInsert(done)) {
            values.add(this.sensorSimulator.getMedia());
            done.add(this.id);
        }
        if (doneNodes((ArrayList) done)) {
            System.out.println("Stats sent to Server with nodes: " + done.toString());
            sendStat(getMedia(values));
            values.clear();
            done.clear();
        }
        return Node.Token.newBuilder().addAllIds(done).addAllValues(values).build();
    }

    public void tokenElaboration (Node.Token request) {
        do request = statsElaboration(request);
        while (nodesInNetwork.size() == 1);
        if (!this.hasToken.isLocked())
            this.hasToken.lock();
        sendToken(getNextNode().getIp(),getNextNode().getPort(),request);
    }

    public static void sendStat (double val) {
        Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
        WebTarget webTarget = client.target("http://localhost:8080/simple_service_webapp_war/webapi/statistics/insert_stat");
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        try { invocationBuilder.post(Entity.json(val)); }
        catch (NumberFormatException | ProcessingException e) { }
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
                stub.updateNodesList(this.buildNodesMessage(nodes));
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

    public NodeImpl (Integer id, String ip, Integer port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.nextNodeId = null;
        this.nextNodePort = null;
        this.nextNodeIp = null;
        this.sensorSimulator = null;
        this.canReceiveToken = true;
        this.recvLock = new Lock();
        this.sendLock = new Lock();
        this.updtLock = new Lock();
        this.hasToken = new Lock();
    }

}
