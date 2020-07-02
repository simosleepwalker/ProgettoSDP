package Beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@XmlRootElement(name = "statistic")
public class Statistic {

    @XmlElement(name = "timestamp")
    private Timestamp timestamp;
    @XmlElement(name = "val")
    private Double val;

    public void setVal(Double val) { this.val = val; }

    public Double getVal() { return val; }

    public Timestamp getTimestamp() { return timestamp; }

    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

    public Statistic () {}

    public Statistic (double val) {
        this.val = val;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String toString() { return timestamp + " VALUE: " + val; }

}
