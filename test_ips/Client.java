package test_ips;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    private static int port = 7777;
    public static void main(String[] args) {
        try{
            //create a server socket
            ServerSocket ss = new ServerSocket(port);
            //create a socket
            Socket s = ss.accept();
            //create an output stream
            DataOutputStream output = new DataOutputStream(s.getOutputStream());
            //send a message to the client
            output.writeUTF("Hello from: " + s.getInetAddress().toString());
            //close the socket
            s.close();
            //close the server socket
            ss.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
