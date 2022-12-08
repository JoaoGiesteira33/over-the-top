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

                        dataOut.writeUTF("MONITORIZACAO");
                        dataOut.writeUTF(s.getLocalAddress().toString());
                        dataOut.writeInt(0);
                        dataOut.writeLong(new Date().getTime()); //Tempo de saida
                        dataOut.writeLong(0); //Delay acumulado
                        

                        dataOut.writeUTF("end");
                        s.close();
                        System.out.println("Mensagem de Monitorização enviada...");
                    }catch(Exception e){
                        System.out.println("Vizinho ainda não conectado (" + v + ")");
                    }
                }    

                //Esperar para voltar a enviar mensagem
                Thread.sleep(intervaloMensagemMS);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
