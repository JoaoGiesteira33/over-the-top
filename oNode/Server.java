package oNode;

import java.net.*;
import java.util.List;
import java.io.*;

public class Server implements Runnable{
    final int PORT = 8090;
    List<String> vizinhos; 
    Rotas rotas;
    Integer numeroNos;
    Fluxos fluxos;

    public Server(List<String> vizinhos, Rotas rotas, Fluxos fluxos, Integer numeroNos){
        this.vizinhos = vizinhos;
        this.rotas = rotas;
        this.fluxos = fluxos;
        this.numeroNos = numeroNos;
    }

    @Override
    public void run(){
        ServerSocket s = null;
        try{
            s = new ServerSocket(this.PORT);

            while(true){
                Socket clientSocket = s.accept();

                DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
				DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());

                //New thread for new connection
                ClientHandler ch = new ClientHandler(clientSocket,dataIn,dataOut,vizinhos,rotas,fluxos,numeroNos);
                Thread t = new Thread(ch);
                t.start();
            }
            
        }catch(IOException e){
            e.printStackTrace();
        }
        try{
            s.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}