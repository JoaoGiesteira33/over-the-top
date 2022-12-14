package oNode;

import java.util.ArrayList;
import java.util.List;

import streaming.Cliente;
import streaming.Encaminhador;
import streaming.Servidor;

// Default ports da aplicação oNode
// Bootstrapper -> 8080
// Server Default -> 8090
public class oNode{
    public static void main(String[] args){
        Integer numeroNos = 33;
        List<String> vizinhos = new ArrayList<>();
        Rotas rotas = new Rotas();
        Fluxos fluxos = new Fluxos();

        if(args.length == 2 && args[0].equals("-b")){ //Server para nodo bootstrapper
            String config_file = args[1];
            //Falta adicionar lista de vizinhos...
            BootstrapperServer sb = new BootstrapperServer(config_file);
            Thread sbThread = new Thread(sb);
            sbThread.start();
        }else if(args.length == 2 && args[0].equals("-c")){ //Server para nodo cliente (visualizador de stream)
            String bootstrapper = args[1];

            //Server default para conseguir receber qualquer mensagem necessária
            Server s = new Server(vizinhos,rotas,fluxos,numeroNos);
            Thread sThread = new Thread(s);
            sThread.start();

            //Cliente para conseguir establece ligação ao bootstrapper
            EntradaOverlay c = new EntradaOverlay(bootstrapper,vizinhos,numeroNos);
            Thread clienThread = new Thread(c);
            clienThread.start();

            //Cliente para difusão de fluxo
            DifusaoFluxo df = new DifusaoFluxo(rotas);
            Thread dfThread = new Thread(df);
            dfThread.start();

            //Cliente
            Cliente cliente = new Cliente();
        }else if(args.length == 2 && args[0].equals("-s")){ //Server para nodo servidor (streamer de vídeo)
            String bootstrapper = args[1];

            //Server default para conseguir receber qualquer mensagem necessária
            Server s = new Server(vizinhos,rotas, fluxos, numeroNos);
            Thread sThread = new Thread(s);
            sThread.start();

            //Cliente para conseguir establece ligação ao bootstrapper
            EntradaOverlay c = new EntradaOverlay(bootstrapper,vizinhos,numeroNos);
            Thread clienThread = new Thread(c);
            clienThread.start();

            //Monitorização da Rede
            StreamerMonitorizacaoRede smr = new StreamerMonitorizacaoRede(vizinhos);
            Thread smrThread = new Thread(smr);
            smrThread.start();

            //Servidor
            Servidor servidor = new Servidor(fluxos);
            servidor.pack();
            servidor.setVisible(true);
        }else if(args.length == 2 && (args[0].equals("-cs") || args[0].equals("-sc"))){
            String bootstrapper = args[1];

            //Server default para conseguir receber qualquer mensagem necessária
            Server s = new Server(vizinhos,rotas,fluxos,numeroNos);
            Thread sThread = new Thread(s);
            sThread.start();

            //Cliente para conseguir establece ligação ao bootstrapper
            EntradaOverlay c = new EntradaOverlay(bootstrapper,vizinhos,numeroNos);
            Thread clienThread = new Thread(c);
            clienThread.start();

            //Cliente para difusão de fluxo
            DifusaoFluxo df = new DifusaoFluxo(rotas);
            Thread dfThread = new Thread(df);
            dfThread.start();

            //Monitorização da Rede
            StreamerMonitorizacaoRede smr = new StreamerMonitorizacaoRede(vizinhos);
            Thread smrThread = new Thread(smr);
            smrThread.start();

            //Cliente
            Cliente cliente = new Cliente();

            //Servidor
            Servidor servidor = new Servidor(fluxos);
            servidor.pack();
            servidor.setVisible(true);
        }else if(args.length == 1){ //Server para nodos restantes
            String bootstrapper = args[0];

            //Server default para conseguir receber qualquer mensagem necessária
            Server s = new Server(vizinhos, rotas, fluxos,numeroNos);
            Thread sThread = new Thread(s);
            sThread.start();
            
            //Cliente para conseguir establece ligação ao bootstrapper
            EntradaOverlay c = new EntradaOverlay(bootstrapper,vizinhos,numeroNos);
            Thread clientThread = new Thread(c);
            clientThread.start();

            //Encaminhador
            Encaminhador encaminhador = new Encaminhador(fluxos);
        }else{
            System.out.println("Normal Node: oNode.java <bootstrapper_ip>");
            System.out.println("Bootstrapper Node: oNode.java -b <config_file>");
            System.out.println("Cliente Node: oNode.java -c <bootstrapper_ip>");
            System.out.println("Servidor Node: oNode.java -s <bootstrapper_ip>");
            System.out.println("Cliente e Servidor Node: oNode.java -cs <bootstrapper_ip>");
            return;
        }
    }
}