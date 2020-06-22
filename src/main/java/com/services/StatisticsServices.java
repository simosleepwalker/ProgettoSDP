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
    public Response insertStat(Statistic stat) {
        Statistics.getInstance().addStat(stat);
        return Response.ok().build();
    }

}