package Beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "nodeslist")
@XmlAccessorType(XmlAccessType.FIELD)
public class NodesList {

    private List<Beans.Node> nodes;

    @XmlElement(name = "node")
    public void setNodes (List<Beans.Node> nodes) {
        this.nodes = nodes;
    }

    public List<Beans.Node> getNodes () {
        return nodes;
    }

    public NodesList () {}

}