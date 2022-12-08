package oNode;

import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.io.*;
import java.util.List;

public class ClientHandler implements Runnable{
    final DataInputStream dataIn;
	final DataOutputStream dataOut;
	final Socket s;
    List<String> vizinhos;

    public ClientHandler(Socket s, DataInputStream din, DataOutputStream dout, List<String> vizinhos){
        this.s = s;
        this.dataIn = din;
        this.dataOut = dout;
        this.vizinhos = vizinhos;
    }

    @Override
    public void run(){
        String messageReceived;
        String answer = "";

        while(true){
            try{
                messageReceived = this.dataIn.readUTF();

                if(messageReceived.equals("end")){
                    System.out.println("Closing connection to " + this.s);
                    this.s.close();
                    break;
                }else if(messageReceived.equals("MONITORIZACAO")){
                    List<String> vizinhosRestantes = new ArrayList<>(vizinhos);
                    vizinhosRestantes.removeIf(v -> v.equals(this.s.getInetAddress().toString()));
                    
                    System.out.println("------------------");
                    System.out.println("Vizinhos: ");
                    System.out.println(this.vizinhos);
                    System.out.println("Received message from: " + this.s.getInetAddress().toString());
                    System.out.println("Vizinhos filtered");
                    System.out.println(vizinhosRestantes);
                    
                    //Handle mensagem de monitorizacao
                    String ipServidor = dataIn.readUTF();
                    int distanciaServidor = dataIn.readInt() + 1;

                    long currentTime = new Date().getTime();
                    long tempoSaida = dataIn.readLong();

                    System.out.println("Server: " + ipServidor + " | Distancia: " + distanciaServidor + " | Delay: " + (currentTime - tempoSaida) + "ms");
                }else{
                    System.out.println("Mensagem desconhecida. Terminando conexão.");
                    this.s.close();
                    break;
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        try{
            this.dataIn.close();
            this.dataOut.close();
            this.s.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}