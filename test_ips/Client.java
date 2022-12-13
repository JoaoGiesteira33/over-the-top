package test_ips;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    private static int port = 7777;
    public static void main(String[] args) {
        try{
            Socket s = new Socket(args[0], port);
            //create an output stream
            DataOutputStream output = new DataOutputStream(s.getOutputStream());
            //send a message to the client
            output.writeUTF("Hello");
            //close the socket
            s.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
