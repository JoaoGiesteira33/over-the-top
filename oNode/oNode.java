package oNode;

public class oNode{
    public static void main(String[] args){
        if(args.length == 2 && args[0].equals("-b")){ //Server para nodo bootstrapper
            String config_file = args[1];

            BootstrapperServer sb = new BootstrapperServer(config_file);
            Thread sbThread = new Thread(sb);
            sbThread.start();

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
            System.out.println("Normal Node: oNode.java <bootstrapper_ip>");
            System.out.println("Bootstrapper Node: oNode.java -b <config_file>");
            return;
        }
    }
}