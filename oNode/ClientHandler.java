package oNode;

import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.io.*;
import java.util.List;

public class ClientHandler implements Runnable{

    final DataInputStream dataIn;
	final DataOutputStream dataOut;
	final Socket s;

    List<String> vizinhos;
    Rotas rotas;

    public ClientHandler(Socket s, DataInputStream din, DataOutputStream dout, List<String> vizinhos, Rotas rotas){
        this.s = s;
        this.dataIn = din;
        this.dataOut = dout;
        this.vizinhos = vizinhos;
        this.rotas = rotas;
    }

    private void reenviarMensagemMonitorizacao(String vizinho,String ipServidor,int distanciaServidor,long delayAcumulado){
        try{
            Socket s = new Socket(vizinho,8090);
            DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());

            dataOut.writeUTF("MONITORIZACAO");
            dataOut.writeUTF(ipServidor);
            dataOut.writeInt(distanciaServidor);
            dataOut.writeLong(new Date().getTime());
            dataOut.writeLong(delayAcumulado);

            dataOut.writeUTF("end");
            s.close();
        }catch(Exception e){
            System.out.println("Vizinho ainda não conectado (" + vizinho + ")");
        }
    }

    @Override
    public void run(){
        String messageReceived;
        String senderIP = this.s.getInetAddress().toString().substring(1);

        while(true){
            try{
                messageReceived = this.dataIn.readUTF();

                if(messageReceived.equals("end")){
                    System.out.println("Closing connection to " + this.s);
                    this.s.close();
                    break;
                }else if(messageReceived.equals("MONITORIZACAO")){
                    //Preparar lista de vizinhos para onde se irá enviar a mensagem
                    List<String> vizinhosRestantes = new ArrayList<>(vizinhos);
                    vizinhosRestantes.removeIf(v -> v.equals(senderIP));
                    
                    System.out.println("------------------");
                    System.out.println("Vizinhos: ");
                    System.out.println(this.vizinhos);
                    System.out.println("Received message from: " + senderIP);
                    System.out.println("Vizinhos filtered");
                    System.out.println(vizinhosRestantes);
                    
                    //Receção da mensagem
                    String ipServidor = dataIn.readUTF();
                    int distanciaServidor = dataIn.readInt() + 1;

                    long currentTime = new Date().getTime();
                    long tempoSaida = dataIn.readLong();
                    long delayAcumulado = dataIn.readLong();

                    long delay = currentTime - tempoSaida;
                    long newDelay = delay + delayAcumulado;

                    System.out.println("Server: " + ipServidor + " | Distancia: " + distanciaServidor + " | Delay Total: " + newDelay + "ms" + " | Delay: " + delay + "ms");
                    System.out.println("-------------------");

                    //Guardar rota
                    Rota novaRota = new Rota(ipServidor.substring(1), senderIP, distanciaServidor, newDelay);
                    if(this.rotas.insereRota(novaRota)){
                        System.out.println("Inserimos a rota, continuar a enviá-la.");
                        //Se inserirmos a rota continuamos a propagá-la
                        for(String vizinho : vizinhosRestantes){
                            reenviarMensagemMonitorizacao(vizinho,ipServidor,distanciaServidor, newDelay);
                        }
                    }else{ //Se não inserirmos enviamos a melhor rota até ao momento para este server
                        System.out.println("Rota não inserida, enviar melhor rota guardada.");
                        Rota melhorRota = this.rotas.melhorRotaServer(ipServidor.substring(1));
                        //Propagar melhor rota para os vizinhos
                        for(String vizinho : vizinhosRestantes){
                            reenviarMensagemMonitorizacao(vizinho, ipServidor, melhorRota.distancia, melhorRota.delay);
                        }
                    }
                    System.out.println("------TABELA DE ROTAS------");
                    System.out.println(this.rotas.toString());
                }else{
                    System.out.println("Mensagem desconhecida. Terminando conexão.");
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
            this.s.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}