package Beans;

import java.util.ArrayList;
import java.util.Collections;

public class Nodes {

    public volatile boolean enteringExitingNode;

    private volatile ArrayList<Node> nodes;

    private volatile static Nodes instance;

    public synchronized static Nodes getInstance() {
        if(instance==null)
            instance = new Nodes();
        return instance;
    }

    public synchronized ArrayList<Node> getNodes() {
        ArrayList<Node> nodes = this.nodes;
        Collections.sort(nodes);
        return nodes;
    }

    public synchronized Node getNode(Integer nodeId) {
        for (int i = 0; i < this.nodes.size(); i++)
            if (this.nodes.get(i).getId().equals(nodeId))
                return this.nodes.get(i);
        return null;
    }

    public synchronized void addNode(Node node) {
        this.nodes.add(node);
    }

    public synchronized void removeNode (Integer id) { this.nodes.remove(this.getNode(id)); }

    public Nodes () {
        this.enteringExitingNode = false;
        this.nodes = new ArrayList<Node>();
    }

}
