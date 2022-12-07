package oNode;

public class oNode{
    public static void main(String[] args){
        /*
         * Cada nodo tem de manter uma tabela de rotas,
         * por isso deve fazer sentido criar uma aqui:
         * 
         * TabelaRotas tr = new TabelaRotas();
         * ,por exemplo.
         */

         /*
          * De seguida temos 4 tipos de nodos:
            -> Default
            -> Clientes (-c)
            -> Servers (-s)
            -> Bootstrapper (-b)

            Um nodo pode ser default, server e cliente ao mesmo tempo, sendo 
            este o caso mais complicado, assim iniciar o programa com:
                > oNode -c -s 10.0.0.10, deve ser possível
            ou até,
                > oNode -c -s -b cfg1_1.
          */

        if(args.length == 2 && args[0].equals("-b")){ //Server para nodo bootstrapper
            String config_file = args[1];

            BootstrapperServer sb = new BootstrapperServer(config_file);
            Thread sbThread = new Thread(sb);
            sbThread.start();

        }else if(args.length == 2 && args[0].equals("-c")){ //Server para nodo cliente (visualizador de stream)
            /*
            Opção 1) 2º argumento é o IP do bootstrapper
            Opção 2) 2º argumento é IP de um servidor que faz stream 

            Origem no enunciado:
                -> "Os clientes devem ligar-se utilizando o mesmo executável,
                    podendo conhecer o endereço do
                    bootstrapper, ou dos nós aos
                    quais deve tentar obter o
                    stream desejado"
            */
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