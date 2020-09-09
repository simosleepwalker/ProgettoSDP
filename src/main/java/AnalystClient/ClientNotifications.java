package AnalystClient;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ClientNotifications implements Runnable {

    private ClientNotificationsImpl notificationsImpls;

    @Override
    public void run() {
        this.notificationsImpls.connectToServer();
        Server server = ServerBuilder.forPort(this.notificationsImpls.getPort()).addService(this.notificationsImpls).build();
        try { server.start(); } catch (IOException e) { e.printStackTrace(); }
    }

    public ClientNotifications (ClientNotificationsImpl notificationsImpl) {
        this.notificationsImpls = notificationsImpl;
    }

}
