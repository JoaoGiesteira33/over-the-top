package oNode;

import java.net.*;
import java.io.*;
import java.util.*;

/*
 * Cliente principal a correr em qualquer nodo
 * que se quer conectar à rede overlay.
 */
public class EntradaOverlay implements Runnable{ 
    String ipAddress; //Bootstrapper IP
    List<String> vizinhos;
    
    public EntradaOverlay(String ipAddress, List<String> vizinhos){
        this.ipAddress = ipAddress;
        this.vizinhos = vizinhos;
    }

    @Override
    public void run(){
        Scanner scanner = new Scanner(System.in);
        Socket s=null;
        try{
            s = new Socket(this.ipAddress, 8080);
            DataInputStream dataIn = new DataInputStream(s.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());

            //Pedido de Vizinhos
            dataOut.writeUTF("OVERLAY_JOIN_REQUEST");
            dataOut.flush();

            //Receber número de vizinhos
            int numeroVizinhos = dataIn.readInt();
            //Receber lista de vizinhos
            for(int i = 0 ; i < numeroVizinhos ; i++){
                String ipVizinho = dataIn.readUTF();
                this.vizinhos.add(ipVizinho);
                System.out.println("v" + (i+1) +") " + ipVizinho);
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        //Terminar conexão com bootstrapper
        scanner.close();
        try{
            s.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}