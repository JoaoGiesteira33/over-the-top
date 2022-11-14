package oNode;

import java.net.*;
import java.io.*;
import java.util.*;

public class Client implements Runnable{ 
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