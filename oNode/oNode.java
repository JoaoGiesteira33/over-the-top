package oNode;

public class oNode{
    public static void main(String[] args){
        //String bootstrapper = args[0];

        //Server para nodo streamer
        if(args.length == 2 && args[0].equals("Server")){
            String config_file = args[1];

            ServerStreamer ss = new ServerStreamer(config_file);
            Thread ssThread = new Thread(ss);
            ssThread.start();

        }else if(args.length == 1){ //Server para nodos restantes
            Server s = new Server();
            Thread sThread = new Thread(s);
            sThread.start();

            String bootstrapper = args[0];
            
            //Cliente para conseguir establece ligação ao bootstrapper
            EntradaOverlay c = new EntradaOverlay(bootstrapper);
            Thread clientThread = new Thread(c);
            clientThread.start();
        }

        else{
            System.out.println("Normal Node: oNode.java <bootstrapper>");
            System.out.println("Server Node: oNode.java \"Server\" <config_file>");
            return;
        }
    }
}