package Beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "statistic")
public class Statistic {

    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "val")
    private Double val;

    public void setVal(Double val) { this.val = val; }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public Double getVal() { return val; }

    public Statistic (String name) {
        this.name = name;
    }

    public Statistic () {}

}
