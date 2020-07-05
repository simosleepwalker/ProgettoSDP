package P2PNet;

import P2PNet.Simulatori.Measurement;
import P2PNet.Simulatori.PM10Simulator;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Node {

    public void waitForShutdown () {
        try {
            System.out.println("Enter anything to exit: ");
            Scanner scanner = new Scanner(System.in);
            scanner.nextByte();
        } catch (InputMismatchException e) { }
    }

    public Node (int id, String ip, int port) {
        SensorSimulator sensorSimulator = new SensorSimulator();
        NodeNetwork nodeNetwork = new NodeNetwork(id,ip,port,sensorSimulator);
        new Thread(nodeNetwork).start();

        Thread sensorSimulatorThread = new Thread(sensorSimulator);
        sensorSimulatorThread.start();

        this.waitForShutdown();
        if (nodeNetwork.isConnected())
            nodeNetwork.exitFromNetwork();
        sensorSimulator.shutdownSensor();
        sensorSimulatorThread.stop();
    }

}

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
                server.awaitTermination();
            }
        }
        catch (InterruptedException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public NodeNetwork (int id, String ip, int port, SensorSimulator sensorSimulator) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.sensorSimulator = sensorSimulator;
        this.connected = false;
    }

}

class Buffer implements P2PNet.Simulatori.Buffer {

    private Measurement buffer [];
    private int i;

    private double media;

    @Override
    public void addMeasurement(Measurement m) {
        this.buffer[this.i] = m;
        if ((this.i == 6 && this.buffer[11] != null) || this.i == 11){
            produceMedia();
        }
        this.i = Math.abs((i+1)%12);
    }

    public Measurement[] getBuffer () {
        return buffer;
    }

    public double getMedia () {
        return this.media;
    }

    public void produceMedia () {
        synchronized (this) {
            double sum = 0;
            for (int i = 0; i < this.buffer.length; i++)
                sum = sum + this.buffer[i].getValue();
            this.media = sum/this.buffer.length;
            this.notify();
        }
    }

    public Buffer () {
        this.buffer = new Measurement[12];
        this.i = 0;
    }
}

class SensorSimulator implements Runnable {

    private PM10Simulator simulator;
    private boolean newMedia;
    private double media;

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (this.simulator.getBuffer()) {
                    this.simulator.getBuffer().wait();
                    this.media = ((Buffer) this.simulator.getBuffer()).getMedia();
                    this.newMedia = true;
                }
            }
        } catch (InterruptedException e) { }
    }

    public boolean getNewMedia () {
        return this.newMedia;
    }

    public double getMedia () {
        this.newMedia = false;
        return this.media;
    }

    public void shutdownSensor () {
        this.simulator.stopMeGently();
    }

    public SensorSimulator () {
        this.simulator = new PM10Simulator(new Buffer());
        new Thread(this.simulator).start();
    }

}