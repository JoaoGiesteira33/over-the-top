package oNode;

import java.util.List;

public class Fluxo {
    String fonte;
    String origem;
    List<String> destinos;
    boolean ativo;

    public Fluxo(String fonte, String origem, List<String> destinos, boolean ativo){
        this.fonte = fonte;
        this.origem = origem;
        this.destinos = destinos;
        this.ativo = ativo;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("Server: ").append(fonte).append("\n");
        sb.append("Anterior: ").append(origem).append("\n"); 
        sb.append("Destinos: ");
        for(String d : this.destinos)
            sb.append(d).append(" | ");
        sb.append("\n").append("Estado: ").append(ativo).append("\n").append("-.-.-.-\n");

        return sb.toString();
    }
}
