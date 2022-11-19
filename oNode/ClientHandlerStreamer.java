package oNode;

import java.net.*;
import java.util.List;
import java.util.Map;
import java.io.*;

public class ClientHandlerStreamer implements Runnable{
    final DataInputStream dataIn;
	final DataOutputStream dataOut;
	final Socket s;

    private Map<String,List<String>> overlay;

    public ClientHandlerStreamer(Socket s, DataInputStream din, DataOutputStream dout, Map<String,List<String>> overlay){
        this.s = s;
        this.dataIn = din;
        this.dataOut = dout;
        this.overlay = overlay;
    }

    @Override
    public void run(){
        String senderIP = this.s.toString();
        String messageReceived;

        while(true){
            try{
                messageReceived = this.dataIn.readUTF();
                if(messageReceived.equals("end")){
                    System.out.println("Closing connection to " + senderIP);
                    this.s.close();
                    break;
                }

                List<String> vizinhos = this.overlay.get(senderIP);
                System.out.println(vizinhos);
                this.dataOut.writeInt(vizinhos.size());
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