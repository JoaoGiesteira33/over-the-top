package oNode;

public class oNode{
    public static void main(String[] args){
        if(args.length < 1 || args.length > 2){
            System.out.println("Normal Node: oNode.java <bootstrapper>");
            System.out.println("Server Node: oNode.java \"Server\" <config_file>");
            return;
        }

        String bootstrapper = args[0];
        System.out.println("Bootstrapper: " + bootstrapper);

        //Server node
        String config_file = "";
        if(args.length == 2){
            if(!bootstrapper.equals("Server"))
            {
                System.out.println("Erro de argumentos!");
                return;
            }
            config_file = args[1];
        }

        if(args.length == 1 && bootstrapper.equals("Server")){
            System.out.println("Erro de argumentos!");
            return;
        }

        //Start server
        Server s = new Server(config_file);
        Thread serverThread = new Thread(s);
        serverThread.start();

        //Start talking to bootstrapper
        if(!bootstrapper.equals("Server")){
            Client c = new Client(bootstrapper);
            Thread clientThread = new Thread(c);
            clientThread.start();
        }
    }
}