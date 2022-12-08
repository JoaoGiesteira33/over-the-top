package oNode;

import java.util.Map;
import java.util.HashMap;

public class Rotas {
    Map<String,Rota> rotas;

    public Rotas(){
        this.rotas = new HashMap<>();
    }

    public void insereRota(Rota r, String server){
        //Verificar se já existe alguma rota para este servidor
        if(!this.rotas.containsKey(server)){
            this.rotas.put(server, r);
        }

        //Se já existe rota verificamos se compensa adicionar nova rota
        long menorDelay = this.rotas.get(server).delay;
        if(r.delay < menorDelay)
        {
            //Trocar rota para este servidor
            this.rotas.put(server, r);
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        for(Map.Entry<String,Rota> r : this.rotas.entrySet())
            sb.append("Server: ").append(r.getKey()).append("\n").append(r.getValue().toString());
 
        return sb.toString();    
    }
}
