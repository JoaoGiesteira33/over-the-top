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
    Integer numeroNos;
    
    public EntradaOverlay(String ipAddress, List<String> vizinhos, Integer numeroNos){
        this.ipAddress = ipAddress;
        this.vizinhos = vizinhos;
        this.numeroNos = numeroNos;
    }

    @Override
    public void run(){
        Socket s=null;
        try{
            s = new Socket(this.ipAddress, 8080);
            DataInputStream dataIn = new DataInputStream(s.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());

            //Pedido de Entrada na Rede
            dataOut.writeUTF("OVERLAY_JOIN_REQUEST");

            //Receber número total de nós
            this.numeroNos = dataIn.readInt();
            System.out.println("Existem " + this.numeroNos + " nós na rede overlay!");

            //Receber número de vizinhos
            int numeroVizinhos = dataIn.readInt();
            

            //Receber lista de vizinhos
            for(int i = 0 ; i < numeroVizinhos ; i++){
                String ipVizinho = dataIn.readUTF();
                this.vizinhos.add(ipVizinho);
                System.out.println("v" + (i+1) +") " + ipVizinho);
            }

            dataOut.writeUTF("end");
        }catch(IOException e){
            e.printStackTrace();
        }

        //Terminar conexão com bootstrapper
        try{
            s.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}