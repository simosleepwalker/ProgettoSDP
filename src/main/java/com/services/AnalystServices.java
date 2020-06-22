package com.services;

import Beans.Nodes;
import Beans.Statistic;
import Beans.Statistics;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
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
        try {
            if (n.equals(null))
                return Response.serverError().build();
            else{
                List stats = Statistics.getInstance().getStats();
                return Response.ok(stats.subList(stats.size()-n-1,stats.size()-1)).build();
            }
        } catch (IndexOutOfBoundsException e) { return Response.ok(Statistics.getInstance().getStats()).build(); }
    }
    @Path("get_dev_med_stats")
    @GET
    public Response getDevMed(@QueryParam("n") String n, @Context UriInfo uriInfo) {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        return Response.ok().build();
    }

}
