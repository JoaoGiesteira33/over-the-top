package oNode;

import java.util.List;
import java.util.ArrayList;

public class Rotas {
    List<Rota> rotas;

    public Rotas(){
        this.rotas = new ArrayList<>();
    }

    private boolean existeRota(String serverIP){
        boolean existe = false;

        for(Rota r : this.rotas){
            if(r.server.equals(serverIP))
                existe = true;
        }

        return existe;
    }

    private long menorDelayServer(String serverIP){
        long menorDelay = Long.MAX_VALUE;

        for(Rota r : this.rotas){
            if(r.server.equals(serverIP) && r.delay < menorDelay){
                menorDelay = r.delay;
            }
        }

        return menorDelay;
    }

    public Rota melhorRotaServer(String serverIP){
        if(this.rotas.size() == 0) return null;
        
        Rota melhorRota = this.rotas.get(0);
        for(int i = 1 ; i < this.rotas.size() ; i++){
            Rota itRota = this.rotas.get(i);
            if(itRota.server.equals(serverIP) && itRota.delay < melhorRota.delay)
                melhorRota = itRota;
        }

        return melhorRota;
    }

    /*
     * Método que insere uma rota na lista de rotas.
     * 
     * Primeiro verifica se já existe um caminho melhor.
     * 
     * Se já existir um caminho melhor não vale a pena guardar 
     * esta rota e continuar a propagála, retorna FALSE.
     * 
     * Se ainda não existe ou é pior adiciona-se a nova rota e retorna TRUE.
     */
    public boolean insereRota(Rota r){
        String server = r.server;
        
        //Verificar se já existe alguma rota para este servidor
        if(!existeRota(server)){
            this.rotas.add(r);
            return true;
        }

        //Se já existe rota verificamos se compensa adicionar nova rota
        long menorDelay = menorDelayServer(server);
        if(r.delay < menorDelay)
        {
            this.rotas.add(r);
            return true;
        }

        return false;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        for(Rota r : this.rotas)
            sb.append(r.toString());
        
        return sb.toString();    
    }
}
