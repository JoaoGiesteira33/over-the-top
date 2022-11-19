package oNode;

import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;

public class ServerStreamer implements Runnable{
    final int PORT = 8080;
    String config_file;

    public ServerStreamer(String config_file){
        this.config_file = config_file;
    }

    @Override
    public void run(){
        Map<String,List<String>> overlay = new HashMap<>();
        System.out.println("Loading config file...");

        final String finalFileName = "../configFiles/" + this.config_file;
        File f = new File(finalFileName);
        overlay = ConfigFileParser.readFile(f);
        
        try{
            ServerSocket s = new ServerSocket(this.PORT);

            while(true){
                Socket clientSocket = s.accept();
                System.out.println("Nova conexão: " + clientSocket);

                DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
				DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());

                //New thread for new connection
                ClientHandlerStreamer ch = new ClientHandlerStreamer(clientSocket,dataIn,dataOut,overlay);
                Thread t = new Thread(ch);
                t.start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}