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
    public synchronized Response getNodes() {
        NodesList nodes = new NodesList();
        nodes.setNodes(Nodes.getInstance().getNodes());
        return Response.ok(nodes).build();
    }
    @Path("get_nodes_number")
    @GET
    public synchronized Response getNodesNumber() {
        return Response.ok(Nodes.getInstance().getNodes().size()).build();
    }
    @Path("insert_node")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public synchronized Response insertNode(Node node) {
        if (Nodes.getInstance().getNode(node.getId()) == null) {
            Nodes.getInstance().addNode(node);
            NodesList nodes = new NodesList();
            nodes.setNodes(Nodes.getInstance().getNodes());
            Notifications.sendNotification("Node [" + node.getId().toString() + "] entered in Network");
            return Response.ok(nodes).build();
        }
        else {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }
    @Path("remove_node")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public synchronized Response removeNode(Node node) {
        NodesList nodes = new NodesList();
        nodes.setNodes(Nodes.getInstance().getNodes());
        Nodes.getInstance().removeNode(node.getId());
        Notifications.sendNotification("Node [" + node.getId().toString() + "] exited from Network");
        return Response.ok(nodes).build();
    }

}