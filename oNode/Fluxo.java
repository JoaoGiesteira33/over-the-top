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


}
