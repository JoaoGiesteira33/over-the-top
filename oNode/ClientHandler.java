package oNode;

import java.net.*;
import java.util.Date;
import java.io.*;

public class ClientHandler implements Runnable{
    final DataInputStream dataIn;
	final DataOutputStream dataOut;
	final Socket s;

    public ClientHandler(Socket s, DataInputStream din, DataOutputStream dout){
        this.s = s;
        this.dataIn = din;
        this.dataOut = dout;
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
                    //Handle mensagem de monitorizacao
                    String ipServidor = dataIn.readUTF();
                    int distanciaServidor = dataIn.readInt();

                    long currentTime = new Date().getTime();
                    long tempoSaida = dataIn.readLong();

                    answer = ("Server: " + ipServidor + " | Distancia: " + distanciaServidor + " | Delay: " + (currentTime - tempoSaida) + "ms");
                }

                dataOut.writeUTF(answer);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        try{
            this.dataIn.close();
            this.dataOut.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}