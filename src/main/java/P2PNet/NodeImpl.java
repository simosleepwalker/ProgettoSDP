package P2PNet;

import Beans.NodesList;
import com.sun.jersey.api.container.filter.LoggingFilter;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.glassfish.jersey.client.ClientConfig;
import p2p.nodes.Node;
import p2p.nodes.NodeServiceGrpc;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class NodeImpl extends NodeServiceGrpc.NodeServiceImplBase {

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
    public void changeNext(Node.NodeMessage request, StreamObserver<Node.OkMessage> responseObserver) {
        synchronized (syncNode) {
            this.nextNodeId = request.getId();
            this.nextNodeIp = request.getIp();
            this.nextNodePort = request.getPort();
            Node.OkMessage response = Node.OkMessage.newBuilder().setVal("Next Node Changed for Node " + this.id.toString()).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            syncNode.notify();
        }
    }

    @Override
    public void recvToken (Node.Token request, StreamObserver<Node.OkMessage> responseObserver) {
        if (this.canReceiveToken) {
            this.mustSendToken = true;
            List done = new ArrayList(request.getIdsList());
            List values = new ArrayList(request.getValuesList());
            int nodeConsidered = request.getNodesConsidered();
            Node.OkMessage response = Node.OkMessage.newBuilder().setVal("Ok").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            nodeConsidered = getNodesNumber();
            if (this.sensorSimulator.getNewMedia() && this.haveToInsert(done)) {
                values.add(this.sensorSimulator.getMedia());
                done.add(this.id);
            }
            if (nodeConsidered == done.size()) {
                sendStat(getMedia(values));
                values.clear();
                done.clear();
            }
            synchronized (syncToken) {
                sendToken(this.nextNodeIp, this.nextNodePort, Node.Token.newBuilder().addAllIds(done).addAllValues(values).setNodesConsidered(nodeConsidered).build());
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
            List<Beans.Node> nodes = response.readEntity(NodesList.class).getNodes();
            for (int i = 0; i < nodes.size(); i++)
                if (nodes.get(i).getId().equals(this.nextNodeId))
                    this.changeNext(nodes.get(mod((i-1),nodes.size())).getIp(),nodes.get(mod((i-1),nodes.size())).getPort(),nodes.get(i).getId(),nodes.get(i).getIp(),nodes.get(i).getPort());
        }
    }

    public boolean insertInNetwork () {
        try {
            Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
            WebTarget webTarget = client.target("http://localhost:8080/simple_service_webapp_war/webapi/nodes/insert_node");
            Beans.Node nodeBean = new Beans.Node(this.id,this.ip,this.port);
            Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(Entity.json(nodeBean));
            List<Beans.Node> nodes = response.readEntity(NodesList.class).getNodes();
            for (int i = 0; i < nodes.size(); i++) {
                if (nodes.get(i).getId().equals(this.id)){
                    this.changeNext(nodes.get(mod((i-1),nodes.size())).getIp(),nodes.get(mod((i-1),nodes.size())).getPort(),this.id,this.ip,this.port);
                    this.changeNext(this.ip,this.port,nodes.get(mod((i+1),nodes.size())).getId(),nodes.get(mod((i+1),nodes.size())).getIp(),nodes.get(mod((i+1),nodes.size())).getPort());
                }
            }
            if (nodes.size() == 1)
                sendToken(this.ip,this.port,p2p.nodes.Node.Token.newBuilder().setNodesConsidered(1).build());
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

    public int mod (int m, int n) { return (((m % n) + n) % n); }
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
                    this.sendToken(this.nextNodeIp,this.nextNodePort,token);
                } catch (InterruptedException ex) { }
            }
            catch (StatusRuntimeException e) { }
        }
    }

    public void changeNext (String ip, Integer port, Integer newId, String newIp, Integer newPort){
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(ip + ":" + port.toString()).usePlaintext(true).build();
        NodeServiceGrpc.NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);
        p2p.nodes.Node.NodeMessage request = p2p.nodes.Node.NodeMessage.newBuilder().setPort(newPort).setId(newId).setIp(newIp).build();
        p2p.nodes.Node.OkMessage response = stub.changeNext(request);
        channel.shutdown();
    }

    public static Integer getNodesNumber () {
        Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
        WebTarget webTarget = client.target("http://localhost:8080/simple_service_webapp_war/webapi/analyst/get_nodes_number");
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        Integer nodesNumber = response.readEntity(Integer.class);
        return nodesNumber;
    }

    public static void sendStat (double val) {
        Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
        WebTarget webTarget = client.target("http://localhost:8080/simple_service_webapp_war/webapi/statistics/insert_stat");
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        invocationBuilder.post(Entity.json(val));
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
