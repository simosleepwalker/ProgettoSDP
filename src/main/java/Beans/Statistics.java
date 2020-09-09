package Beans;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Statistics {

    private ArrayList<Statistic> stats;

    private volatile static Statistics instance;

    public static Statistics getInstance() {
        if(instance==null)
            instance = new Statistics();
        return instance;
    }

    public void addStat (Statistic stat) {
        this.stats.add(stat);
    }

    public synchronized List<Statistic> getStats () {
        return this.stats;
    }

    public synchronized List<Statistic> getStats (Integer n) {
        try {
            return this.stats.subList(this.stats.size()-n,this.stats.size());
        } catch (IndexOutOfBoundsException e) { return this.getStats(); }
    }

    public Statistics () {
        this.stats = new ArrayList<Statistic>();
    }

    public Double getSumStats (Integer n) {
        Double counter = (double) 0;
        for (int i = this.stats.size()-1; i >= this.stats.size()-n; i--)
            counter = counter + ((Statistic)this.stats.get(i)).getVal();
        return counter;
    }

    public synchronized Double getMedia (Integer n) {
        return this.getSumStats(n)/n;
    }

    public synchronized Double getVarianza (Integer n) {
        Double media = this.getMedia(n);
        List<Double> subtracted = this.getStats(n).stream().map(stat -> Math.abs(stat.getVal()-media)).
                collect(Collectors.toList());
        List<Double> elevated = subtracted.stream().map(stat -> stat*stat).collect(Collectors.toList());
        Double sum = (double) 0;
        for (int i = 0; i < n; i++)
            sum = sum + elevated.get(i);
        return sum/(n-1);
    }

    public synchronized Double getDevStandard (Integer n) {
        return Math.sqrt(this.getVarianza(n));
    }

}
