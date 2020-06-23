package Beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "statisticslist")
@XmlAccessorType(XmlAccessType.FIELD)
public class StatisticsList {

    private List<Statistic> statistics;

    @XmlElement(name = "statistic")
    public void setStatistics(List<Statistic> statistics) {
        this.statistics = statistics;
    }

    public List<Statistic> getStatistics() {
        return statistics;
    }

    public StatisticsList () {}

}