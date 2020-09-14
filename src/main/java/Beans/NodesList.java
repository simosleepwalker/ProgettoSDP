package Beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "nodeslist")
@XmlAccessorType(XmlAccessType.FIELD)
public class NodesList {

    @XmlElement(name = "node")
    private List<Beans.Node> nodes;

    public synchronized void setNodes (List<Beans.Node> nodes) {
        this.nodes = nodes;
    }

    public synchronized List<Beans.Node> getNodes () {
        return nodes;
    }

    public NodesList () {}

}