package AnalystClient;

import Beans.StatisticsList;
import org.glassfish.jersey.message.internal.MessageBodyProviderNotFoundException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ClientConsole {

    private Scanner scanner;
    private ClientRequests requests;
    private ClientNotificationsImpl notifications;

    public void printNotification (String notification) {
        System.out.println(notification);
        this.printMainMenu();
    }

    public void printNodesNumber () {
        System.out.println("Nodi nella rete: " + this.requests.getNodes().toString());
        printMainMenu();
    }

    public void printLastStats (Integer n) {
        StatisticsList stats = this.requests.getStats(n);
        System.out.println("Ultime " + stats.getStatistics().size() + " statistiche: ");
        for (int i = 0; i < stats.getStatistics().size(); i++)
            System.out.println(stats.getStatistics().get(i).toString());
        printMainMenu();
    }

    public void printMedLastStats (Integer n) {
        System.out.println("Media delle ultime " + n.toString() + " statistiche: " + this.requests.getMedia(n));
        printMainMenu();
    }

    public void printDevStandard (Integer n) {
        System.out.println("Deviazione Standard delle ultime " + n.toString() + " statistiche: " + this.requests.getDevStandard(n));
        printMainMenu();
    }


    public int getInput (String sentence) {
        try {
            System.out.print(sentence);
            return scanner.nextInt();
        }
        catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.print("Opzione non disponibile.\n");
            return this.getInput(sentence);
        }
    }

    public void printWelcome () {
        System.out.println("--------------------------------------");
        System.out.println("     Benvenuto in Client Analyzer     ");
        System.out.println("--------------------------------------");
    }

    public void exit () {
        this.notifications.exit();
        System.exit(1);
    }

    public void printMainMenu () {
        System.out.println();
        System.out.println("1) Visualizza numero nodi nel quartiere");
        System.out.println("2) Visualizza ultime n statistiche");
        System.out.println("3) Visualizza media delle ultime n statistiche");
        System.out.println("4) Visualizza deviazione standard delle ultime n statistiche");
        System.out.println("5) Esci");
        Integer choice = this.getInput("Scegli un'opzione: ");
        try {
            switch (choice) {
                case 1: choice.equals(1);
                    printNodesNumber();
                case 2: choice.equals(2);
                    printLastStats(this.getInput("Quante statistiche vuoi vedere? "));
                case 3: choice.equals(3);
                    printMedLastStats(this.getInput("Quante statistiche vuoi usare per l'analisi? "));
                case 4: choice.equals(4);
                    printDevStandard(this.getInput("Quante statistiche vuoi usare per l'analisi? "));
                case 5: choice.equals(5);
                    this.exit();
                default:
                    System.out.println("Opzione non disponibile");
                    printMainMenu();
            }
        }
        catch (MessageBodyProviderNotFoundException e) {
            System.out.println("Non è stato possibile completare l'operazione, non ci sono abbastanza statistiche");
            printMainMenu();
        }
        catch (javax.ws.rs.ProcessingException e) {
            System.out.println("C'è stato un problema nel contattare il server");
            printMainMenu();
        }
    }

    public ClientConsole (ClientRequests requests, ClientNotificationsImpl notifications) {
        this.scanner = new Scanner(System.in);
        this.requests = requests;
        this.notifications = notifications;
        printWelcome();
        printMainMenu();
    }

}
