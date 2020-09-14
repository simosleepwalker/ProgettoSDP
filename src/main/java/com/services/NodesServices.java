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
    @Path("get_nodes_number")
    @GET
    public Response getNodesNumber() {
        return Response.ok(Nodes.getInstance().getNodes().size()).build();
    }
    @Path("insert_node")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response insertNode(Node node) {
        Nodes instance = Nodes.getInstance();
        synchronized (instance) {
            while (instance.enteringExitingNode) try { instance.wait(); } catch (InterruptedException e) { }
            if (instance.getNode(node.getId()) == null) {
                instance.enteringExitingNode = true;
                instance.addNode(node);
                NodesList nodes = new NodesList();
                nodes.setNodes(Nodes.getInstance().getNodes());
                Notifications.sendNotification("Node [" + node.getId().toString() + "] entered in Network");
                instance.enteringExitingNode = false;
                instance.notify();
                return Response.ok(nodes).build();
            }
            else {
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
        }
    }
    @Path("remove_node")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response removeNode(Node node) {
        Nodes instance = Nodes.getInstance();
        synchronized (instance) {
            while (instance.enteringExitingNode) try { instance.wait(); } catch (InterruptedException e) { }
            instance.enteringExitingNode = true;
            NodesList nodes = new NodesList();
            nodes.setNodes(Nodes.getInstance().getNodes());
            instance.removeNode(node.getId());
            Notifications.sendNotification("Node [" + node.getId().toString() + "] exited from Network");
            instance.enteringExitingNode = false;
            instance.notify();
            return Response.ok(nodes).build();
        }
    }

}