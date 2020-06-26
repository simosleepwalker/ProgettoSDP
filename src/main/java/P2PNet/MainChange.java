package P2PNet;

public class MainChange {

    public static void main(String[] args) {
        Node.changeNext(8080,5555);
        Node.changeNext(5555,8080);
        Node.changeNext(5555,4444);
        Node.changeNext(4444,8080);
        Node.sendToken("localhost",8080,p2p.nodes.Node.Token.newBuilder().setToken("PROVA COMUNICAZIONE").build());
    }

}
