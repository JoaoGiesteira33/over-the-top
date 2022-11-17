package oNode;

import java.net.*;
import java.util.List;
import java.util.Map;
import java.io.*;

public class ClientHandler implements Runnable{
    final DataInputStream dataIn;
	final DataOutputStream dataOut;
	final Socket s;

    private Map<String,List<String>> overlay;

    public ClientHandler(Socket s, DataInputStream din, DataOutputStream dout, Map<String,List<String>> overlay){
        this.s = s;
        this.dataIn = din;
        this.dataOut = dout;
        this.overlay = overlay;
    }

    @Override
    public void run(){
        String messageReceived;
        String answer;

        while(true){
            try{
                messageReceived = this.dataIn.readUTF();
                if(messageReceived.equals("end")){
                    System.out.println("Closing connection (" + this.s + ")");
                    this.s.close();
                    break;
                }

                System.out.println(this.overlay.get("" + this.s.getInetAddress()));
                for(String s : this.overlay.get(""+s.getInetAddress())){
                    dataOut.writeUTF(s);
                }
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