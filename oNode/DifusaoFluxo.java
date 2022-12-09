package oNode;

import java.util.List;
import java.net.*;
import java.io.*;

public class DifusaoFluxo implements Runnable{
    Fluxos fluxos;
    Rotas rotas;
    List<String> vizinhos;

    public DifusaoFluxo(Fluxos fluxos, Rotas rotas, List<String> vizinhos){
        this.fluxos = fluxos;
        this.rotas = rotas;
        this.vizinhos = vizinhos;
    }

    @Override
    public void run(){
        try{
            Thread.sleep(2000); //Esperar por construção de rotas

            try{
                //Obter server mais favorável (menos delay)
                String melhorServer = this.rotas.servidorMenosDelay();
                Rota melhorRota = this.rotas.rotas.get(melhorServer);
                String proximoNodo = melhorRota.nodoAnterior;

                Socket s = new Socket(proximoNodo,8090);
                DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());

                //Enviamos ao próximo nodo qual o servidor
                //a que nos desejamos conectar.
                dataOut.writeUTF("FLUXO-C");
                dataOut.writeUTF(melhorServer);
                
                dataOut.writeUTF("end");
                s.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
