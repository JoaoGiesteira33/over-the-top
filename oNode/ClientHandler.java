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
    Fluxos fluxos;
    Integer numeroNos;

    public ClientHandler(Socket s, DataInputStream din, DataOutputStream dout, List<String> vizinhos, Rotas rotas, Fluxos fluxos, Integer numeroNos){
        this.s = s;
        this.dataIn = din;
        this.dataOut = dout;
        this.vizinhos = vizinhos;
        this.rotas = rotas;
        this.fluxos = fluxos;
        this.numeroNos = numeroNos;
    }

    private void printInfo(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("------TABELA DE ROTAS------");
        System.out.println(this.rotas.toString());
        System.out.println("********TABELA DE FLUXOS********");
        System.out.println(this.fluxos.toString());
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

    private void reenviarMensagemFluxo(String ipServidor, String ipProximoNodo){
        try{
            Socket s = new Socket(ipProximoNodo, 8090);
            DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());

            dataOut.writeUTF("FLUXO");
            dataOut.writeUTF(ipServidor);

            dataOut.writeUTF("end");
            s.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void sinalizarFimFluxo(String ipProximoNodo){
        try{
            Socket s = new Socket(ipProximoNodo, 8090);
            DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());

            dataOut.writeUTF("FLUXO-END");

            dataOut.writeUTF("end");
            s.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        String messageReceived;
        String senderIP = this.s.getInetAddress().toString().substring(1);
        String myIP = this.s.getLocalAddress().toString().substring(1);

        while(true){
            try{
                messageReceived = this.dataIn.readUTF();

                if(messageReceived.equals("end")){
                    //System.out.println("Closing connection to " + this.s);
                    this.s.close();
                    break;
                }else if(messageReceived.equals("MONITORIZACAO")){
                    //Preparar lista de vizinhos para onde se irá enviar a mensagem
                    List<String> vizinhosRestantes = new ArrayList<>(vizinhos);
                    vizinhosRestantes.removeIf(v -> v.equals(senderIP));
                    
                    System.out.println("---------MONITORIZACAO---------");
                    System.out.println("Received message from: " + senderIP);
                    
                    //Receção da mensagem
                    String ipServidor = dataIn.readUTF();
                    int distanciaServidor = dataIn.readInt() + 1;

                    long currentTime = new Date().getTime();
                    long tempoSaida = dataIn.readLong();
                    long delayAcumulado = dataIn.readLong();

                    long delay = currentTime - tempoSaida;
                    long newDelay = delay + delayAcumulado;

                    System.out.println("Server: " + ipServidor + " | Distancia: " + distanciaServidor + " | Delay Total: " + newDelay + "ms" + " | Delay: " + delay + "ms");

                    //Guardar rota
                    Rota novaRota = new Rota(senderIP, distanciaServidor, newDelay);
                    boolean mudouRotas = this.rotas.insereRota(novaRota, ipServidor.substring(1));

                    //Obter melhor rota guardada (inserirRota nas rotas pode ter ignorado nova entrada)
                    Rota melhorRota = this.rotas.rotas.get(ipServidor.substring(1));
                    
                    //Enviar melhor rota para restantes vizinhos
                    if(melhorRota.distancia < numeroNos && mudouRotas){
                        printInfo();
                        for(String vizinho : vizinhosRestantes){
                            reenviarMensagemMonitorizacao(vizinho, ipServidor, melhorRota.distancia, melhorRota.delay);
                        }
                    }
                    
                    //System.out.println("------TABELA DE ROTAS------");
                    //System.out.println(this.rotas.toString());
                }else if(messageReceived.equals("FLUXO")){
                    //Servidor de onde vamos receber a stream
                    String ipServidor = dataIn.readUTF();
                    
                    //Verificamos se chegamos ao servidor desejado
                    if(ipServidor.equals(myIP)){
                        this.fluxos.inserFluxoServer(senderIP);
                    }else{
                        //Descobrir proximo nodo para chegar ao servidor recebido
                        String proximoNodo = this.rotas.rotas.get(ipServidor).nodoAnterior;

                        //Atualizacao de tabela de fluxo
                        String oldNode = this.fluxos.insereFluxo(ipServidor,senderIP,proximoNodo);

                        //Verificamos se é necessário avisar o nodo anterior que já não queremos receber fluxo dele
                        if(oldNode != ""){
                            sinalizarFimFluxo(oldNode);
                        }

                        //Enviar fluxos
                        for(Fluxo f : this.fluxos.fluxos){
                            if(f.destinos.isEmpty()){ //Já não se quer stream deste servidor, vamos sinalizar
                                sinalizarFimFluxo(f.origem);
                            }else{
                                reenviarMensagemFluxo(f.fonte, f.origem);
                            }
                        }
                    }
                    printInfo();
                    //System.out.println("********TABELA DE FLUXOS********");
                    //System.out.println(this.fluxos.toString());
                }else if(messageReceived.equals("FLUXO-END")){
                    //Remover IP de onde recebemos esta mensagem da lista de destinos
                    this.fluxos.removeDestino(senderIP);
                    printInfo();
                    //System.out.println("********TABELA DE FLUXOS********");
                    //System.out.println(this.fluxos.toString());
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