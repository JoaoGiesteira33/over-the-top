package oNode;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;

public class ServerStreamer implements Runnable{
    final int PORT = 8080;
    String config_file;
    List<String> connected_nodes;

    public ServerStreamer(String config_file){
        this.config_file = config_file;
        this.connected_nodes = new ArrayList<>();
    }

    @Override
    public void run(){
        //Load de informação no config file
        Map<String,List<String>> overlay = new HashMap<>();
        System.out.println("Loading config file...");

        final String finalFileName = "../configFiles/" + this.config_file;
        File f = new File(finalFileName);
        overlay = ConfigFileParser.readFile(f);
        System.out.println("File loaded sucesfully!");
        
        //Inicializar serviço de difusão
        StreamerDifusao sd = new StreamerDifusao();
        Thread threadSD = new Thread(sd);
        threadSD.start(overlay,connected_nodes);

        //Escutar todos os pedidos
        try{
            ServerSocket s = new ServerSocket(this.PORT);

            while(true){
                Socket clientSocket = s.accept();
                System.out.println("Nova conexão: " + clientSocket);

                DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
				DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());

                //New thread for new connection
                ClientHandlerStreamer ch = new ClientHandlerStreamer(clientSocket,dataIn,dataOut,overlay,connected_nodes);
                Thread t = new Thread(ch);
                t.start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}