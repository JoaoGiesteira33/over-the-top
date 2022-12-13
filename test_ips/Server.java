package test_ips;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static int port = 7777;

    public static void main(String[] args) {
        try{
            //create a server socket
            ServerSocket ss = new ServerSocket(port);
            //create a socket
            Socket s = ss.accept();
            //create an input stream
            DataInputStream input = new DataInputStream(s.getInputStream());
            //read a message from the client
            String message = input.readUTF();
            //print the message
            System.out.println("[RECEIVED]"+message+"\n[FROM]:"+s.getInetAddress().toString());
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
