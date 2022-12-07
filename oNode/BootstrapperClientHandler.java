package oNode;

import java.net.*;
import java.util.List;
import java.util.Map;
import java.io.*;

public class BootstrapperClientHandler implements Runnable{
    final DataInputStream dataIn;
	final DataOutputStream dataOut;
	final Socket s;

    private Map<String,List<String>> overlay;
    private List<String> connected_nodes;

    public BootstrapperClientHandler(
        Socket s, 
            DataInputStream din, 
                DataOutputStream dout,
                    Map<String,List<String>> overlay,
                        List<String> connected_nodes){
        this.s = s;
        this.dataIn = din;
        this.dataOut = dout;
        this.overlay = overlay;
        this.connected_nodes = connected_nodes;
    }

    @Override
    public void run(){
        String senderIP = this.s.getInetAddress().toString();
        String messageReceived;

        while(true){
            try{
                messageReceived = this.dataIn.readUTF();

                if(messageReceived.equals("end")){
                    System.out.println("Closing connection to " + senderIP);

                    //Remover nodo da lista de nodos conectados
                    this.connected_nodes.remove(senderIP);

                    this.s.close();
                    break;
                }else if(messageReceived.equals("OVERLAY_JOIN_REQUEST")){
                    System.out.println("Asking to join overlay: " + senderIP);
                    List<String> vizinhos = this.overlay.get(senderIP);

                    //Enviar número de vizinhos
                    this.dataOut.writeInt(vizinhos.size());

                    //Enviar ip de cada vizinho
                    for(String ip : vizinhos){
                        this.dataOut.writeUTF(ip);
                    }

                    //Adicionar Nodo à lista de nodos conectados
                    this.connected_nodes.add(senderIP);
                }else{
                    System.out.println("Unkown Message! Shutting Down!");
                    this.dataOut.writeUTF("Unkown Message! Shutting Down!");
                    
                    //Remover nodo da lista de nodos conectados
                    this.connected_nodes.remove(senderIP);
                    
                    this.s.close();
                    break;
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