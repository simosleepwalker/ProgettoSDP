package P2PNet;

import io.grpc.Server;
import io.grpc.ServerBuilder;

class NodeNetwork implements Runnable {

    private NodeImpl nodeImpl;
    private Server server;
    private int id;
    private String ip;
    private int port;
    private SensorSimulator sensorSimulator;

    private boolean connected;

    public boolean isConnected () {
        return connected;
    }

    public void exitFromNetwork () {
        this.nodeImpl.exitFromNetwork();
        this.server.shutdown();
    }

    public void setSensorSimulator (SensorSimulator s) {
        this.nodeImpl.setSensorSimulator(s);
    }

    public void run () {
        try {
            this.nodeImpl = new NodeImpl(this.id,this.ip,this.port);
            this.setSensorSimulator(this.sensorSimulator);
            this.server = ServerBuilder.forPort(this.port).addService(nodeImpl).build();
            this.server.start();
            if (this.nodeImpl.insertInNetwork()) {
                this.connected = true;
                System.out.println("Enter anything to exit: ");
                server.awaitTermination();
            }
        }
        catch (Exception e) { System.out.println("Failed to start network service"); }
    }

    public NodeNetwork (int id, String ip, int port, SensorSimulator sensorSimulator) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.sensorSimulator = sensorSimulator;
        this.connected = false;
    }

}
