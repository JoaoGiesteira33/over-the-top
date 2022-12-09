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

    private void removeDestino(String destino){
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
            this.fluxos.add(new Fluxo(ipServidor, proximoNodo, null, false));
            return;
        }else{ //Adiciona destino à lista de destinos deste servidor
            for(Fluxo f : this.fluxos){
                if(f.fonte.equals(ipServidor)){
                    f.destinos.add(senderIP);
                    f.origem = proximoNodo; //Atualizar origem, pode ter mudado desde inicio de programa
                    break;
                }
            }
        }


    }
}
