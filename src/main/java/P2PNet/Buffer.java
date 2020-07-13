package P2PNet;

import P2PNet.Simulatori.Measurement;

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