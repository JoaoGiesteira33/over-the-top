package oNode;

import java.net.*;
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
        String answer;

        while(true){
            try{
                messageReceived = this.dataIn.readUTF();
                answer = "Received (UPPER): " + messageReceived.toUpperCase();
                System.out.println("Receiver a message from: " + this.s);

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