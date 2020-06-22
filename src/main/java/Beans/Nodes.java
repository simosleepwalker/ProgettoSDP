package Beans;

import java.util.ArrayList;

public class Nodes {

    private ArrayList<Node> nodes;

    private static Nodes instance;

    public static Nodes getInstance() {
        if(instance==null)
            instance = new Nodes();
        return instance;
    }

    public ArrayList<Node> getNodes() {
        return this.nodes;
    }

    public void addNode(Node node) {
        this.nodes.add(node);
    }

    public void removeNode (Integer id) { this.nodes.remove(this.getNode(id)); }

    public Node getNode(Integer nodeId) {
        for (int i = 0; i < this.nodes.size(); i++)
            if (this.nodes.get(i).getId().equals(nodeId))
                return this.nodes.get(i);
        return null;
    }

    public Nodes () {
        this.nodes = new ArrayList<Node>();
    }

}
