package Beans;

import java.util.ArrayList;

public class AnalystClients {

    private ArrayList<AnalystClient> analystClients;

    private volatile static AnalystClients instance;

    public synchronized static AnalystClients getInstance() {
        if(instance==null)
            instance = new AnalystClients();
        return instance;
    }

    public synchronized ArrayList<AnalystClient> getAnalystClients() {
        return this.analystClients;
    }

    public AnalystClient getAnalyst(Integer analystId) {
        for (int i = 0; i < this.analystClients.size(); i++)
            if (this.analystClients.get(i).getId().equals(analystId))
                return this.analystClients.get(i);
        return null;
    }

    public synchronized void addAnalyst(AnalystClient analystClient) { this.analystClients.add(analystClient); }

    public synchronized void removeAnalyst (Integer id) { this.analystClients.remove(this.getAnalyst(id)); }

    public AnalystClients () {
        this.analystClients = new ArrayList<AnalystClient>();
    }

}
