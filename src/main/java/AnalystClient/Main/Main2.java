package AnalystClient.Main;

import AnalystClient.ClientConsole;
import AnalystClient.ClientNotifications;
import AnalystClient.ClientNotificationsImpl;
import AnalystClient.ClientRequests;

public class Main2 {

    public static void main(String[] args) {
        ClientNotificationsImpl notificationsImpl = new ClientNotificationsImpl(2,"localhost",9092);
        ClientNotifications notifications = new ClientNotifications(notificationsImpl);
        new Thread(notifications).start();

        ClientConsole console = new ClientConsole(new ClientRequests(), notificationsImpl);
    }

}
