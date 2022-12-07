package oNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

public class Delay{
    private Map <List<String>, SimpleEntry<Float, Integer>> connectionDelay;

    public Delay(){
        this.connectionDelay = new HashMap<>();
    }

    public void addDelay(List<String> connection, float delay, int jumps){
        connection.sort(String::compareTo);
        this.connectionDelay.put(connection, new SimpleEntry<>(delay, jumps));
    }

    public SimpleEntry<Float, Integer> getDelay(List<String> connection){
        connection.sort(String::compareTo);
        return this.connectionDelay.get(connection);
    }

    public void removeDelay(List<String> connection){
        connection.sort(String::compareTo);
        this.connectionDelay.remove(connection);
    }

    public Map<List<String>, SimpleEntry<Float, Integer>> delayNeighbourns(String node){
        //all keys
        Map<List<String>, SimpleEntry<Float, Integer>> delayNeighbourns = new HashMap<>();
        for(List<String> key : this.connectionDelay.keySet()){
            if(key.get(0).equals(node)){
                delayNeighbourns.put(key, this.connectionDelay.get(key));
            }
            else if(key.get(1).equals(node)){
                delayNeighbourns.put(key, this.connectionDelay.get(key));
            }
        }
        return delayNeighbourns;
    }

       
}
