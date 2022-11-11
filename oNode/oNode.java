package oNode;

import java.net.*;
import java.io.*;
import java.util.*;

class Client implements Runnable{ 
    String ipAddress;
    
    public Client(String ipAddress){
        this.ipAddress = ipAddress;
    }
    @Override
    public void run(){
        Scanner scanner = new Scanner(System.in);

        try{
            Socket s = new Socket(this.ipAddress, 8080);           
            DataInputStream dataIn = new DataInputStream(s.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());

            while(true){
                String message = scanner.nextLine();
                dataOut.writeUTF(message);

                String answer = dataIn.readUTF();
                System.out.println(answer);
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        scanner.close();
    }
}

class ClientHandler implements Runnable{
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
        String answer;

        while(true){
            try{
                messageReceived = this.dataIn.readUTF();
                answer = "Received (UPPER): " + messageReceived.toUpperCase();

                if(messageReceived.equals("end")){
                    System.out.println("Closing connection (" + this.s + ")");
                    this.s.close();
                    break;
                }

                this.dataOut.writeUTF(answer);
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

class Server implements Runnable{
    final int PORT = 8080;

    public Server(){
    }
    @Override
    public void run(){
        try{
            ServerSocket s = new ServerSocket(this.PORT);

            while(true){
                Socket clientSocket = s.accept();
                System.out.println("Nova conexão: " + clientSocket);

                DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
				DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());

                //New thread for new connection
                ClientHandler ch = new ClientHandler(clientSocket,dataIn,dataOut);
                Thread t = new Thread(ch);
                t.start();
            }
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

public class oNode{
    public static void main(String[] args){
        if(args.length != 2){
            System.out.println("oNode.java <bootstrapper>");
            return;
        }

        String bootstrapper = args[1];

        Server s = new Server();
        Thread serverThread = new Thread(s);
        serverThread.start();

        if(!bootstrapper.equals("Server")){
            Client c = new Client(bootstrapper);
            Thread clientThread = new Thread(c);
            clientThread.start();
        }
    }
}