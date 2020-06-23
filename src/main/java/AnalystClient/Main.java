package AnalystClient;

import Beans.StatisticsList;

import javax.ws.rs.core.Response;

public class Main {

    public static void main(String[] args) {
        ClientRequests client = new ClientRequests();
        System.out.println("--------------------------------------");
        System.out.println("      Welcome to Client Analyzer      ");
        System.out.println("--------------------------------------");
        StatisticsList res = client.getStats(3);
        for (int i = 0; i < res.getStatistics().size(); i++)
            System.out.println(res.getStatistics().get(i).getVal());
        System.out.println(client.getDevStandard(3));
        System.out.println(client.getMedia(3));
    }

}
