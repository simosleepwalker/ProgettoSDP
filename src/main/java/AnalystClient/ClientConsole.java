package AnalystClient;

import Beans.StatisticsList;

import java.util.Scanner;

public class ClientConsole {

    private Scanner scanner;
    private ClientRequests requests;

    public void printNodesNumber () {
        System.out.println("Nodi nella rete: " + this.requests.getNodes().toString());
        printMainMenu();
    }

    public void printLastStats (Integer n) {
        System.out.println("Ultime " + n.toString() + " statistiche: ");
        StatisticsList stats = this.requests.getStats(n);
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

    public int getInput () {
        System.out.print("User Input: ");
        return scanner.nextInt();
    }

    public int getInput (String sentence) {
        System.out.print(sentence);
        return scanner.nextInt();
    }

    public void printWelcome () {
        System.out.println("--------------------------------------");
        System.out.println("     Benvenuto in Client Analyzer     ");
        System.out.println("--------------------------------------");
    }

    public void printMainMenu () {
        System.out.println();
        System.out.println("1) Visualizza numero nodi nel quartiere");
        System.out.println("2) Visualizza ultime n statistiche");
        System.out.println("3) Visualizza media delle ultime n statistiche");
        System.out.println("4) Visualizza deviazione standard delle ultime n statistiche");
        Integer choice = this.getInput();
        switch (choice) {
            case 1: choice.equals(1);
                printNodesNumber();
            case 2: choice.equals(2);
                printLastStats(this.getInput("Quante statistiche vuoi vedere? "));
            case 3: choice.equals(3);
                printMedLastStats(this.getInput("Quante statistiche vuoi usare per l'analisi? "));
            case 4: choice.equals(4);
                printDevStandard(this.getInput("Quante statistiche vuoi usare per l'analisi? "));
        }
    }

    public ClientConsole (ClientRequests requests) {
        this.scanner = new Scanner(System.in);
        this.requests = requests;
        printWelcome();
        printMainMenu();
    }

}
