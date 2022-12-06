package streaming;

import java.io.File;
import java.util.List;
import java.util.Objects;


/*
 * Ideia é ter uma classe Manager que recebe um ficheiro de config que tem o ip do Servidor, os ips dos encaminhadores e o ip do cliente, e 
 * contrói a seguinte extrutura com eles, encaminhando de seguida pela ordem suposta.
 * 
 * Dúvidas: - Da forma que está como funcionaria ? Ler ficheiro -> Contruir estruturas -> Correr por ordem ? Como sincronizar ? Ordem Contrária ?
 *          - Como fazer correr executáveis com Java ? R: Process Builder, Runtime.getRuntime().exec(), 
 *             
 */
public class Manager {
    private Servidor servidor;
    private Cliente cliente;
    private List<Encaminhador> encaminhadores; 


    public Manager() {
    }

    public Manager(File file){
        
    }

    public Manager(Servidor servidor, Cliente cliente, List<Encaminhador> encaminhadores) {
        this.servidor = servidor;
        this.cliente = cliente;
        this.encaminhadores = encaminhadores;
    }

    public Servidor getServidor() {
        return this.servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Encaminhador> getEncaminhadores() {
        return this.encaminhadores;
    }

    public void setEncaminhadores(List<Encaminhador> encaminhadores) {
        this.encaminhadores = encaminhadores;
    }

    public Manager servidor(Servidor servidor) {
        setServidor(servidor);
        return this;
    }

    public Manager cliente(Cliente cliente) {
        setCliente(cliente);
        return this;
    }

    public Manager encaminhadores(List<Encaminhador> encaminhadores) {
        setEncaminhadores(encaminhadores);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Manager)) {
            return false;
        }
        Manager manager = (Manager) o;
        return Objects.equals(servidor, manager.servidor) && Objects.equals(cliente, manager.cliente) && Objects.equals(encaminhadores, manager.encaminhadores);
    }

    @Override
    public int hashCode() {
        return Objects.hash(servidor, cliente, encaminhadores);
    }

    @Override
    public String toString() {
        return "{" +
            " servidor='" + getServidor() + "'" +
            ", cliente='" + getCliente() + "'" +
            ", encaminhadores='" + getEncaminhadores() + "'" +
            "}";
    }


}
