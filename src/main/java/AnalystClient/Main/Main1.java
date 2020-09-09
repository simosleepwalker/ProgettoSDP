package AnalystClient.Main;

import AnalystClient.ClientConsole;
import AnalystClient.ClientNotifications;
import AnalystClient.ClientNotificationsImpl;
import AnalystClient.ClientRequests;

public class Main1 {

    public static void main(String[] args) {
        ClientNotificationsImpl notificationsImpl = new ClientNotificationsImpl(1,"localhost",9091);
        ClientNotifications notifications = new ClientNotifications(notificationsImpl);
        new Thread(notifications).start();

        ClientConsole console = new ClientConsole(new ClientRequests(), notificationsImpl);
    }

}
