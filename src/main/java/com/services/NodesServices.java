package com.services;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Beans.Node;
import Beans.Nodes;
import Beans.NodesList;

@Path("nodes")
public class NodesServices {

    @Path("get_nodes")
    @GET
    public Response getNodes() {
        NodesList nodes = new NodesList();
        nodes.setNodes(Nodes.getInstance().getNodes());
        return Response.ok(nodes).build();
    }
    @Path("insert_node")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response insertNode(Node node) {
        if (Nodes.getInstance().getNode(node.getId()) == null) {
            Nodes.getInstance().addNode(node);
            NodesList nodes = new NodesList();
            nodes.setNodes(Nodes.getInstance().getNodes());
            return Response.ok(nodes).build();
        }
        else {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }
    @Path("remove_node")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response removeNode(Node node) {
        Nodes.getInstance().removeNode(node.getId());
        return Response.ok().build();
    }

}