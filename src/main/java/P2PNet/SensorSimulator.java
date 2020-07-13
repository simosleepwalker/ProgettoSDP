package P2PNet;

import P2PNet.Simulatori.PM10Simulator;

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
