package com.services;

import Beans.AnalystClients;
import analyst.client.Analyst;
import analyst.client.AnalystClientGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Notifications {

    public static void sendNotification (String notification) {
        AnalystClients analysts = AnalystClients.getInstance();
        for (int i = 0; i < analysts.getAnalystClients().size(); i++ ) {
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(analysts.getAnalystClients().get(i).getIp()
                + ":" + analysts.getAnalystClients().get(i).getPort().toString()).usePlaintext(true).build();
            AnalystClientGrpc.AnalystClientBlockingStub stub = AnalystClientGrpc.newBlockingStub(channel);
            Analyst.okMessage res = stub.notify(Analyst.notificationMessage.newBuilder().setMessage(notification).build());
            channel.shutdown();
        }
    }

}
