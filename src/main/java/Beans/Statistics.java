package Beans;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "statistic")
public class Statistics {

    private ArrayList<Statistic> stats;

    private static Statistics instance;

    public static Statistics getInstance() {
        if(instance==null)
            instance = new Statistics();
        return instance;
    }

    public void addStat (Statistic stat) {
        this.stats.add(stat);
    }

    public ArrayList<Statistic> getStats () {
        return this.stats;
    }

    public Statistics () {
        this.stats = new ArrayList<Statistic>();
    }

}
