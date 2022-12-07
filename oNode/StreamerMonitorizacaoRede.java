package oNode;

import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.io.*;

public class StreamerMonitorizacaoRede implements Runnable{
    List<String> vizinhos;
    final int intervaloMensagemMS = 3000; //3 segundos entre mensagens

    public StreamerMonitorizacaoRede(List<String> vizinhos){
        this.vizinhos = vizinhos;
    }

    @Override
    public void run(){
        try{
            while (true) {
                //Enviar mensagem de controlo a todos os vizinhos
                for(String v : this.vizinhos){
                    try{
                        Socket s = new Socket(v,8090);
                        DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());
                        DataInputStream dataIn = new DataInputStream(s.getInputStream());

                        dataOut.writeUTF("MONITORIZACAO");
                        dataOut.writeUTF(s.getLocalAddress().toString());
                        dataOut.writeInt(0);
                        dataOut.writeLong(new Date().getTime());

                        System.out.println(s.getInetAddress() + " | " + dataIn.readUTF());
                        s.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }    

                //Esperar para voltar a enviar mensagem
                Thread.sleep(intervaloMensagemMS);
            }
        } catch (InterruptedException ex) {
            //SomeFishCatching
        }
    }
}