package oNode;

public class Rota {
    String nodoAnterior;
    int distancia;
    long delay;

    public Rota(String nodoAnterior, int distancia, long delay){
        this.nodoAnterior = nodoAnterior;
        this.distancia = distancia;
        this.delay = delay;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("Nodo Anterior: ").append(this.nodoAnterior).append("\n");
        sb.append("Distancia: ").append(this.distancia).append("\n");
        sb.append("Delay: ").append(this.delay).append("\n").append("-----------------------\n");

        return sb.toString();
    }
}
