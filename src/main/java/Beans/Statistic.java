package Beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "statistic")
public class Statistic {

    private String name;

    @XmlElement(name = "name")
    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public Statistic (String name) {
        this.name = name;
    }

    public Statistic () {}

}
