package oNode;

import java.util.ArrayList;
import java.util.List;

public class Fluxos {
    List<Fluxo> fluxos;

    public Fluxos(){
        this.fluxos = new ArrayList<>();
    }

    /*
     * Dado um servidor, verifica se já existe
     * um fluxo com origem no mesmo.
     */
    private boolean existeFluxo(String servidor){
        for(Fluxo f : this.fluxos){
            if(f.fonte.equals(servidor))
                return true;
        }
        
        return false;
    }

    public void removeDestino(String destino){
        for(Fluxo f : this.fluxos){
            if(f.destinos.remove(destino))
                break;
        }
    }

    public void insereFluxo(String ipServidor,String senderIP, String proximoNodo){
        //Remover destino de outros fluxos que já possam existir
        removeDestino(senderIP);

        if(!existeFluxo(ipServidor)){
            List<String> newDestinos = new ArrayList<>();
            newDestinos.add(senderIP);
            this.fluxos.add(new Fluxo(ipServidor, proximoNodo, newDestinos, false));
        }else{ //Adiciona destino à lista de destinos deste servidor
            for(Fluxo f : this.fluxos){
                if(f.fonte.equals(ipServidor)){
                    f.destinos.add(senderIP);
                    f.origem = proximoNodo; //Atualizar origem, pode ter mudado desde inicio de programa
                    f.destinos.remove(proximoNodo); //Remover origem da lista de destinos
                    break;
                }
            }
        }
    }

    public void inserFluxoServer(String senderIP){
        //Se ainda não existir fluxo a sair deste streamer criamos um novo
        if(this.fluxos.size() == 0){
            List<String> destinos = new ArrayList<>();
            destinos.add(senderIP);
            this.fluxos.add(new Fluxo("", "", destinos, false));
            return;
        }

        //Se já existe fluxo, adicionamos este destino se ainda lá não estava
        if(this.fluxos.get(0).destinos.contains(senderIP)){
            return;
        }else{
            this.fluxos.get(0).destinos.add(senderIP);
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        for(Fluxo f : this.fluxos){
            sb.append(f.toString());
        }

        return sb.toString();
    }
}
