package com.services;

import Beans.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("analyst")
public class AnalystServices {
    @Path("get_analyst_number")
    @GET
    public Response getAnalystNumber() {
        return Response.ok(AnalystClients.getInstance().getAnalystClients().size()).build();
    }
    @Path("get_nodes_number")
    @GET
    public Response getNodesNumber() {
        return Response.ok(Nodes.getInstance().getNodes().size()).build();
    }
    @Path("get_last_stats")
    @GET
    public synchronized Response getStats(@QueryParam("n") Integer n, @Context UriInfo uriInfo) {
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
            return Response.ok(Statistics.getInstance().getDevStandard(n)).build();
        } catch (Exception e) { return Response.serverError().build(); }
    }
    @Path("get_med")
    @GET
    public Response getMed(@QueryParam("n") Integer n, @Context UriInfo uriInfo) {
        try {
            MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
            return Response.ok(Statistics.getInstance().getMedia(n)).build();
        } catch (Exception e) { return Response.serverError().build(); }
    }
    @Path("insert_analyst")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public synchronized Response insertAnalyst(AnalystClient analyst) {
        if (AnalystClients.getInstance().getAnalyst(analyst.getId()) == null) {
            AnalystClients.getInstance().addAnalyst(analyst);
            return Response.ok().build();
        }
        else {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }
    @Path("remove_analyst")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public synchronized Response removeAnalyst(AnalystClient analyst) {
        AnalystClients.getInstance().removeAnalyst(analyst.getId());
        return Response.ok().build();
    }

}
