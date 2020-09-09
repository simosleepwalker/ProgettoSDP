package com.services;

import Beans.Statistic;
import Beans.Statistics;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("statistics")
public class StatisticsServices {

    @Path("insert_stat")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public synchronized Response insertStat(Double val) {
        Statistic stat = new Statistic(val);
        Statistics.getInstance().addStat(stat);
        Notifications.sendNotification("New Statistic available");
        return Response.ok().build();
    }

}