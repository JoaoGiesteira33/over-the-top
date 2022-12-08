package oNode;

import java.net.*;
import java.util.List;
import java.io.*;

public class Server implements Runnable{
    final int PORT = 8090;
    List<String> vizinhos; 
    Rotas rotas;

    public Server(List<String> vizinhos, Rotas rotas){
        this.vizinhos = vizinhos;
        this.rotas = rotas;
    }

    @Override
    public void run(){
        ServerSocket s = null;
        try{
            s = new ServerSocket(this.PORT);

            while(true){
                Socket clientSocket = s.accept();
                System.out.println("Nova conexão: " + clientSocket);

                DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
				DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());

                //New thread for new connection
                ClientHandler ch = new ClientHandler(clientSocket,dataIn,dataOut,vizinhos,rotas);
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