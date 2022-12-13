package oNode;

import java.util.Map;
import java.util.HashMap;

public class Rotas {
    private final int tempo_por_salto_ms = 10;
    Map<String,Rota> rotas;

    public Rotas(){
        this.rotas = new HashMap<>();
    }

    public String servidorMenosDelay(){
        long min_delay = Long.MAX_VALUE;
        String min_delay_ip = "";
        
        for(Map.Entry<String,Rota> rotaEntry : this.rotas.entrySet()){
            
            if(rotaEntry.getValue().delay < min_delay){
                min_delay = rotaEntry.getValue().delay;
                min_delay_ip = rotaEntry.getKey();
            }
        }

        return min_delay_ip;
    }

    public boolean insereRota(Rota r, String server){
        //Verificar se já existe alguma rota para este servidor
        if(!this.rotas.containsKey(server)){
            this.rotas.put(server, r);
            return true;
        }else{
            //Obter rota atual para o servidor dado
            Rota atual = this.rotas.get(server);
            int dif_saltos = atual.distancia - r.distancia;


            //Verificar se rota é igual, o que significa que a velocidade da ligação mudou   
            if(atual.distancia == r.distancia && atual.nodoAnterior.equals(r.nodoAnterior)){

                //Mesmo que esta rota seja mais lenta temos de trocar
                this.rotas.put(server,r);
                return true;

            }else if(r.delay < (atual.delay + (tempo_por_salto_ms * dif_saltos))){

                //Se for uma rota nova apenas a colocamos se for mais rápida
                this.rotas.put(server, r);
                return true;

            }
        }

        return false;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        for(Map.Entry<String,Rota> r : this.rotas.entrySet())
            sb.append("Server: ").append(r.getKey()).append("\n").append(r.getValue().toString());
 
        return sb.toString();    
    }
}
