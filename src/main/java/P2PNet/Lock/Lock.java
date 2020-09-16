package P2PNet.Lock;

public class Lock {

    private boolean lock;

    public boolean isLocked () {
        return lock;
    }

    public synchronized boolean lock () {
        while (this.lock)
            try { this.wait(); } catch (InterruptedException e) { }
        this.lock = true;
        return true;
    }

    public synchronized void unlock () {
        this.lock = false;
        this.notifyAll();
    }

    public Lock () {
        this.lock = false;
    }

}
