package com.services;

import Beans.Nodes;
import Beans.Statistics;
import Beans.StatisticsList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("analyst")
public class AnalystServices {

    @Path("get_nodes_number")
    @GET
    public Response getNodesNumber() {
        return Response.ok(Nodes.getInstance().getNodes().size()).build();
    }
    @Path("get_last_stats")
    @GET
    public Response getStats(@QueryParam("n") Integer n, @Context UriInfo uriInfo) {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        if (n.equals(null))
            return Response.serverError().build();
        else{
            StatisticsList statsList = new StatisticsList();
            statsList.setStatistics(Statistics.getInstance().getStats(n));
            return Response.ok(statsList).build();
        }
    }
    @Path("get_dev")
    @GET
    public Response getDev(@QueryParam("n") Integer n, @Context UriInfo uriInfo) {
        try {
            MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
            return Response.ok(Statistics.getInstance().getDevStandard(n)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
    @Path("get_med")
    @GET
    public Response getMed(@QueryParam("n") Integer n, @Context UriInfo uriInfo) {
        try {
            MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
            return Response.ok(Statistics.getInstance().getMedia(n)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
}
