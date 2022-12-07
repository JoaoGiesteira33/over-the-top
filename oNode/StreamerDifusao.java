package oNode;

import java.util.List;
import java.util.Map;

public class StreamerDifusao implements Runnable{
    List<String> connected_nodes;
    Map<String,List<String>> overlay;

    public StreamerDifusao(Map<String,List<String>> overlay, List<String> connected_nodes){
        this.overlay = overlay;
        this.connected_nodes = connected_nodes;
    }

    @Override
    public void run(){
        
    }
}