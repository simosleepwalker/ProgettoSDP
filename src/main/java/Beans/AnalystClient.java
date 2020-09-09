package Beans;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class AnalystClient {

    @XmlAttribute
    private Integer id;
    @XmlElement(name = "ip")
    private String ip;
    @XmlElement(name = "port")
    private Integer port;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setIp(String ip) { this.ip = ip; }

    public void setPort(Integer port) { this.port = port; }

    public Integer getId () {
        return this.id;
    }

    public String getIp() { return ip; }

    public Integer getPort() { return port; }

    public AnalystClient (Integer id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }

    public AnalystClient () {}

}
