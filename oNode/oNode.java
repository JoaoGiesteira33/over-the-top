package oNode;

public class oNode{
    public static void main(String[] args){
        if(args.length != 1){
            System.out.println("oNode.java <bootstrapper>");
            return;
        }

        String bootstrapper = args[0];
        System.out.println("Bootstrapper: " + bootstrapper);

        Server s = new Server();
        Thread serverThread = new Thread(s);
        serverThread.start();

        if(!bootstrapper.equals("Server")){
            Client c = new Client(bootstrapper);
            Thread clientThread = new Thread(c);
            clientThread.start();
        }
    }
}