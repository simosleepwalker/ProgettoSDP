package P2PNet;

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