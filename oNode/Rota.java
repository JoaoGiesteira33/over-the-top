package oNode;

public class Rota {
    String server;
    String nodoAnterior;
    int distancia;
    long delay;

    public Rota(String server, String nodoAnterior, int distancia, long delay){
        this.server = server;
        this.nodoAnterior = nodoAnterior;
        this.distancia = distancia;
        this.delay = delay;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("Server: ").append(this.server).append("\n");
        sb.append("Nodo Anterior: ").append(this.nodoAnterior).append("\n");
        sb.append("Distancia: ").append(this.distancia).append("\n");
        sb.append("Delay: ").append(this.delay).append("\n").append("-----------------------");

        return sb.toString();
    }
}
