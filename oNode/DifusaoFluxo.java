package oNode;

import java.net.*;
import java.io.*;

public class DifusaoFluxo implements Runnable{
    final int intervaloMensagemMS = 5000; //5 segundos entre mensagens

    Rotas rotas;

    public DifusaoFluxo(Rotas rotas){
        this.rotas = rotas;
    }

    @Override
    public void run(){
        try{
            Thread.sleep(4000); //Esperar por construção de rotas

            while(true){
                try{
                    //Obter server mais favorável (menos delay)
                    String melhorServer = this.rotas.servidorMenosDelay();
                    Rota melhorRota = this.rotas.rotas.get(melhorServer);
                    String proximoNodo = melhorRota.nodoAnterior;
                    
                    Socket s = new Socket(proximoNodo,8090);
                    DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());
                    
                    //Enviamos ao próximo nodo qual o servidor
                    //a que nos desejamos conectar.
                    dataOut.writeUTF("FLUXO");
                    dataOut.writeUTF(melhorServer);
                    
                    dataOut.writeUTF("end");
                    s.close();
                }catch(Exception e){
                    e.printStackTrace();
                }

                //Esperar para voltar a enviar mensagem
                Thread.sleep(intervaloMensagemMS);
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
